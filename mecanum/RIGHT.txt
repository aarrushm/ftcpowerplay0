package org.firstinspires.ftc.teamcode.mecanum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//@Autonomous(name="RIGHT", group="Autonomous")
public class RIGHT extends LinearOpMode {
    @Override
    public void runOpMode() {

        VuforiaFTC vf = new VuforiaFTC(hardwareMap, telemetry);
        Slide rightslide = new Slide(hardwareMap, telemetry,"rightslide");
        Slide leftslide = new Slide(hardwareMap, telemetry,"leftslide");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Servos intake = new Servos(hardwareMap, telemetry, false);

        rightslide.MoveToLevel(Slide.level.LEVEL_0);
        leftslide.MoveToLevel(Slide.level.LEVEL_0);
        intake.forwardMAX();
        VuforiaFTC.barcode_level BarcodeLevel = vf.BarcodeLevel();


        waitForStart();
        while (opModeIsActive()) {

            drive.setPoseEstimate(new Pose2d());
            Trajectory Pole1 = drive.trajectoryBuilder(new Pose2d(),false)
                    /*.forward(35,SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))*/
                    .lineToLinearHeading(new Pose2d(15, -37, Math.toRadians(0)),SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Pole1b = drive.trajectoryBuilder(new Pose2d(),false)
                    .forward(22.7+15,SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            //Trajectory traj1 = drive.trajectoryBuilder(new Pose2d().plus(new Pose2d(0, 0, Math.toRadians(-90))), true)
            Trajectory Spline = drive.trajectoryBuilder(new Pose2d())
                    .splineTo(new Vector2d(18.5, -31), Math.toRadians(-90), SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Allign = drive.trajectoryBuilder(new Pose2d(),false)
                    .back(1,SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Reverse = drive.trajectoryBuilder(new Pose2d(),false)
                    .back(28,SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Reverse1 = drive.trajectoryBuilder(new Pose2d(),false)
                    .back(16,SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Pole2 = drive.trajectoryBuilder(new Pose2d(),false)
                    .forward(12,SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Pole2Reverse = drive.trajectoryBuilder(new Pose2d(),false)
                    .back(12,SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory Forward = drive.trajectoryBuilder(new Pose2d(),false)
                    .forward(25,SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();


            rightslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            drive.followTrajectory(Pole1);
            drive.setPoseEstimate(new Pose2d());
            drive.followTrajectory(Pole1b);
            drive.setPoseEstimate(new Pose2d());
            drive.followTrajectory(Reverse1);
            sleep(1000);
            intake.reverseMAX();
            sleep(1000);
            rightslide.MoveToLevelAsync(Slide.level.AUTO_2);
            drive.setPoseEstimate(new Pose2d());
            drive.followTrajectory(Spline);
            drive.setPoseEstimate(new Pose2d());
            drive.followTrajectory(Allign);
            drive.setPoseEstimate(new Pose2d());
            intake.forwardMAX();
            drive.followTrajectory(Allign);
            drive.setPoseEstimate(new Pose2d());
            rightslide.MoveToLevelAsync(Slide.level.LEVEL_1);
            drive.followTrajectory(Reverse);
            drive.setPoseEstimate(new Pose2d());
            drive.turn(Math.toRadians(-50));
            drive.setPoseEstimate(new Pose2d());
            drive.followTrajectory(Pole2);
            drive.setPoseEstimate(new Pose2d());
            intake.reverseMAX();
            sleep(2000);

            if (BarcodeLevel == VuforiaFTC.barcode_level.SLEEVE_1){
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                drive.followTrajectory(Pole2Reverse);
                drive.setPoseEstimate(new Pose2d());
                drive.turn(Math.toRadians(-155));
                drive.setPoseEstimate(new Pose2d());
                drive.followTrajectory(Forward);
            } else if (BarcodeLevel == VuforiaFTC.barcode_level.SLEEVE_2){
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                drive.followTrajectory(Pole2Reverse);
                drive.setPoseEstimate(new Pose2d());
                drive.turn(Math.toRadians(50));
                drive.setPoseEstimate(new Pose2d());
            } else {
                rightslide.MoveToLevelAsync(Slide.level.LEVEL_0);
                drive.followTrajectory(Pole2Reverse);
                drive.setPoseEstimate(new Pose2d());
                drive.turn(Math.toRadians(50));
                drive.setPoseEstimate(new Pose2d());
                drive.followTrajectory(Forward);
            }
            rightslide.terminate();
            break;
        }
    }
}