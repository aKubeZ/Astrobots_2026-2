package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

import java.util.function.DoubleSupplier;

/**
 * A command that drives the robot using double suppliers
 * as inputs for each individual motor for testing.
 */
public class TestDrive extends CommandBase {
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final DoubleSupplier flSupplier;
    private final DoubleSupplier frSupplier;
    private final DoubleSupplier blSupplier;
    private final DoubleSupplier brSupplier;

    /**
     * Create a test drive command.
     * @param flSupplier supplier for the front left motor
     * @param frSupplier supplier for the front right motor
     * @param blSupplier supplier for the back left motor
     * @param brSupplier supplier for the back right motor
     */
    public TestDrive(
            DoubleSupplier flSupplier,
            DoubleSupplier frSupplier,
            DoubleSupplier blSupplier,
            DoubleSupplier brSupplier
    ) {
        this.flSupplier = flSupplier;
        this.frSupplier = frSupplier;
        this.blSupplier = blSupplier;
        this.brSupplier = brSupplier;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.setFLPower(flSupplier.getAsDouble());
        drivetrain.setFRPower(frSupplier.getAsDouble());
        drivetrain.setBLPower(blSupplier.getAsDouble());
        drivetrain.setBRPower(brSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.brake();
    }
}