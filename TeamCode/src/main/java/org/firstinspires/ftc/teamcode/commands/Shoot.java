package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.function.DoubleSupplier;

public class Shoot extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final DoubleSupplier powerSupplier;

    public Shoot(DoubleSupplier powerSupplier) {
        this.powerSupplier = powerSupplier;
        addRequirements(shooter);
    }

    @Override
    public void execute() { shooter.run(powerSupplier.getAsDouble()); }
}
