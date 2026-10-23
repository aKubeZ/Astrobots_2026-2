package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;

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

    private final CRServo leftServo;
    private final CRServo rightServo;
    private IntakeServos() {
        leftServo = Constants.hardwareMap.get(CRServo.class, "li");
        rightServo = Constants.hardwareMap.get(CRServo.class, "ri");
    }

    public void run() {
        leftServo.setPower(1);
        rightServo.setPower(1);
    }
}
