package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.freightfrenzy.Arm;
import org.firstinspires.ftc.teamcode.freightfrenzy.Crsl;
import org.firstinspires.ftc.teamcode.freightfrenzy.Intake;
import org.firstinspires.ftc.teamcode.freightfrenzy.Motion;
import org.firstinspires.ftc.teamcode.vuforia.VuforiaFTC;

//@Autonomous (name="RedCarousel", group="Autonomus")
public class RedCarousel extends LinearOpMode {

/*
detect level
move arm to respective level
go forward & run over custom object
turn toward the alliance shipping hub
move forward to alliance shipping hub
run intake in reverse to drop the preload box on the respective level
drive backward and wall square for alignment
move forward and turn so back is facing carousel
move backward toward carousel
spin carousel wheel until duck is dropped
reposition robot so that driving forward will get us fully in the parking garage
drive to the parking garage
*/

    @Override
    public void runOpMode() {
        Motion motion = new Motion(hardwareMap, telemetry, true);
        Intake intake = new Intake(hardwareMap, telemetry);
        Arm arm = new Arm(hardwareMap, telemetry, true);
        Crsl crsl = new Crsl(hardwareMap, telemetry);
        VuforiaFTC vf = new VuforiaFTC(hardwareMap, telemetry);

        arm.MoveToLevel(Arm.level.LEVEL_0);

        waitForStart();
        while (opModeIsActive()) {

            VuforiaFTC.barcode_level BarcodeLevel = vf.BarcodeLevel();

            if (BarcodeLevel == VuforiaFTC.barcode_level.BARCODE_LEVEL_3) {
                arm.MoveToLevel(Arm.level.LEVEL_3);
                motion.powerforward(43, 0.8);
                sleep(500);
                motion.spinright(17);
                sleep(500);
                motion.forward(16);
                intake.runbackward(3000, 2000);
                motion.backward(40);
                sleep(500);
                motion.forward(20);
                sleep(500);
                motion.spinleft(8);
                sleep(500);
                motion.backward(42);
                arm.MoveToLevel(Arm.level.LEVEL_0);
                crsl.runforward(2000, 3000);
                motion.spinleft(8);
                motion.forward(30);
            } else if (BarcodeLevel == VuforiaFTC.barcode_level.BARCODE_LEVEL_2) {
                arm.MoveToLevel(Arm.level.LEVEL_2);
                motion.powerforward(43, 0.8);
                sleep(500);
                motion.spinright(16);
                sleep(500);
                motion.forward(20);
                intake.runbackward(3000, 2000);
                motion.backward(40);
                sleep(500);
                motion.forward(20);
                sleep(500);
                motion.spinleft(8);
                sleep(500);
                motion.backward(42);
                arm.MoveToLevel(Arm.level.LEVEL_0);
                crsl.runforward(2000, 3000);
                motion.spinleft(8);
                motion.forward(30);
            } else {
                arm.MoveToLevel(Arm.level.LEVEL_1);
                motion.powerforward(43, 0.8);
                sleep(500);
                motion.spinright(17);
                sleep(500);
                motion.forward(16);
                intake.runbackward(3000, 2000);
                motion.backward(40);
                sleep(500);
                motion.forward(20);
                sleep(500);
                motion.spinleft(8);
                sleep(500);
                motion.backward(42);
                arm.MoveToLevel(Arm.level.LEVEL_0);
                crsl.runforward(2000, 3000);
                motion.spinleft(8);
                motion.forward(30);
            }
            break;
        }
    }
}