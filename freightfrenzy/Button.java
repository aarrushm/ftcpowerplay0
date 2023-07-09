package org.firstinspires.ftc.teamcode.freightfrenzy;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
     * Manages toggling an action.
     *
     * Call checkToggleStatus once every loop to determine whether a full button press has
     * occurred or not.
 */
    public class Button
    {
        public enum ButtonType{
            BUTTON_A,
            BUTTON_B,
            BUTTON_X,
            BUTTON_Y,
            LEFTBUMPER,
            RIGHTBUMPER,
            L_STICK_BUTTON,
            R_STICK_BUTTON
        }

        private boolean buttonPreviousState = false;
        private Telemetry telemetry = null;
        private Button.ButtonType type = null;
        private Gamepad pad = null;

        public Button(Telemetry t, ButtonType b, Gamepad g) {
            telemetry = t;
            type = b;
            pad = g;
        }
        public boolean buttonClick () {
            boolean returnVal = false;
            boolean buttonCurState = false;

            if (type == ButtonType.LEFTBUMPER){
                buttonCurState = pad.left_bumper;
            } else if (type == ButtonType.RIGHTBUMPER){
                buttonCurState = pad.right_bumper;
            } else if (type == ButtonType.L_STICK_BUTTON){
                buttonCurState = pad.left_stick_button;
            } else if (type == ButtonType.R_STICK_BUTTON){
                buttonCurState = pad.right_stick_button;
            } else if (type == ButtonType.BUTTON_A){
                buttonCurState = pad.a;
            } else if (type == ButtonType.BUTTON_B){
                buttonCurState = pad.b;
            } else if (type == ButtonType.BUTTON_X){
                buttonCurState = pad.x;
            } else if (type == ButtonType.BUTTON_Y){
                buttonCurState = pad.y;
            }
            returnVal = buttonCurState && !buttonPreviousState;
            buttonPreviousState = buttonCurState;
            return returnVal;
        }
    }

