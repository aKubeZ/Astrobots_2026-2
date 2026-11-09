package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * Gyroscope (not really a subsystem but).
 */
public class Gyroscope {
    private static Gyroscope instance = null;

    /**
     * Returns the gyroscope instance.
     * Use this instead of creating a new instance.
     * @return the gyroscope instance.
     */
    public static synchronized Gyroscope getInstance() {
        if (instance == null) instance = new Gyroscope();
        return instance;
    }

    private final IMU gyro;
    private double headingOrigin = 0;
    private Gyroscope() { gyro = Constants.hardwareMap.get(IMU.class, "imu"); }

    /**
     * Sets the heading origin to be such that getHeading()
     * currently returns the given heading.
     */
    public void setCurrentHeading(double heading) {
        headingOrigin = getRawHeading() - heading;
    }

    /**
     * Returns the heading of the gyroscope, offset with the set value.
     * @return gyroscope heading
     */
    public double getHeading() {
        return getRawHeading() - headingOrigin;
    }

    /**
     * Returns the reading of the gyroscope.
     * @return gyroscope heading
     */
    public double getRawHeading() {
        return gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
}