
package org.firstinspires.ftc.teamcode.vuforia;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
public class VuforiaFTC {

    private static final String VUFORIA_KEY = "AWW8IJP/////AAABmeA2L95wv0HGsNBs3dEtJUQOz44cqs9iyhzeEsTgRZMTalglfTSfEPyWKteMpNfpBeTp3Ca5AFBB2NrS8isl7ktX+M5Iq6c7FeLqAaBOuf1votUcE2N5oYqLWEcuZdKpeNbsQlUd1z+SiYV3WgA1bHFJFeHzXMRGeNf2RhSy4Xo+JcLeOadsXkti3oZJIRZxn1o44Tfjqeu1IJfA9ofBtLIgEC3FOpYJkM+B3UR7eaBNgQwG5MzwZTHVTdOZTllC3qtoXsuIcPt3wCbtibTjGn54TX1eO9epUrWhtVWD1NWlbF0a/+DrQCs4YO/EphyeoR1r+PBevjNPkxbXQ47ud4Gjs/Iuq05RqEng/O9EzdYf";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

        public enum barcode_level {
            BARCODE_LEVEL_UNKNOWN,
            BARCODE_LEVEL_1,//LEFT
            BARCODE_LEVEL_2,//MIDDLE
            BARCODE_LEVEL_3,//RIGHT

    }
    private ElapsedTime buffer = new ElapsedTime();


    //logic:
    private barcode_level CalculateBarcodeLvl(float left, float right) {
        barcode_level verdict = barcode_level.BARCODE_LEVEL_UNKNOWN;
        telemetry.addData(String.format("Inside Logic"),"");


            if (left <= 330.5) {
                verdict = barcode_level.BARCODE_LEVEL_2;
                telemetry.addData(String.format("Level 2, Object"), "");
                RobotLog.d ("Level 2");
            } else {
                verdict = barcode_level.BARCODE_LEVEL_3;
                telemetry.addData(String.format("Level 3, Object"), "");
                RobotLog.d ("Level 3");
            }

        return verdict;
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;
    public VuforiaFTC (HardwareMap h, Telemetry t) {
        telemetry = t;
        hardwareMap = h;
        telemetry.addData(String.format("VuforiaFTC: constructor called"),"");
        telemetry.update();
        initVuforia();
        initTfod();
    }

//Main Function:
    public barcode_level BarcodeLevel () {
        boolean detected = false;
        String found = "Object Not Found";
        barcode_level level = barcode_level.BARCODE_LEVEL_1; // Assuming that if the Duck is not detected it is on the left (VERIFY THIS ASSUMPTION !!!)

        telemetry.addData(String.format("BarcodeLevel Called"),"");
        //telemetry.update();
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            //tfod.setZoom(1.0, 16.0/9.0);
            tfod.setZoom(2.5, 16.0/9.0);
        }

        int number_of_loops = 0;
        buffer.reset();
        while (detected == false) {
            telemetry.addData(String.format("Inside While loop, Roshens Amazing, %d", number_of_loops),"");
            //telemetry.update();

            if (buffer.seconds() > 5){
                tfod.deactivate();
                  break;
            }


         //   RobotLog.d("Inside While Loop");

            telemetry.update();
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Detecting object", updatedRecognitions.size());
                RobotLog.d("Detecting object");
                // step through the list of recognitions and display boundary info.

                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    String thislabel = recognition.getLabel();
                    float thisLeft = recognition.getLeft();
                    float thisRight = recognition.getRight();

                    telemetry.addData(String.format("label (%d)", i), thislabel);
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    RobotLog.d("Object detected left = %.03f, right = %.03f, label = %s", thisLeft, thisRight, thislabel);
                    if (thislabel == LABELS[0] || thislabel == LABELS[1] || thislabel == LABELS[2]) {
                        //Found Duck
                        //telemetry.addData(String.format(" Found Duck !!");
                        detected = true;
                        level = CalculateBarcodeLvl(thisLeft, thisRight);
                        telemetry.addData(String.format("Object Detected"),"");
                        RobotLog.d("Object Detected");

                        tfod.deactivate();
                        break;
                    }

                    i++;
                }

                //telemetry.update();
            }
            number_of_loops++;
        }




        return level;

    }
}


