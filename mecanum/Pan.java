package org.firstinspires.ftc.teamcode.mecanum;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Pan extends Motor1 implements Runnable {
    static final int CENTER_TP = 0;
    static final int RIGHT_TP = 2435;
    static final int LEFT_TP = -2435;


    enum level{

        CENTER,
        RIGHT,
        LEFT
    }

    boolean do_async = false;
    boolean exit = false;
    Pan.level targetLevel = level.CENTER;

    @Override
    public void run() {
        while (exit == false) {
            if (do_async) {
                Move(targetLevel);
                do_async = false;
            }

        }
    }

    public Pan(HardwareMap hardwareMap, Telemetry t) {
        super(hardwareMap, t);
        motor = hardwareMap.get(DcMotorEx.class, "leftslide");
        name = "leftslide";
        super.setTargetPosition(0);
        setModeResetEncoder();
        setModeRunToPosition();
        Thread te = new Thread(this, "leftslide");
        te.start();
    }

    public void terminate () {exit = true;}
    public boolean Move(Pan.level e){
        if (e == Pan.level.LEFT) {
            runbackward(LEFT_TP);
            //RobotLog.d("MoveToLevel 3");
        } else if (e == Pan.level.RIGHT) {
            runbackward(RIGHT_TP);
            //RobotLog.d("MoveToLevel 1");
        } else {
            runbackward(CENTER_TP);
            //RobotLog.d("MoveToLevel 2");
        }
        return true;
    }
    public boolean MoveAsync (Pan.level l) {
        targetLevel = l;
        do_async = true;
        return true;
    }
}
