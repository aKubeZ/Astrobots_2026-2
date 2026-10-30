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
    private IntakeServos() {
        leftServo = Constants.hardwareMap.get(Servo.class, "li");
        rightServo = Constants.hardwareMap.get(Servo.class, "ri");
    }

    public void intake() {
        leftServo.setPosition(0);
        rightServo.setPosition(1);
    }

    public void outtake() {
        leftServo.setPosition(1);
        rightServo.setPosition(0);
    }
}
