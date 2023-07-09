package org.firstinspires.ftc.teamcode.mecanum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.advanced.PoseStorage;

/**
 * This opmode demonstrates how one can augment driver control by following Road Runner arbitrary
 * Road Runner trajectories at any time during teleop. This really isn't recommended at all. This is
 * not what Trajectories are meant for. A path follower is more suited for this scenario. This
 * sample primarily serves as a demo showcasing Road Runner's capabilities.
 * <p>
 * This bot starts in driver controlled mode by default. The player is able to drive the bot around
 * like any teleop opmode. However, if one of the select buttons are pressed, the bot will switch
 * to automatic control and run to specified location on its own.
 * <p>
 * If A is pressed, the bot will generate a splineTo() trajectory on the fly and follow it to
 * targetA (x: 45, y: 45, heading: 90deg).
 * <p>
 * If B is pressed, the bot will generate a lineTo() trajectory on the fly and follow it to
 * targetB (x: -15, y: 25, heading: whatever the heading is when you press B).
 * <p>
 * If Y is pressed, the bot will turn to face 45 degrees, no matter its position on the field.
 * <p>
 * Pressing X will cancel trajectory following and switch control to the driver. The bot will also
 * cede control to the driver once trajectory following is done.
 * <p>
 * The following may be a little off with this method as the trajectory follower and turn
 * function assume the bot starts at rest.
 * <p>
 * This sample utilizes the SampleMecanumDriveCancelable.java and TrajectorySequenceRunnerCancelable.java
 * classes. Please ensure that these files are copied into your own project.
 */
@Disabled
@TeleOp(name="DRIVER_CONTROL V2", group="Linear Opmode") // @Autonomous(...) is the other common choice
public class DRIVER_CONTROL_V2 extends LinearOpMode /*implements Runnable*/ {

    // Define 2 states, drive control or automatic control
    enum Mode {
        DRIVER_CONTROL,
        AUTOMATIC_CONTROL
    }

    Mode currentMode = Mode.DRIVER_CONTROL;
    Pose2d currentPoseTemp = new Pose2d();

    double velocity = 0.5;

    // The coordinates we want the bot to automatically go to when we press the A button
    Vector2d targetAVector = new Vector2d(-14.8, -60.9);
    // The heading we want the bot to end on for targetA
    double targetAHeading = Math.toRadians(-90);

    // The location we want the bot to automatically go to when we press the B button
    Vector2d targetBVector = new Vector2d(24, 22);

    // The angle we want to align to when we press Y
    double targetAngle = Math.toRadians(0);

    private Slide rightslide = null;
    private Slide.level level = Slide.level.LEVEL_0_DC;
    private Slide leftslide = null;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize custom cancelable SampleMecanumDrive class
        // Ensure that the contents are copied over from https://github.com/NoahBres/road-runner-quickstart/blob/advanced-examples/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/drive/advanced/SampleMecanumDriveCancelable.java
        // and https://github.com/NoahBres/road-runner-quickstart/blob/advanced-examples/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/drive/advanced/TrajectorySequenceRunnerCancelable.java
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // We want to turn off velocity control for teleop
        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Retrieve our pose from the PoseStorage.currentPose static field
        // See AutoTransferPose.java for further details
        //

        drive.setPoseEstimate(PoseStorage.currentPose);

        waitForStart();

        Servos intake = new Servos(hardwareMap, telemetry, false);
        rightslide = new Slide(hardwareMap, telemetry, "rightslide");
        leftslide = new Slide(hardwareMap, telemetry, "leftslide");

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            // Update the drive class
            drive.update();

            // Read pose
            Pose2d poseEstimate = drive.getPoseEstimate();

            // Print pose to telemetry
            telemetry.addData("mode", currentMode);
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();

            // We follow different logic based on whether we are in manual driver control or switch
            // control to the automatic mode
            switch (currentMode) {
                case DRIVER_CONTROL:
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    -gamepad1.left_stick_y*velocity,
                                    -gamepad1.left_stick_x*velocity,
                                    -gamepad1.right_stick_x*velocity
                            )
                    );

                    if (gamepad1.left_trigger >= 0.8) {
                        velocity = 0.2;
                    } else if (gamepad1.right_trigger >= 0.8) {
                        velocity = 1.5;
                    } else{
                        velocity = 0.5;
                    }

                    if (gamepad2.right_bumper == true) {
                        intake.forwardMAX();
                    } else if (gamepad2.left_bumper == true) {
                        intake.reverseMAX();
                    } else{
                        intake.holdPosition();
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

                        level = Slide.level.LEVEL_3_DC;
                        rightslide.MoveToLevelAsync(level);
                        leftslide.MoveToLevelAsync(level);
                        //RobotLog.d("Button click Y");
                    }

                    if (gamepad1.left_bumper) {
                        // If the A button is pressed on gamepad1, we generate a splineTo()
                        // trajectory on the fly and follow it
                        // We switch the state to AUTOMATIC_CONTROL

                        Trajectory traj1 = drive.trajectoryBuilder(poseEstimate)
                                .splineTo(targetAVector, targetAHeading)
                                .build();

                        drive.followTrajectoryAsync(traj1);

                        currentMode = Mode.AUTOMATIC_CONTROL;
                    } /*else if (gamepad1.right_bumper) {
                        // If the B button is pressed on gamepad1, we generate a lineTo()
                        // trajectory on the fly and follow it
                        // We switch the state to AUTOMATIC_CONTROL

                        Trajectory traj1 = drive.trajectoryBuilder(poseEstimate)
                                .lineTo(targetBVector)
                                .build();

                        drive.followTrajectoryAsync(traj1);

                        currentMode = Mode.AUTOMATIC_CONTROL;
                    } *//*else if (gamepad2.x) {
                        // If Y is pressed, we turn the bot to the specified angle to reach
                        // targetAngle (by default, 45 degrees)

                        drive.turnAsync(Angle.normDelta(targetAngle - poseEstimate.getHeading()));

                        currentMode = Mode.AUTOMATIC_CONTROL;
                    }
                    break;*/


                case AUTOMATIC_CONTROL:
                    // If x is pressed, we break out of the automatic following
                    if (gamepad2.x) {
                       // drive.breakFollowing();
                        currentMode = Mode.DRIVER_CONTROL;
                    }

                    // If drive finishes its task, cede control to the driver
                    if (!drive.isBusy()) {
                        currentMode = Mode.DRIVER_CONTROL;
                    }
                    break;
            }
        }
    }
}