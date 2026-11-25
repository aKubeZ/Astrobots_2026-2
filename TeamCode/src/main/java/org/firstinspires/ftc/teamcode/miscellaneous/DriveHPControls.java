package org.firstinspires.ftc.teamcode.miscellaneous;

import static org.firstinspires.ftc.teamcode.Constants.logger;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.Logger;

import java.util.function.DoubleSupplier;

/**
 * Manages the control for the DriveHeadingPID class
 * Using DoubleSuppliers for everything is honestly fun
 */
public class DriveHPControls {
    private GamepadEx pilot;
    private GamepadEx copilot;

    private double previousTimestamp;
    private double deltaT = 0;
    private boolean initialized = false;
    private double scaleRT = 0;
    private boolean gyroscopeCalibrating = false;
    private double derivative = 0;

    public DriveHPControls(GamepadEx pilot, GamepadEx copilot) {
        this.pilot = pilot;
        this.copilot = copilot;
        logger.addEntry("d", () -> derivative);
    }

    /**
     * Run this function before you access the suppliers.
     */
    public void update() {
        // previous timestamp
        if (!initialized) {
            previousTimestamp = BotContainer.timestamp();
            initialized = true;
        } else {
            deltaT = BotContainer.timestamp() - previousTimestamp;
            previousTimestamp = BotContainer.timestamp();
        }

        scaleRT = 1 - 0.75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        gyroscopeCalibrating = pilot.getGamepadButton(GamepadKeys.Button.BACK).get();
    }

    public double x() {
        if (gyroscopeCalibrating) return 0;
        return scaleRT * pilot.getLeftX();
    }

    public double y() {
        if (gyroscopeCalibrating) return 0;
        return scaleRT * pilot.getLeftY();
    }

    public double cos() {
        if (gyroscopeCalibrating) return 0;
        return pilot.getRightX();
    }

    public double sin() {
        if (gyroscopeCalibrating) return 0;
        return -pilot.getRightY();
    }

    public double turn() {
        if (gyroscopeCalibrating || deltaT == 0) return 0;
        return scaleRT * Constants.Drive.turnSpeed * deltaT * (
                (pilot.getGamepadButton(GamepadKeys.Button.X).get() ? 1 : 0)
                - (pilot.getGamepadButton(GamepadKeys.Button.B).get() ? 1 : 0)
        );
    }

    public boolean sustainHeading() {
        if (gyroscopeCalibrating) return false;
        return pilot.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).get();
    }
}
