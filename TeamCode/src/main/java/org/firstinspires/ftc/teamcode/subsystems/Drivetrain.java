package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * Drivetrain subsystem.
 */
public class Drivetrain extends SubsystemBase {
    /**
     * A data class that stores 4 doubles corresponding to each motor.
     */
    public static class DriveState {
        public final double flEncoding;
        public final double frEncoding;
        public final double blEncoding;
        public final double brEncoding;
        public final double flPower;
        public final double frPower;
        public final double blPower;
        public final double brPower;
        public DriveState(
                double flEncoding, double frEncoding, double blEncoding, double brEncoding,
                double flPower, double frPower, double blPower, double brPower
        ) {
            this.flEncoding = flEncoding;
            this.frEncoding = frEncoding;
            this.blEncoding = blEncoding;
            this.brEncoding = brEncoding;
            this.flPower = flPower;
            this.frPower = frPower;
            this.blPower = blPower;
            this.brPower = brPower;
        }
    }


    private static Drivetrain instance = null;

    /**
     * Returns the instance of the drivetrain subsystem,
     * use this instead of constructing an instance.
     * @return the drivetrain instance.
     */
    public static synchronized Drivetrain getInstance() {
        if (instance == null) instance = new Drivetrain();
        return instance;
    }

    private final DcMotorEx motorFL;
    private final DcMotorEx motorFR;
    private final DcMotorEx motorBL;
    private final DcMotorEx motorBR;
    private final boolean disabled = !Constants.SubsystemToggle.drivetrain;

    private Drivetrain() {
        motorFL = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "fl");
        motorFR = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "fr");
        motorBL = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "bl");
        motorBR = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "br");
        if (disabled) return;
        resetZeroPowerBehavior();
    }

    /**
     * Sets the drivetrain motors to brake or to just float based on
     * Constants.AutoConstants.neutralDrivetrain.
     */
    public void resetZeroPowerBehavior() {
        if (disabled) return;
        if (Constants.AutoConstants.neutralDrivetrain) {
            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } else {
            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    /**
     * Drives the robot using robot centric.
     * @param x positive moves it right, negative moves it left
     * @param y positive moves it forwards, negative moves it backwards
     * @param rot positive spins it clockwise, negative spins it counter-clockwise
     */
    public void driveRobotCentric(double x, double y, double rot) {
        if (disabled) return;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
        motorFL.setPower((y + x + rot) / denominator);
        motorFR.setPower((-y + x + rot) / denominator);
        motorBL.setPower((y - x + rot) / denominator);
        motorBR.setPower((y + x - rot) / denominator);
    }

     /**
     * Drives the robot using field centric
     * @param x positive moves it right, negative moves it left
     * @param y positive moves it forwards, negative moves it backwards
     * @param rot positive spins it clockwise, negative spins it counter-clockwise
     * @param heading the absolute heading; origin is set by headingOrigin
     */
    public void driveFieldCentric(double x, double y, double rot, double heading) {
        if (disabled) return;
        /*
            [ cos   -sin ] [ x ] = [ x cos - y sin ]
            [ sin    cos ] [ y ] = [ x sin + y cos ]
         */
        double angle = -heading; // flipped bc clockwise
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double robotX = x * cos - y * sin;
        double robotY = x * sin + y * cos;
        driveRobotCentric(robotX, robotY, rot);
    }

    /**
     * Drives the robot using field centric, but with each input bounded by the constant,
     * and the field centric is relative to the gyroscope zero.
     * @param x positive moves it right, negative moves it left
     * @param y positive moves it forwards, negative moves it backwards
     * @param rot positive spins it clockwise, negative spins it counter-clockwise
     * @param heading the where the robot thinks it's heading
     */
    public void driveFieldAuto(double x, double y, double rot, double heading) {
        if (disabled) return;
        double bound = Constants.AutoConstants.driveMax;
        double boundedX = Math.max(Math.min(x, bound), -bound);
        double boundedY = Math.max(Math.min(y, bound), -bound);
        double boundedRot = Math.max(Math.min(rot, bound), -bound);
        driveFieldCentric(boundedX, boundedY, boundedRot, heading);
    }

    /**
     * Halts every motor.
     */
    public void brake() {
        if (disabled) return;
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }

    public void setFLPower(double power) {
        if (disabled) return;
        motorFL.setPower(power);
    }

    public void setFRPower(double power) {
        if (disabled) return;
        motorFR.setPower(power);
    }

    public void setBLPower(double power) {
        if (disabled) return;
        motorBL.setPower(power);
    }

    public void setBRPower(double power) {
        if (disabled) return;
        motorBR.setPower(power);
    }

    /**
     * Returns the current drive state with each motor's position.
     * @return the drive state.
     */
    public DriveState getDriveState() {
        if (disabled) return new DriveState(0, 0, 0, 0, 0, 0, 0, 0);
        return new DriveState(
                motorFL.getCurrentPosition(),
                motorFR.getCurrentPosition(),
                motorBL.getCurrentPosition(),
                motorBR.getCurrentPosition(),
                motorFL.getPower(),
                motorFR.getPower(),
                motorBL.getPower(),
                motorBR.getPower()
        );
    }
}