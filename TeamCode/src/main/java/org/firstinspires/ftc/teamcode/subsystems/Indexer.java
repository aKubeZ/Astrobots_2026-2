package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * A magazine subsystem; transports pollen/nectar from the intake
 * to the shooter.
 */
public class Indexer extends SubsystemBase {
    private static Indexer shooter;
    public static synchronized Indexer getInstance() {
        if (shooter == null) shooter = new Indexer();
        return shooter;
    }

    private final DcMotorEx motor;
    private Indexer() {
        motor = Constants.hardwareMap.get(DcMotorEx.class, "ix");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void run(double power) { motor.setPower(power); }
}
