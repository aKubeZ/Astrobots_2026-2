package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Indexer;

import java.util.function.DoubleSupplier;

/**
 * Command that runs the indexer.
 */
public class RunIndexer extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final DoubleSupplier powerSupplier;

    /**
     * Continuously run the indexer given a supplier for the power.
     * @param powerSupplier the power supplier.
     */
    public RunIndexer(DoubleSupplier powerSupplier) {
        this.powerSupplier = powerSupplier;
        addRequirements(indexer);
    }

    @Override
    public void execute() { indexer.run(powerSupplier.getAsDouble()); }
}
