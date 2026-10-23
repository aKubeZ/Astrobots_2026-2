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
    private double eastHeading = 0;
    private Drivetrain() {
        motorFL = Constants.hardwareMap.get(DcMotorEx.class, "fl");
        motorFR = Constants.hardwareMap.get(DcMotorEx.class, "fr");
        motorBL = Constants.hardwareMap.get(DcMotorEx.class, "bl");
        motorBR = Constants.hardwareMap.get(DcMotorEx.class, "br");
        resetZeroPowerBehavior();
    }

    /**
     * Sets the drivetrain motors to brake or to just float based on
     * Constants.AutoConstants.neutralDrivetrain.
     */
    public void resetZeroPowerBehavior() {
        if (!Constants.AutoConstants.neutralDrivetrain) {
            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Sets the initial heading of the robot, to make a reference
     * to base the field-centric drive on.
     * @param heading the initial heading
     */
    public void setEastHeading(double heading) { eastHeading = heading; }

    /**
     * Returns the initial heading of the robot for a reference.
     * @return the initial heading
     */
    public double getEastHeading() { return eastHeading; }

    /**
     * Drives the robot using robot centric.
     * @param x positive moves it right, negative moves it left
     * @param y positive moves it forwards, negative moves it backwards
     * @param rot positive spins it clockwise, negative spins it counter-clockwise
     */
    public void driveRobotCentric(double x, double y, double rot) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
        motorFL.setPower((-y - x - rot) / denominator);
        motorFR.setPower((-y + x + rot) / denominator);
        motorBL.setPower((y - x + rot) / denominator);
        motorBR.setPower((y + x - rot) / denominator);
    }

     /**
     * Drives the robot using field centric
     * @param x positive moves it right, negative moves it left
     * @param y positive moves it forwards, negative moves it backwards
     * @param rot positive spins it clockwise, negative spins it counter-clockwise
     */
    public void driveFieldCentric(double x, double y, double rot, double heading) {
        /*
            [ cos   -sin ] [ x ] = [ x cos - y sin ]
            [ sin    cos ] [ y ] = [ x sin + y cos ]
         */
        double angle = -(heading - eastHeading); // flipped bc clockwise
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
     */
    public void driveFieldAuto(double x, double y, double rot, double heading) {
        double bound = Constants.AutoConstants.driveMax;
        double boundedX = Math.max(Math.min(x, bound), -bound);
        double boundedY = Math.max(Math.min(y, bound), -bound);
        double boundedRot = Math.max(Math.min(rot, bound), -bound);
        driveFieldCentric(boundedX, boundedY, boundedRot, heading + eastHeading);
        // heading + eastHeading is a bit dumb but
    }

    /**
     * Halts every motor.
     */
    public void brake() {
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }

    public void setFLPower(double power) { motorFL.setPower(power); }
    public void setFRPower(double power) { motorFR.setPower(power); }
    public void setBLPower(double power) { motorBL.setPower(power); }
    public void setBRPower(double power) { motorBR.setPower(power); }

    /**
     * Returns the current drive state with each motor's position.
     * @return the drive state.
     */
    public DriveState getDriveState() {
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

    /**
     * Returns the current position of the front left motor
     * @return the front left motor's position
     */
    public double getFLPosition() { return motorFL.getCurrentPosition(); }

    /**
     * Returns the current position of the front right motor
     * @return the front right motor's position
     */
    public double getFRPosition() { return motorFR.getCurrentPosition(); }

    /**
     * Returns the current position of the back left motor
     * @return the back left motor's position
     */
    public double getBLPosition() { return motorBL.getCurrentPosition(); }

    /**
     * Returns the current position of the back right motor
     * @return the back right motor's position
     */
    public double getBRPosition() { return motorBR.getCurrentPosition(); }
}