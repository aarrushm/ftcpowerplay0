package org.firstinspires.ftc.teamcode.freightfrenzy;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;

public class Motion {

    /* Declare OpMode members. */
    //private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx right_back = null;
    private DcMotorEx left_back = null;
    private DcMotorEx right_front = null;
    private DcMotorEx left_front = null;
    private Telemetry telemetry = null;

    //Convert from the counts per revolution of the encoder to counts per inch
    static final double HD_COUNTS_PER_REV = 28;
    static final double DRIVE_GEAR_REDUCTION = 20.15293;
    static final double WHEEL_CIRCUMFERENCE_MM = 90 * Math.PI;
    static final double DRIVE_COUNTS_PER_MM = (HD_COUNTS_PER_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_MM;
    static final double DRIVE_COUNTS_PER_IN = DRIVE_COUNTS_PER_MM * 25.4;
    private double defaultpower = 0.5;
    public Motion(HardwareMap hardwareMap, Telemetry t, boolean runtoposition) {

        /* Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        left_back = hardwareMap.get(DcMotorEx.class, "left");
        right_back = hardwareMap.get(DcMotorEx.class, "right");
        left_front = hardwareMap.get(DcMotorEx.class, "left");
        right_front = hardwareMap.get(DcMotorEx.class, "right");
        left_back.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        left_front.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        right_back.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        if (runtoposition) {
            setTargetPosition(0);
            setModeResetEncoder();
            setMotorPower(defaultpower);

        }

        telemetry = t;
    }

    private void setModeRunToPosition() {
        right_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void setModeResetEncoder() {
        right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    private void setVelocity(double motorVelocity) {
        right_front.setVelocity(motorVelocity);
        left_front.setVelocity(motorVelocity);
        right_back.setVelocity(motorVelocity);
        left_back.setVelocity(motorVelocity);
    }

    private void setTargetPosition(int targetPosition) {
        right_back.setTargetPosition(targetPosition);
        left_back.setTargetPosition(targetPosition);
        right_front.setTargetPosition(targetPosition);
        left_front.setTargetPosition(targetPosition);
    }

    private void setMotorPower (double power) {
        right_front.setPower(power);
        left_front.setPower(power);
        right_back.setPower(power);
        left_back.setPower(power);
    }

    public void spinright(double motorVelocity, int timeMs) {

        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        right_back.setDirection(DcMotor.Direction.FORWARD);
        //velocity input
        setVelocity(motorVelocity);
        //time input
        if (timeMs > 0) {
            try {
                sleep(timeMs);
            } catch (InterruptedException e) {
            }
            stop();
        }

    }

    //using encoders
    public void spinright(int inches) {
        int targetPosition = (int) (inches*DRIVE_COUNTS_PER_IN);
        setTargetPosition(0);
        setModeResetEncoder();
        //setModeRunToPosition();
        //left (inverted)
        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        right_back.setDirection(DcMotor.Direction.FORWARD);
        //velocity input
        setTargetPosition(targetPosition);
        setModeRunToPosition();
        while (targetPosition >= right_front.getCurrentPosition()) ;
        while (targetPosition >= right_back.getCurrentPosition()) ;
    }

    public void spinleft(double motorVelocity, int timeMs) {

        left_front.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.REVERSE);
        //velocity input
        setVelocity(motorVelocity);
        //time input
        if (timeMs > 0) {
            try {
                sleep(timeMs);
            } catch (InterruptedException e) {
            }
            stop();
        }
    }

    //using encoders
    public void spinleft(int inches) {
        int targetPosition = (int) (inches*DRIVE_COUNTS_PER_IN);
        setTargetPosition(0);
        setModeResetEncoder();
        //setModeRunToPosition();
        //left (inverted)
        left_front.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.REVERSE);
        //velocity input
        setTargetPosition(targetPosition);
        setModeRunToPosition();
        while (targetPosition >= right_front.getCurrentPosition()) ;
        //while (targetPosition >= right_back.getCurrentPosition()) ;
    }

    public void forward(double motorVelocity, int timeMs) {

        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.FORWARD);
        //velocity input
        setVelocity(motorVelocity);
        //time input
        if (timeMs > 0) {
            try {
                sleep(timeMs);
            } catch (InterruptedException e) {
            }
            stop();
        }
    }

    //using encoders
    public void forward(int inches) {
        int targetPosition = (int) (inches*DRIVE_COUNTS_PER_IN);
        setTargetPosition(0);
        setModeResetEncoder();
        //setModeRunToPosition();
        //left (inverted)
        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.FORWARD);
        //velocity input
        setTargetPosition(targetPosition);
        setModeRunToPosition();
        while (targetPosition >= right_front.getCurrentPosition()) ;
        //while (targetPosition >= right_back.getCurrentPosition()) ;
    }

    //using encoders
    public void powerforward (int inches, double power) {
        int targetPosition = (int) (inches*DRIVE_COUNTS_PER_IN);
        setMotorPower(power);
        setTargetPosition(0);
        setModeResetEncoder();
        //setModeRunToPosition();
        //left (inverted)
        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.FORWARD);
        //velocity input
        setTargetPosition(targetPosition);
        setModeRunToPosition();
        while (targetPosition >= right_front.getCurrentPosition()) ;
        //while (targetPosition >= right_back.getCurrentPosition()) ;
        setMotorPower(defaultpower);
    }

    public void backward(double motorVelocity, int timeMs) {

        left_front.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        right_back.setDirection(DcMotor.Direction.REVERSE);
        setVelocity(motorVelocity);
        //time input
        if (timeMs > 0) {
            try {
                sleep(timeMs);
            } catch (InterruptedException e) {
            }
            stop();
        }
    }

    //using encoders
    public void backward(int inches) {
        int targetPosition = (int) (inches*DRIVE_COUNTS_PER_IN);
        setTargetPosition(0);
        setModeResetEncoder();
        //setModeRunToPosition();
        //left (inverted)
        left_front.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        right_back.setDirection(DcMotor.Direction.REVERSE);
        //velocity input
        setTargetPosition(targetPosition);
        setModeRunToPosition();
        while (targetPosition >= left_front.getCurrentPosition()) ;
        //while (targetPosition >= right_back.getCurrentPosition()) ;
    }

    public void stop() {

        setVelocity(0);

    }

    public void showTelemetery() {
        telemetry.addData("Encoder value - back right", right_back.getCurrentPosition());
        telemetry.addData("Encoder value - back left", left_back.getCurrentPosition());
        telemetry.addData("Encoder value - front right", right_front.getCurrentPosition());
        telemetry.addData("Encoder value - front left", left_front.getCurrentPosition());
        telemetry.update();
    }
}