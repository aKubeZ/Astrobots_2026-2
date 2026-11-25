package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.Logger;
import org.firstinspires.ftc.teamcode.pid.PIDData;

public class Constants {
    public static HardwareMap hardwareMap = null;
    public static Telemetry telemetry = null;
    public static GamepadEx pilot = null;
    public static GamepadEx copilot = null;
    public static double gyroscopeOffset = Math.PI;
    public static double shooterRPM = 1420; // wha the ffff is a feed
    public static PIDData shooterPID = new PIDData(0.01, 0, 0);
    public static double intakePower = -1;
    public static Logger logger;

    /**
     * adds/remove 2*n*pi until boundStart <= angle < boundStart + 2pi
     * also literally where else am i meant to put this
     */
    public static double boundCyclic(double angle, double boundStart) {
        // im too lazy to find out how to implement this
        // so im lit gonna do exactly what the javadoc comment says lmao
        // i mean this cant be thatt inefficient
        while (angle < boundStart)
            angle += 2 * Math.PI;
        while (angle >= boundStart + 2 * Math.PI)
            angle -= 2 * Math.PI;
        return angle;
    }


    public static class Subsystems {
        public static boolean drivetrain = true;
        public static boolean indexer = false;
        public static boolean intake = true;
        public static boolean verity = true;
        public static boolean shooter = false;
    }

    public static class Verity {
        public static double everything = 0.2;
        public static double cow = 0.6;
    }

    public static class Drive {
        public static boolean neutralDrivetrain = false;
        public final static double driveMax = 0.7;
        public static PIDData xyPID = new PIDData(0.2, 0, 0);
        // a bang bang would be fine for this but im lazy
        public static PIDData headingPID = new PIDData(0.67, 0, 0);
        public static double teleStartHeading = Math.PI / 2;
        public static double turnSpeed = 2;

        public static class Coeffs {
            public static double flY = -1;
            public static double frY = -1;
            public static double blY = 1;
            public static double brY = 1;
            public static double flX = -1;
            public static double frX = 1;
            public static double blX = -1;
            public static double brX = 1;
            public static double flRot = 1;
            public static double frRot = -1;
            public static double blRot = -1;
            public static double brRot = 1;
        }
    }

    public static class Localizer {
        public final static double freeInchesPerTickFL = 0.0227344490054;
        public final static double deadInchesPerTickFL = 0.0338107536981;
        public final static double freeInchesPerTickFR = 0.0237388724036;
        public final static double deadInchesPerTickFR = 0.0260964117434;
        public final static double freeInchesPerTickBL = 0.0230215827338;
        public final static double deadInchesPerTickBL = 0.0247465200206;
        public final static double freeInchesPerTickBR = 0.0227380388441;
        public final static double deadInchesPerTickBR = 0.0235140431091;
        public final static double localizeYScale = 1.23933934177;
        public final static double localizeXScale = 1.40040903082;
    }
}