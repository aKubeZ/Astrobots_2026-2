package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * A magazine subsystem; transports pollen/nectar from the intake
 * to the shooter.
 */
public class IntakeServos extends SubsystemBase {
    private static IntakeServos shooter;
    public static synchronized IntakeServos getInstance() {
        if (shooter == null) shooter = new IntakeServos();
        return shooter;
    }

    private final Servo leftServo;
    private final Servo rightServo;
    private final boolean disabled = !Constants.SubsystemToggle.intakeServos;
    private IntakeServos() {
        leftServo = disabled ? null : Constants.hardwareMap.get(Servo.class, "li");
        rightServo = disabled ? null : Constants.hardwareMap.get(Servo.class, "ri");
    }

    /**
     * Make the servos spin inward given that they weren't already.
     */
    public void intake() {
        if (disabled) return;
        leftServo.setPosition(0);
        rightServo.setPosition(1);
    }

    /**
     * Make the servos spin outward given that they weren't already.
     */
    public void outtake() {
        if (disabled) return;
        leftServo.setPosition(1);
        rightServo.setPosition(0);
    }
}
