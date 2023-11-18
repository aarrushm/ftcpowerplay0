package org.firstinspires.ftc.teamcode.centerstage24.COMP;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name="red right", group="Autonomous")
public class redright2 extends LinearOpMode {
    @Override
    public void runOpMode() {

        TfodJava tj = new TfodJava(hardwareMap, telemetry);

        intake intake = new intake(hardwareMap, telemetry);
        Slide transfer = new Slide(hardwareMap, telemetry, "transfer");
        Slide slide = new Slide(hardwareMap, telemetry, "slide");
        Servos outtake = new Servos(hardwareMap,telemetry, false);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);


        slide.MoveToLevel(Slide.level.LEVEL_0);
        transfer.MoveToLevel(Slide.level.LEVEL_0);
        outtake.forwardMAX();
        //VuforiaFTC.barcode_level BarcodeLevel = vf.BarcodeLevel();

        Pose2d startPose = new Pose2d(-12, 72, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        Trajectory line1 = drive.trajectoryBuilder(startPose)
                .lineTo(new Vector2d(-12, 51), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory middledropline = drive.trajectoryBuilder(line1.end())
                .lineTo(new Vector2d(-12, 44), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory reverse = drive.trajectoryBuilder(line1.end())
                .lineTo(new Vector2d(-12, 76), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory strafe = drive.trajectoryBuilder(line1.end())
                .strafeTo(new Vector2d(-60, 65), SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();


        waitForStart();

        while (opModeIsActive()) {

            transfer.MoveToLevel(Slide.level.AUTO_2);
            //drive to try to detect object at middle level (L2)
            drive.followTrajectory(line1);
            TfodJava.object_pos Detected = tj.ObjectPos();
            //if middle is detected, continue
            if (Detected == TfodJava.object_pos.POS_FOUND){
                drive.followTrajectory(middledropline);
                slide.MoveToLevel(Slide.level.LEVEL_1);
                sleep(100);
                transfer.MoveToLevel(Slide.level.LEVEL_1_DC);
                sleep(500);
                transfer.MoveToLevel(Slide.level.AUTO_2);
                slide.MoveToLevel(Slide.level.LEVEL_0);
                drive.followTrajectory(reverse);
                drive.followTrajectory(strafe);
                transfer.MoveToLevel(Slide.level.LEVEL_0);
            } else {
                //clearly middle not detected, so 45 degree turn towards left side
                drive.turn(Math.toRadians(-55));
                TfodJava.object_pos Detected1 = tj.ObjectPos();
                if (Detected1 == TfodJava.object_pos.POS_FOUND){
                    drive.turn(Math.toRadians(55));
                    drive.followTrajectory(middledropline);
                    drive.turn(Math.toRadians(-80));
                    slide.MoveToLevel(Slide.level.LEVEL_1);
                    sleep(100);
                    transfer.MoveToLevel(Slide.level.LEVEL_1_DC);
                    sleep(500);
                    transfer.MoveToLevel(Slide.level.AUTO_2);
                    slide.MoveToLevel(Slide.level.LEVEL_0);
                    drive.turn(Math.toRadians(80));
                    drive.followTrajectory(reverse);
                    drive.followTrajectory(strafe);
                    transfer.MoveToLevel(Slide.level.LEVEL_0);
                } else {
                    //clearly middle or left not middle detected, so its right, so go to startpose
                    drive.turn(Math.toRadians(55));
                    drive.followTrajectory(middledropline);
                    drive.turn(Math.toRadians(80));
                    slide.MoveToLevel(Slide.level.LEVEL_1);
                    sleep(100);
                    transfer.MoveToLevel(Slide.level.LEVEL_1_DC);
                    sleep(500);
                    transfer.MoveToLevel(Slide.level.AUTO_2);
                    slide.MoveToLevel(Slide.level.LEVEL_0);
                    drive.turn(Math.toRadians(-80));
                    drive.followTrajectory(reverse);
                    drive.followTrajectory(strafe);
                    transfer.MoveToLevel(Slide.level.LEVEL_0);
                }

                break ;

            }

            break;
        }
    }
}