package org.firstinspires.ftc.teamcode.pid;

/**
 * Data class that stores PID values.
 */
public class PIDData {
    public final double kP;
    public final double kI;
    public final double kD;

    public PIDData(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
}
