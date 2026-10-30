package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.Pose;
import org.firstinspires.ftc.teamcode.commands.DataLog;
import org.firstinspires.ftc.teamcode.pid.PIDData;

public class Constants {
    public static HardwareMap hardwareMap = null;
    public static Telemetry telemetry = null;
    public static GamepadEx pilot = null;
    public static GamepadEx copilot = null;
    public static DataLog logger; // putting this here feels wrong
    public static double shooterRPM = 1420;
    public static double indexerPower = 0.2;

    public static class AutoConstants {
        public final static double freeInchesPerTickFL = 0.02352;
        public final static double deadInchesPerTickFL = 0.03597;
        public final static double freeInchesPerTickFR = 0.02261;
        public final static double deadInchesPerTickFR = 0.03151;
        public final static double freeInchesPerTickBL = 0.02302;
        public final static double deadInchesPerTickBL = 0.02418;
        public final static double freeInchesPerTickBR = 0.02284;
        public final static double deadInchesPerTickBR = 0.03403;
        public final static double localizeYScale = 0.970866747868;
        public final static double localizeXScale = 0.923711340208;
        public static boolean neutralDrivetrain = false;
        public final static double driveMax = 0.7;
        public final static Pose robotStartPose = new Pose(0, 0, 0);

        // i have NO clue why this is negative OHH I GET IT NOW
        public final static PIDData xyPID = new PIDData(0.95, 0, 0);
        public final static PIDData headingPID = new PIDData(0.6, 0, 0);

        public final static PIDData shooterPID = new PIDData(0.005, 0.00001, 0.0000
        );
    }
}