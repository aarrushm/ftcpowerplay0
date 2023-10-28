package org.firstinspires.ftc.teamcode.mecanum;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Slide extends Motor1 implements Runnable {
    static final int LEVEL_0_TP_DC = 0;
    static final int LEVEL_0_TP = 0;
    static final int reverse = 0;
    static final int AUTO_2_TP = 70;
    static final int LEVEL_1_TP = 500;
    static final int LEVEL_1_TP_DC = 130;
    static final int LEVEL_2_TP = 1900;
    static final int LEVEL_2_TP_DC = 1900;
    static final int LEVEL_3_TP = 2900;
    static final int LEVEL_3_TP_DC = 2900;



    enum level{

        LEVEL_0_DC,
        LEVEL_1_DC,
        LEVEL_2_DC,
        LEVEL_3_DC,
        LEVEL_0,
        AUTO_2,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        reverse
    }
    boolean do_async = false;
    boolean exit = false;
    level targetLevel = level.LEVEL_0;

    @Override
    public void run() {
        while (exit == false) {
            if (do_async) {
                MoveToLevel(targetLevel);
                do_async = false;
            }

        }
    }

    public Slide(HardwareMap hardwareMap, Telemetry t, String device_name) {
        super(hardwareMap, t);
        motor = hardwareMap.get(DcMotorEx.class, device_name);
        name = device_name;
        super.setTargetPosition(0);
        setModeResetEncoder();
        setModeRunToPosition();
        Thread te = new Thread(this, device_name);
        te.start();
    }
    public void terminate () {exit = true;}
    private void move (int l) {
        if (name == "leftslide"){
            runforward(l);
        } else {
            runbackward(l);
        }
    }
    public boolean MoveToLevel(Slide.level l){
        if (l == Slide.level.LEVEL_3) {
            move(LEVEL_3_TP);
            //RobotLog.d("MoveToLevel 3");
        } else if (l == Slide.level.LEVEL_1) {
        move(LEVEL_1_TP);
            //RobotLog.d("MoveToLevel 1");
        } else if (l == Slide.level.LEVEL_2) {
        move(LEVEL_2_TP);
            //RobotLog.d("MoveToLevel 2");
        } else if (l == level.AUTO_2) {
        move(AUTO_2_TP);
            //RobotLog.d("MoveToAUTO 2");
        } else if (l == level.LEVEL_0_DC) {
        move(LEVEL_0_TP_DC);
            //RobotLog.d("MoveToAUTO 2");
        }
        else if (l == level.LEVEL_1_DC) {
        move(LEVEL_1_TP_DC);
            //RobotLog.d("MoveToAUTO 2");
        } else if (l == level.LEVEL_2_DC) {
        move(LEVEL_2_TP_DC);
            //RobotLog.d("MoveToAUTO 2");
        } else if (l == level.LEVEL_3_DC) {
        move(LEVEL_3_TP_DC);
            //RobotLog.d("MoveToAUTO 2");
        } else if (l == level.reverse) {
        move(reverse);
            //RobotLog.d("MoveToAUTO 2");
        } else {
        move(LEVEL_0_TP);
            //RobotLog.d("MoveToLevel 0");
        }
        return true;
    }

    public boolean MoveToLevelAsync (Slide.level l) {
        targetLevel = l;
        do_async = true;
        return true;
    }

    }
