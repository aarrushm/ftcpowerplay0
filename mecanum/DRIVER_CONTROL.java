package org.firstinspires.ftc.teamcode.mecanum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive_DC;

@TeleOp(name="DRIVER_CONTROL", group="Linear Opmode") // @Autonomous(...) is the other common choice
public class DRIVER_CONTROL extends LinearOpMode /*implements Runnable*/ {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private Slide.level level = Slide.level.LEVEL_0_DC;
    private slidev2 slide = null;
    private intake intake = null;

    //private transfer transfer = null;

    private Slide outtake = null;

    private Slide transfer = null;
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
        transfer = new Slide(hardwareMap, telemetry, "transfer");
        //slide = new Slide(hardwareMap, telemetry, "slide");
        slide = new slidev2(hardwareMap,telemetry);
        outtake = new Slide(hardwareMap, telemetry, "outtake");

        //Thread t = new Thread(this, "arm thread");
        //t.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());

            //robot movement

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y*velocity,
                            gamepad1.left_stick_x*velocity,
                            gamepad1.right_stick_x*velocity
                    )
            );


            // intake + outtake
            if (gamepad1.right_bumper == true) {
                intake.runbackward(1000,0);
            } else if (gamepad1.left_bumper == true) {
                intake.stop();
            }

            /*if (gamepad1.b == true) {
                transfer.runforward(600,0);
            } else if (gamepad1.x == true) {
                transfer.runbackward(600,0);
            } else{
                transfer.stop();
            }*/

            if (gamepad1.b == true) {
                level = Slide.level.LEVEL_0;
                transfer.MoveToLevelAsync(level);
                //RobotLog.d("Button click A");
            } else if (gamepad1.x == true) {
                level = Slide.level.AUTO_2;
                transfer.MoveToLevelAsync(level);
                //RobotLog.d("Button click X");
            }

            if (gamepad1.left_trigger >= 0.8) {
                velocity = 0.2;
            } else if (gamepad1.right_trigger >= 0.8) {
                velocity = 1.5;
            } else{
                velocity = 0.5;
            }

            if (gamepad1.a == true) {
                //level = Slide.level.LEVEL_0;
                //slide.MoveToLevelAsync(level);
                slide.runforward(1000,0);
                //RobotLog.d("Button click A");
            /*} else if (gamepad1.b == true) {
                level = Slide.level.LEVEL_1_DC;
                slide.MoveToLevelAsync(level);
                //RobotLog.d("Button click B");*/
            } else if (gamepad1.y == true) {
                //level = Slide.level.LEVEL_2_DC;
                //slide.MoveToLevelAsync(level);
                slide.runbackward(1000,0);
                //RobotLog.d("Button click X");
            } else {
                slide.stop();
            }

            if (gamepad1.left_stick_button == true) {
                level = Slide.level.LEVEL_0;
                outtake.MoveToLevelAsync(level);
                //RobotLog.d("Button click A");
            } else if (gamepad1.right_stick_button == true) {
                level = Slide.level.LEVEL_1_DC;
                outtake.MoveToLevelAsync(level);
                //RobotLog.d("Button click X");
            }

        }
    }

}