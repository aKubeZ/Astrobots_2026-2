package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * A shooter subsystem.
 */
public class Shooter extends SubsystemBase {
    private static Shooter shooter;
    public static synchronized Shooter getInstance() {
        if (shooter == null) shooter = new Shooter();
        return shooter;
    }

    private final DcMotorEx motor;
    private final boolean disabled = !Constants.SubsystemToggle.shooter;
    private Shooter() {
        motor = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "sh");
        if (disabled) return;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void run(double power) {
        if (disabled) return;
        motor.setPower(power);
    }

    public void brake() {
        if (disabled) return;
        motor.setPower(0);
    }

    public double getRPM() {
        if (disabled) return 0;
        return motor.getVelocity();
    }
}
