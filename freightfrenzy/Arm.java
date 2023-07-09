package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm extends Motor {


    static final int LEVEL_0_TP = 290;
    static final int LEVEL_1_TP = 1781;
    static final int LEVEL_2_TP = 2489;
    static final int LEVEL_3_TP = 3223;
    static final int SSH = 0;
    enum level{

        LEVEL_0,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        SSH
    }

    private ElapsedTime time = new ElapsedTime();
    public Arm(HardwareMap hardwareMap, Telemetry t, boolean runtoposition){
        super(hardwareMap,t);
        motor = hardwareMap.get(DcMotorEx.class, "arm");
        if (runtoposition == true){
            super.setTargetPosition(0);
            setModeResetEncoder();
            setModeRunToPosition();
            time.reset();
        }

    }

    public boolean MoveToLevel(level l){
        if (l == level.LEVEL_0) {
            runbackward(LEVEL_0_TP);
            //RobotLog.d("MoveToLevel 0");
        } else if (l == level.LEVEL_1) {
            runbackward(LEVEL_1_TP);
            //RobotLog.d("MoveToLevel 1");
        } else if (l == level.LEVEL_2) {
            runbackward(LEVEL_2_TP);
            //RobotLog.d("MoveToLevel 2");
        } else if (l == level.SSH) {
            runbackward(SSH);
            //RobotLog.d("MoveToLevel 2");
        } else {
            runbackward(LEVEL_3_TP);
            //RobotLog.d("MoveToLevel 3");
        }
    return true;
    }
}