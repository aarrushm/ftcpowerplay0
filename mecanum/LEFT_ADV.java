package org.firstinspires.ftc.teamcode.mecanum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.advanced.AutoTransferPose;
import org.firstinspires.ftc.teamcode.drive.advanced.PoseStorage;

@Autonomous(name="LEFT_ADV", group="Autonomous")
public class LEFT_ADV extends LinearOpMode {
    @Override
    public void runOpMode() {

        //VuforiaFTC vf = new VuforiaFTC(hardwareMap, telemetry);
        Slide rightslide = new Slide(hardwareMap, telemetry,"rightslide");
        Slide leftslide = new Slide(hardwareMap, telemetry,"leftslide");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Servos intake = new Servos(hardwareMap, telemetry, false);

        rightslide.MoveToLevel(Slide.level.LEVEL_0);
        leftslide.MoveToLevel(Slide.level.LEVEL_0);
        intake.forwardMAX();
        //VuforiaFTC.barcode_level BarcodeLevel = vf.BarcodeLevel();

        Pose2d startPose = new Pose2d(-44, -69.3, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        Trajectory strafe1 = drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(-27.5, -69.3), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory forward1 = drive.trajectoryBuilder(strafe1.end())
                .splineTo(new Vector2d(-27.5, -61.5), (Math.toRadians(90)), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory reverse1 = drive.trajectoryBuilder(forward1.end())
                .lineTo(new Vector2d(-27.5, -69.3), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory strafe2 = drive.trajectoryBuilder(reverse1.end())
                .strafeTo(new Vector2d(-16, -69.3), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory forward2 = drive.trajectoryBuilder(strafe2.end())
                .splineTo(new Vector2d(-16, -17.5), (Math.toRadians(90)), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory forward3 = drive.trajectoryBuilder(forward2.end().plus(new Pose2d(0, 0, Math.toRadians(90))))
                .splineTo(new Vector2d(-68, -17.5), (Math.toRadians(180)), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory wallreverse = drive.trajectoryBuilder(forward3.end())
                .lineTo(new Vector2d(-66, -17.5), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory reverse2 = drive.trajectoryBuilder(wallreverse.end())
                .lineTo(new Vector2d(-35, -17.5), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory strafe3 = drive.trajectoryBuilder(reverse2.end())
                .strafeTo(new Vector2d(-43, -31), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory strafe4 = drive.trajectoryBuilder(strafe3.end())
                .strafeTo(new Vector2d(-35, -17.5), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory forward4 = drive.trajectoryBuilder(strafe4.end())
                .splineTo(new Vector2d(-67, -17.5), (Math.toRadians(180)), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory reverse3 = drive.trajectoryBuilder(forward4.end())
                .lineTo(new Vector2d(-16, -17.5), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        waitForStart();
        while (opModeIsActive()) {

            //move slide
            rightslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            leftslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            //strafe for 1st cone
            drive.followTrajectory(strafe1);
            drive.followTrajectory(forward1);
            //drop 1st cone
            sleep(100);
            intake.reverseMAX();
            sleep(600);
            intake.forwardMAX();
            //wall square
            drive.followTrajectory(reverse1);
            //lower slide
            rightslide.MoveToLevelAsync(Slide.level.AUTO_2);
            leftslide.MoveToLevelAsync(Slide.level.AUTO_2);
            // drive near center
            drive.followTrajectory(strafe2);
            drive.followTrajectory(forward2);
            //turn
            drive.turn(Math.toRadians(90));
            //open intake
            intake.reverseMAX();
            //forward to side cones
            drive.followTrajectory(forward3);
            //pick up cone
            intake.forwardMAX();
            sleep(600);
            //reverse from wall
            drive.followTrajectory(wallreverse);
            rightslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            leftslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            drive.followTrajectory(reverse2);
            //strafe to pole
            drive.followTrajectory(strafe3);
            //drop cone
            sleep(100);
            intake.reverseMAX();
            sleep(600);
            intake.forwardMAX();
            sleep(600);
            rightslide.MoveToLevelAsync(Slide.level.AUTO_2);
            leftslide.MoveToLevelAsync(Slide.level.AUTO_2);
            intake.reverseMAX();
            sleep(600);

/*
            if (BarcodeLevel == VuforiaFTC.barcode_level.SLEEVE_1){

                //ps 2
                drive.followTrajectory(strafe4);
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                leftslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                //ps 1
                drive.followTrajectory(forward4);


            } else if (BarcodeLevel == VuforiaFTC.barcode_level.SLEEVE_2){

                //ps 2
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                leftslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                drive.followTrajectory(strafe4);

            } else {

                //ps 2
                drive.followTrajectory(strafe4);
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                leftslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                //ps 1
                drive.followTrajectory(forward4);
                //ps 3
                drive.followTrajectory(reverse3);

            }
            rightslide.terminate();
            leftslide.terminate();

            // save the current pose
            PoseStorage.currentPose = drive.getPoseEstimate();
            break;

 */
        }
    }
}