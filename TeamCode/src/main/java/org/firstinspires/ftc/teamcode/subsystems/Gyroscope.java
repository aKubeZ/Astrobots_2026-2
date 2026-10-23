package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * Gyroscope subsystem.
 */
public class Gyroscope extends SubsystemBase {
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
    private Gyroscope() { gyro = Constants.hardwareMap.get(IMU.class, "imu"); }

    /**
     * Returns the heading of the gyroscope.
     * @return gyroscope heading
     */
    public double getHeading() { return gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS); }
}