package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * An intake subsystem.
 */
public class Intake extends SubsystemBase {
    private static Intake shooter;
    public static synchronized Intake getInstance() {
        if (shooter == null) shooter = new Intake();
        return shooter;
    }

    private final DcMotorEx motor;
    private final boolean disabled = !Constants.SubsystemToggle.intake;
    private Intake() {
        motor = disabled ? null : Constants.hardwareMap.get(DcMotorEx.class, "in");
        if (disabled) return;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void run(double power) {
        if (disabled) return;
        motor.setPower(power);
    }
}
