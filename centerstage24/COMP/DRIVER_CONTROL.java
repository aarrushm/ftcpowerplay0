package org.firstinspires.ftc.teamcode.centerstage24.COMP;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive_DC;

@TeleOp(name="DRIVER_CONTROL", group="Linear Opmode") // @Autonomous(...) is the other common choice
public class DRIVER_CONTROL extends LinearOpMode /*implements Runnable*/ {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private Slide.level level = Slide.level.LEVEL_0_DC;
    private slidev2 slide = null;
    private intake intake = null;

    private Servos launch = null;

    private Servos outtake = null;

    //private transfer transfer = null;

    private transfer transfer = null;

    //private boolean opmodeactive = false;
    @Override
    public void runOpMode() {
        //opmodeactive = true;
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        SampleMecanumDrive_DC drive = new SampleMecanumDrive_DC(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double velocity = 0.5;

    // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        intake = new intake(hardwareMap, telemetry);
        transfer = new transfer(hardwareMap, telemetry);
        slide = new slidev2(hardwareMap,telemetry);
        launch = new Servos(hardwareMap, telemetry,true);
        outtake = new Servos(hardwareMap, telemetry, false);


        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());

            //robot movement

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y*velocity,
                            -gamepad1.left_stick_x*velocity,
                            gamepad1.right_stick_x*velocity
                    )
            );


            // intake + outtake
            if (gamepad2.right_bumper == true) {
                intake.runbackward(1000,0);
            } else if (gamepad2.left_bumper == true) {
                intake.stop();
            }

            // airplane
            if (gamepad1.dpad_down == true) {
                launch.forwardMAX();
            } else if (gamepad1.dpad_up == true) {
                launch.reverseMAX();
            }

            // transfer
            if (gamepad2.y == true) {
                transfer.runbackward(1000,0);
                //RobotLog.d("Button click A");
            } else if (gamepad2.a == true) {
                transfer.runforward(1000,0);
                //RobotLog.d("Button click X");
            } else {
                transfer.stop();
            }

            //velocity controls
            if (gamepad1.left_trigger >= 0.8) {
                velocity = 0.2;
            } else if (gamepad1.right_trigger >= 0.8) {
                velocity = 1.5;
            } else{
                velocity = 0.5;
            }

            //slide movement
            if (gamepad1.right_bumper == true) {
                //level = Slide.level.LEVEL_0;
                //slide.MoveToLevelAsync(level);
                slide.runforward(4000,0);
                //RobotLog.d("Button click A");
            /*} else if (gamepad1.b == true) {
                level = Slide.level.LEVEL_1_DC;
                slide.MoveToLevelAsync(level);
                //RobotLog.d("Button click B");*/
            } else if (gamepad1.left_bumper == true) {
                //level = Slide.level.LEVEL_2_DC;
                //slide.MoveToLevelAsync(level);
                slide.runbackward(4000,0);
                //RobotLog.d("Button click X");
            } else {
                slide.stop();
            }

            //outtake
            if (gamepad2.dpad_down == true) {
                outtake.forwardMAX();
            } else if (gamepad2.dpad_up == true) {
                outtake.reverseMAX();
            }

        }
    }

}