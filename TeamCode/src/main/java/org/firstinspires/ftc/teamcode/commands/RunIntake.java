package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.function.DoubleSupplier;

/**
 * Command that runs the intake.
 */
public class RunIntake extends CommandBase {
    private final Intake intake = Intake.getInstance();
    private final DoubleSupplier powerSupplier;

    /**
     * Continuously run the intake given a supplier for the power.
     * @param powerSupplier the power supplier.
     */
    public RunIntake(DoubleSupplier powerSupplier) {
        this.powerSupplier = powerSupplier;
        addRequirements(intake);
    }

    @Override
    public void execute() { intake.run(powerSupplier.getAsDouble()); }
}
