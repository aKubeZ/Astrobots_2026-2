package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * An intake subsystem.
 */
public class Intake extends SubsystemBase {
    private static Intake instance;
    public static synchronized Intake getInstance() {
        if (instance == null) instance = new Intake();
        return instance;
    }

    private final CRServo servo;
    private final boolean disabled = !Constants.Subsystems.intake;
    private Intake() {
        servo = disabled ? null : Constants.hardwareMap.get(CRServo.class, "in");
        if (disabled) return;
    }

    public void run(double power) {
        if (disabled) return;
        servo.setPower(power);
    }
}
