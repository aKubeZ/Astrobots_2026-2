package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

/**
 * An attempt to make localization possible with the robot
 * Tries to use the drive encoders + gyroscope to estimate its position.
 */
public class Localizer {
    private static Localizer instance; // not even a subsystem but
    public static synchronized Localizer getInstance() {
        if (instance == null) instance = new Localizer();
        return instance;
    }

    // give the slip multiplier given an acceleration value
    // ill be changing this constantly probably
    // these two functions probably don't do much but oh well
    private static double slipMultiplierX(double accel) {
        return (accel < 450) ? 1 : (accel < 800) ? 0.5 : 0;
    }

    private static double slipMultiplierY(double accel) {
        return (accel < 600) ? 1 : (accel < 1000) ? 0.5 : 0;
    }

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private double previousFLEncoding = 0; // to calculate speed
    private double previousFREncoding = 0;
    private double previousBLEncoding = 0;
    private double previousBREncoding = 0;

    private double previousFLSpeed = 0; // to calculate acceleration
    private double previousFRSpeed = 0;
    private double previousBLSpeed = 0;
    private double previousBRSpeed = 0;

    private double netWheelAccel = 0;
    private boolean initialized = false;
    private boolean initializedAgain = false;

    private Pose pose = new Pose(0, 0, 0);

    private Localizer() { }

    /**
     * Updates the robot pose.
     * @param deltaT the change in time since the last update.
     */
    public void update(double deltaT) {
        if (deltaT == 0) return;
        // TODO: test this bc all of this only hypothetically works
        Drivetrain.DriveState driveState = drivetrain.getDriveState();
        if (!initialized) {
            previousFLEncoding = driveState.flEncoding;
            previousFREncoding = driveState.frEncoding;
            previousBLEncoding = driveState.blEncoding;
            previousBREncoding = driveState.brEncoding;
            initialized = true;
            return;
        }

        // these doubles probably will be in inches per second
        double rawFLSpeed = ((driveState.flPower == 0)
                ? Constants.AutoConstants.deadInchesPerTickFL
                : Constants.AutoConstants.freeInchesPerTickFL)
                * (driveState.flEncoding - previousFLEncoding) / deltaT;
        double rawFRSpeed = ((driveState.frPower == 0)
                ? Constants.AutoConstants.deadInchesPerTickFR
                : Constants.AutoConstants.freeInchesPerTickFR)
                * (driveState.frEncoding - previousFREncoding) / deltaT;
        double rawBLSpeed = ((driveState.blPower == 0)
                ? Constants.AutoConstants.deadInchesPerTickBL
                : Constants.AutoConstants.freeInchesPerTickBL)
                * (driveState.blEncoding - previousBLEncoding) / deltaT;
        double rawBRSpeed = ((driveState.brPower == 0)
                ? Constants.AutoConstants.deadInchesPerTickBR
                : Constants.AutoConstants.freeInchesPerTickBR)
                * (driveState.brEncoding - previousBREncoding) / deltaT;

        double flSpeed;
        double frSpeed;
        double blSpeed;
        double brSpeed;
        if (!initializedAgain) {
            flSpeed = previousFLSpeed = rawFLSpeed;
            frSpeed = previousFRSpeed = rawFRSpeed;
            blSpeed = previousBLSpeed = rawBLSpeed;
            brSpeed = previousBRSpeed = rawBRSpeed;
            initializedAgain = true;
        } else {
            // to account for slipping yay
            double flAccel = (rawFLSpeed - previousFLSpeed) / deltaT;
            double frAccel = (rawFRSpeed - previousFRSpeed) / deltaT;
            double blAccel = (rawBLSpeed - previousBLSpeed) / deltaT;
            double brAccel = (rawBRSpeed - previousBRSpeed) / deltaT;

//            flSpeed = rawFLSpeed * slipMultiplier(flAccel);
//            frSpeed = rawFRSpeed * slipMultiplier(frAccel);
//            blSpeed = rawBLSpeed * slipMultiplier(blAccel);
//            brSpeed = rawBRSpeed * slipMultiplier(brAccel);
            flSpeed = rawFLSpeed;
            frSpeed = rawFRSpeed;
            blSpeed = rawBLSpeed;
            brSpeed = rawBRSpeed;
            netWheelAccel = (Math.abs(flAccel) + Math.abs(frAccel) + Math.abs(blAccel) + Math.abs(brAccel)) / 4;
        }

        /* essentially just this but inverted
            [ fl ]   [ -1  -1  -1 ]
            [ fr ] = [  1  -1   1 ] [ x ]
            [ bl ]   [ -1   1   1 ] [ y ]
            [ br ]   [  1   1  -1 ] [ t ]
         */
        double dXRelative = slipMultiplierX(netWheelAccel) *
                Constants.AutoConstants.localizeXScale
                * (-flSpeed + frSpeed - blSpeed + brSpeed) / 4;
        double dYRelative = slipMultiplierY(netWheelAccel) *
                Constants.AutoConstants.localizeYScale
                * (-flSpeed - frSpeed + blSpeed + brSpeed) / 4;

        /* Rotate based on heading
            [ cos   -sin ] [ x ] = [ x cos - y sin ]
            [ sin    cos ] [ y ] = [ x sin + y cos ]
         */
        double heading = gyroscope.getHeading() - drivetrain.getEastHeading();
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);
        double dX = dXRelative * cos - dYRelative * sin;
        double dY = dXRelative * sin + dYRelative * cos;

        pose = new Pose(
                pose.x + dX * deltaT,
                pose.y + dY * deltaT,
                heading
        );

        // update the previous values
        previousFLEncoding = driveState.flEncoding;
        previousFREncoding = driveState.frEncoding;
        previousBLEncoding = driveState.blEncoding;
        previousBREncoding = driveState.brEncoding;

        previousFLSpeed = rawFLSpeed;
        previousFRSpeed = rawFRSpeed;
        previousBLSpeed = rawBLSpeed;
        previousBRSpeed = rawBRSpeed;
    }

    /**
     * Returns the estimated robot pose.
     * @return the robot pose
     */
    public Pose getPose() { return pose; }

    /**
     * Reset the pose to the robot start pose.
     */
    public void resetPose() {
        pose = Constants.AutoConstants.robotStartPose;
        initialized = false;
    }

    public double getNetWheelAccel() { return netWheelAccel; }
}