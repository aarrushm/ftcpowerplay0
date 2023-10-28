
package org.firstinspires.ftc.teamcode.mecanum;


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


    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private static final String TFOD_MODEL_ASSET = "sleeve.tflite";
    private static final String[] LABELS = {
            "green",
            "pink"
    };

        public enum barcode_level {
            SLEEVE_UNKNOWN,
            SLEEVE_1, // Duck
            SLEEVE_2,//Cube
            SLEEVE_3,//Ball
//This Enum is what is returned to the main op mode.
    }
    private ElapsedTime buffer = new ElapsedTime();


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
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);//Loading Assets
    }
    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;
    public VuforiaFTC (HardwareMap h, Telemetry t) {//constructor
        telemetry = t;
        hardwareMap = h;
        telemetry.addData(String.format("VuforiaFTC: constructor called"),"");
        telemetry.update();
        initVuforia();
        initTfod();
    }

    public barcode_level BarcodeLevel () {//Main function

        boolean detected = false;
        String found = "Object Not Found";
        barcode_level level = barcode_level.SLEEVE_3; // Assuming that if the Duck is not detected it is on the left
     
        telemetry.addData(String.format("BarcodeLevel Called"),"");
        //telemetry.update();
        if (tfod != null) {
            tfod.activate();


            tfod.setZoom(1.0, 16.0/9.0);
        }//setting zoom and activated Tfod (starting detection)

        int number_of_loops = 0;
        buffer.reset();
        while (detected == false) {
            telemetry.addData(String.format("Inside While loop %d", number_of_loops),"");
            //telemetry.update();

            if (buffer.seconds() > 2){
                tfod.deactivate();
                  break;
            }//If the loop has run for more than 5 seconds, loop will end to allow autonomous run to continue, and will return level 1 as the level with the duck in it. (see CalculateSleeve() function above)


            telemetry.update();
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();//
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.

                int i = 0;
                for (Recognition recognition : updatedRecognitions) {//This loop runs until the list of updated recongitions (objects in view) are displayed in the telemetry with their coordanites updated.
                    //In the case of this function, Only one object (the duck) should be visible, but if multiple object are detected, the function will exit the loop after the duck is detected, and use the ducks coordanites
                    String thislabel = recognition.getLabel();//These three variables establish the asset type (whether the object is a duck) left and right values as a float that can be used in the logic
                    //float thisLeft = recognition.getLeft();
                    //float thisRight = recognition.getRight();

                    telemetry.addData(String.format("label (%d)", i), thislabel);
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());//Left and Top percentages of the Duck (how much of the duck that is seen is the left side, top, etc)
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());//Right and Bottom percentages of the Duck

                    if (thislabel == LABELS[0]) {//Checks if detected object is a Ball
                        //Found Sleeve 1
                        level = barcode_level.SLEEVE_2; // Assuming that if the Duck is not detected it is on the left
                        detected = true;//boolean that while loop runs on so that while loop is exited after if statement is finished
                        telemetry.addData(String.format("Sleeve Detected"),"");
                        RobotLog.d("Sleeve Detected");
                       tfod.deactivate();//stop detecting
                       break;//exit loop
                    }if (thislabel == LABELS[1]) {//Checks if detected object is a Cube
                        //Found Sleeve 1
                        level = barcode_level.SLEEVE_1; // Assuming that if the Duck is not detected it is on the left
                        detected = true;//boolean that while loop runs on so that while loop is exited after if statement is finished
                        telemetry.addData(String.format("Sleeve Detected"),"");
                        RobotLog.d("Sleeve Detected");
                        tfod.deactivate();//stop detecting
                        break;//exit loop
                    }
                    i++;
                }

            }
            number_of_loops++;
        }




        return level;//return Barcode Level.

    }
}


