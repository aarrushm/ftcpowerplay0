package org.firstinspires.ftc.teamcode.mecanum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive_DC;

@TeleOp(name="DRIVER_CONTROL", group="Linear Opmode") // @Autonomous(...) is the other common choice
public class DRIVER_CONTROL extends LinearOpMode /*implements Runnable*/ {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private Slide rightslide = null;
    private Slide.level level = Slide.level.LEVEL_0_DC;
    private Slide leftslide = null;
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

        Servos intake = new Servos(hardwareMap, telemetry, false);
        rightslide = new Slide(hardwareMap, telemetry, "rightslide");
        leftslide = new Slide(hardwareMap, telemetry, "leftslide");

        //Thread t = new Thread(this, "arm thread");
        //t.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());

            //robot movement

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y*velocity,
                            -gamepad1.left_stick_x*velocity,
                            -gamepad1.right_stick_x*velocity
                    )
            );


            // intake + outtake
            if (gamepad2.right_bumper == true) {
                intake.forwardMAX();
            } else if (gamepad2.left_bumper == true) {
                intake.reverseMAX();
            } else{
                intake.holdPosition();
            }

            if (gamepad1.left_trigger >= 0.8) {
                velocity = 0.2;
            } else if (gamepad1.right_trigger >= 0.8) {
                velocity = 1.5;
            } else{
                velocity = 0.5;
            }

            if (gamepad1.a == true) {
                level = Slide.level.LEVEL_0;
                rightslide.MoveToLevelAsync(level);
                leftslide.MoveToLevelAsync(level);
                //RobotLog.d("Button click A");
            } else if (gamepad1.b == true) {
                level = Slide.level.LEVEL_1_DC;
                rightslide.MoveToLevelAsync(level);
                leftslide.MoveToLevelAsync(level);
                //RobotLog.d("Button click B");
            } else if (gamepad1.x == true) {
                level = Slide.level.LEVEL_2_DC;
                rightslide.MoveToLevelAsync(level);
                leftslide.MoveToLevelAsync(level);
                //RobotLog.d("Button click X");
            } else if (gamepad2.a == true) {
                level = Slide.level.AUTO_2;
                rightslide.MoveToLevelAsync(level);
                leftslide.MoveToLevelAsync(level);
                //RobotLog.d("Button click X");
            } else if (gamepad1.y == true) {

                level = Slide.level.LEVEL_3;
                rightslide.MoveToLevelAsync(level);
                leftslide.MoveToLevelAsync(level);
                //RobotLog.d("Button click Y");
            }

        }
    }

}