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
    public static double intakePower = 1;
    public static double indexerPower = 0.2;

    public static class SubsystemToggle {
        public static boolean drivetrain = true;
        public static boolean indexer = true;
        public static boolean intake = true;
        public static boolean intakeServos = true;
        public static boolean shooter = true;
    }

    public static class AutoConstants {
        public final static double freeInchesPerTickFL = 0.0227344490054;
        public final static double deadInchesPerTickFL = 0.0338107536981;
        public final static double freeInchesPerTickFR = 0.0237388724036;
        public final static double deadInchesPerTickFR = 0.0260964117434;
        public final static double freeInchesPerTickBL = 0.0230215827338;
        public final static double deadInchesPerTickBL = 0.0247465200206;
        public final static double freeInchesPerTickBR = 0.0227380388441;
        public final static double deadInchesPerTickBR = 0.0235140431091;
        public final static double localizeYScale = 0.852045797462;
        public final static double localizeXScale = 0.868071254182;
        public static boolean neutralDrivetrain = false;
        public final static double driveMax = 0.7;

        public static PIDData xyPID = new PIDData(1, 0, 0);
        public static PIDData headingPID = new PIDData(0.6, 0, 0);

        public static PIDData shooterPID = new PIDData(0.005, 0.00001, 0.0000
        );
    }
}