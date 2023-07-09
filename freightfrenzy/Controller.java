package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

/*
Gamepad 2 Controls:
    Joystick forward - robot moves forward
    Joystick right - robot moves right
    Joystick left - robot moves left
    Joystick back - robot moves back
    Right bumper - intake freight
    Left bumper - outtake freight
Gamepad 1 Controls:
    A - ground level
    B - level 1
    X - level 2
    Y - level 3
    Right bumper - Custom level for the shared shipping hub
    Push right joystick straight down - spin carousel right
    Push left joystick straight down - spin carousel left
 */

//@TeleOp(name="Controller", group="Linear Opmode") // @Autonomous(...) is the other common choice
//@Disabled
public class Controller extends LinearOpMode implements Runnable {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private Arm arm = null;
    private Arm.level level = Arm.level.LEVEL_0;
    private boolean opmodeactive = false;
    @Override
    public void runOpMode() {
        opmodeactive = true;
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Motion motion = new Motion(hardwareMap, telemetry, false);
        Intake intake = new Intake(hardwareMap, telemetry);
        arm = new Arm(hardwareMap, telemetry, true);
        Crsl crsl = new Crsl(hardwareMap, telemetry);
        Button button_a = new Button(telemetry, Button.ButtonType.BUTTON_A, gamepad1);
        Button button_b = new Button(telemetry, Button.ButtonType.BUTTON_B, gamepad1);
        Button button_x = new Button(telemetry, Button.ButtonType.BUTTON_X, gamepad1);
        Button button_y = new Button(telemetry, Button.ButtonType.BUTTON_Y, gamepad1);
        Button leftbumper = new Button(telemetry, Button.ButtonType.LEFTBUMPER, gamepad1);
        Button rightbumper = new Button(telemetry, Button.ButtonType.RIGHTBUMPER, gamepad1);
        Button joystickpush_l = new Button(telemetry, Button.ButtonType.L_STICK_BUTTON, gamepad1);
        Button joystickpush_r = new Button(telemetry, Button.ButtonType.R_STICK_BUTTON, gamepad1);

        // Remove me
        //arm.showTelemetery();


        Thread t = new Thread(this, "arm thread");
        t.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Run Time: " + runtime.toString());

            //robot movement

            if (gamepad2.left_stick_y < 0) {
                motion.backward(2000, 0);
            } else if (gamepad2.left_stick_y > 0) {
                motion.forward(2000, 0);
            } else if (gamepad2.right_stick_x > 0) {
                motion.spinright(2000, 0);
            } else if (gamepad2.right_stick_x < 0) {
                motion.spinleft(2000, 0);
            } else {
                motion.stop();
            }

            // arm movement to assigned level (0,1,2,3)

            if (gamepad1.a == true) {
                level = Arm.level.LEVEL_0;
                //RobotLog.d("Button click A");
            } else if (gamepad1.b == true) {
                level = Arm.level.LEVEL_1;
                //RobotLog.d("Button click B");
            } else if (gamepad1.x == true) {
                level = Arm.level.LEVEL_2;
                //RobotLog.d("Button click X");
            } else if (gamepad1.y == true) {
                level = Arm.level.LEVEL_3;
                //RobotLog.d("Button click Y");
            } else if (gamepad1.left_bumper == true) {
                level = Arm.level.SSH;
            }

            // intake + outtake

            if (gamepad2.left_bumper == true) {
                intake.runbackward(1000, 0);
            } else if (gamepad2.right_bumper == true) {
                intake.runforward(1000, 0);
            } else {
                intake.stop();
            }


            // carousel

            if (gamepad1.right_stick_button == true) {
                crsl.runbackward(1800, 0);
                RobotLog.d("right stick button");
            } else if (gamepad1.left_stick_button == true) {
                crsl.runforward(1800, 0);
                RobotLog.d("right stick button");
            } else {
                crsl.stop();
            }
        }

        opmodeactive = false;
    }

    @Override
    public void run() {
        RobotLog.d("inside thread");
        while(opmodeactive == true) {
            arm.MoveToLevel(level);
        }
    }
}