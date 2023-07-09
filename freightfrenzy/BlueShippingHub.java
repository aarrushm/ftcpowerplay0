package org.firstinspires.ftc.teamcode.freightfrenzy;
// import lines were omitted. OnBotJava will add them automatically.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vuforia.VuforiaFTC;

import java.util.Random;

//@Autonomous(name="BlueWarehouse", group="Autonomous") // @Autonomous(...) is the other common choice
// @Disabled
public class BlueShippingHub extends LinearOpMode {

/*
Detect object level
Move arm to that respective level
Go forward
Turn towards alliance shipping hub
Drop block in the hub
Turn towards warehouse
Go into warehouse
 */

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        /* Instantiate Motion class. */
        Motion motion = new Motion(hardwareMap, telemetry, true);
        Intake intake = new Intake(hardwareMap, telemetry);
        Arm arm = new Arm(hardwareMap, telemetry,true);
        VuforiaFTC vf = new VuforiaFTC(hardwareMap, telemetry);

        //Color_Sensor color_sensor = new Color_Sensor(hardwareMap, telemetry);

        Random rand = new Random();


        //move arm to level 0 when robot starts
        arm.MoveToLevel(Arm.level.LEVEL_0);

        waitForStart();
        while (opModeIsActive()) {

            VuforiaFTC.barcode_level BarcodeLevel = vf.BarcodeLevel();

            if (BarcodeLevel == VuforiaFTC.barcode_level.BARCODE_LEVEL_1){
                motion.forward(3);
                motion.spinright(6);
                arm.MoveToLevel(Arm.level.LEVEL_1);
                motion.forward(29);
                intake.runbackward(3000,2000);
                motion.backward(10);
                motion.spinleft(22);
                motion.powerforward(70, 0.8);
                arm.MoveToLevel(Arm.level.LEVEL_0);
            } else if (BarcodeLevel == VuforiaFTC.barcode_level.BARCODE_LEVEL_2){
                motion.forward(3);
                motion.spinright(6);
                arm.MoveToLevel(Arm.level.LEVEL_2);
                motion.forward(29);
                intake.runbackward(3000,2000);
                motion.backward(10);
                arm.MoveToLevel(Arm.level.LEVEL_1);
                motion.spinleft(22);
                motion.powerforward(70, 0.8);
                arm.MoveToLevel(Arm.level.LEVEL_0);
            } else {
                motion.forward(3);
                motion.spinright(6);
                arm.MoveToLevel(Arm.level.LEVEL_3);
                motion.forward(30);
                intake.runbackward(3000,2000);
                motion.backward(10);
                arm.MoveToLevel(Arm.level.LEVEL_1);
                motion.spinleft(22);
                motion.powerforward(70, 0.8);
                arm.MoveToLevel(Arm.level.LEVEL_0);
            }

            break;

        }
    }
}