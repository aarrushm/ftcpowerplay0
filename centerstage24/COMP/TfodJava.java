package org.firstinspires.ftc.teamcode.centerstage24.COMP;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import java.util.List;
public class TfodJava {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    private TfodProcessor tfod;
    private VisionPortal visionPortal;


    private static final String TFOD_MODEL_ASSET = "CenterStage.tflite";
    private static final String[] LABELS = {
            "Pixel",
        //    "red"
    };
    public enum object_pos {
        POS_UNKNOWN,
        POS_FOUND, // Duck

//This Enum is what is returned to the main op mode.
    }
    private ElapsedTime buffer = new ElapsedTime();



    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // Use setModelAssetName() if the TF Model is built in as an asset.
                .setModelAssetName(TFOD_MODEL_ASSET)

                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));


        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()
    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;
    public TfodJava (HardwareMap h, Telemetry t) {//constructor
        telemetry = t;
        hardwareMap = h;
        telemetry.addData(String.format("TfodJava: constructor called"),"");
        telemetry.update();
        initTfod();

    }

    public object_pos ObjectPos () {//Main function

        boolean detected = false;
        String found = "Object Not Found";
        object_pos pos = object_pos.POS_UNKNOWN; // Assuming that if the Duck is not detected it is on the left

        telemetry.addData(String.format("ObjectPos Called"),"");
        //telemetry.update();



        //setting zoom and activated Tfod (starting detection)

        int number_of_loops = 0;
        buffer.reset();
        while (detected == false) {
            telemetry.addData(String.format("Inside While loop %d", number_of_loops),"");
            //telemetry.update();

            if (buffer.seconds() > 2){
                visionPortal.stopStreaming();
                break;
            }//If the loop has run for more than 5 seconds, loop will end to allow autonomous run to continue, and will return level 1 as the level with the duck in it. (see CalculateSleeve() function above)


            telemetry.update();
            List<Recognition> currentRecognitions = tfod.getRecognitions();//
            if (currentRecognitions != null) {
                telemetry.addData("# Object Detected", currentRecognitions.size());
                // step through the list of recognitions and display boundary info.


                int i = 0;
                for (Recognition recognition : currentRecognitions) {//This loop runs until the list of updated recongitions (objects in view) are displayed in the telemetry with their coordanites updated.
                    //In the case of this function, Only one object (the duck) should be visible, but if multiple object are detected, the function will exit the loop after the duck is detected, and use the ducks coordanites
                    String thislabel = recognition.getLabel();//These three variables establish the asset type (whether the object is a duck) left and right values as a float that can be used in the logic
                    //float thisLeft = recognition.getLeft();
                    //float thisRight = recognition.getRight();

                    double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
                    double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

                    telemetry.addData(""," ");
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                    telemetry.addData("- Position", "%.0f / %.0f", x, y);
                    telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());


                    if (thislabel == LABELS[0]) {//Checks if detected object is a Pixel
                        //Found Sleeve 1
                        pos = object_pos.POS_FOUND; // Assuming that if the Duck is not detected it is on the left
                        detected = true;//boolean that while loop runs on so that while loop is exited after if statement is finished
                        telemetry.addData(String.format("Sleeve Detected"),"");
                        RobotLog.d("Sleeve Detected");
                        break;//exit loop


                    }
                    /*
                    if (thislabel == LABELS[1]) {//Checks if detected object is a Cube
                        //Found Sleeve 1
                        level = object_pos.POS_1; // Assuming that if the Duck is not detected it is on the left
                        detected = true;//boolean that while loop runs on so that while loop is exited after if statement is finished
                        telemetry.addData(String.format("Sleeve Detected"),"");
                        RobotLog.d("Sleeve Detected");
                        tfod.deactivate();//stop detecting
                        break;//exit loop
                    }
                    */



                }

            }
            number_of_loops++;
        }

        return pos;//return Barcode Level.
    }
}