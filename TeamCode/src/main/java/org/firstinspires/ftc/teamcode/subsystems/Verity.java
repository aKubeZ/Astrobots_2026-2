package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * Verity; aka the flower intake thing.
 */
public class Verity extends SubsystemBase {
    private static Verity instance;
    public static synchronized Verity getInstance() {
        if (instance == null) instance = new Verity();
        return instance;
    }

    private final Servo servo;
    private final boolean disabled = !Constants.Subsystems.verity;
    private Verity() {
        servo = disabled ? null : Constants.hardwareMap.get(Servo.class, "vt");
        if (disabled) return;
    }

    /**
     * Move the servo up.
     */
    public void knowEverything() {
        if (disabled) return;
        servo.setPosition(Constants.Verity.everything);
    }

    /**
     * Move the servo down.
     */
    public void speakCow() {
        if (disabled) return;
        servo.setPosition(Constants.Verity.cow);
    }
}
