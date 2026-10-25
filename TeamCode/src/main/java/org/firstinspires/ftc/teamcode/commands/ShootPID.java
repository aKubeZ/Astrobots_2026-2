package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.pid.PIDController;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.function.DoubleSupplier;

public class ShootPID extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final DoubleSupplier rpmSupplier;
    private final PIDController pidController
            = new PIDController(Constants.AutoConstants.shooterPID, false);
    private double previousTimestamp = -1;

    public ShootPID(DoubleSupplier rpmSupplier) {
        this.rpmSupplier = rpmSupplier;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        if (previousTimestamp == -1) {
            previousTimestamp = BotContainer.timestamp();
            return;
        }

        if (rpmSupplier.getAsDouble() == 0){
            shooter.brake();
            pidController.resetIntegral();
        }
        else shooter.run(pidController.update(
                rpmSupplier.getAsDouble(),
                shooter.getRPM(),
                BotContainer.timestamp()
        ));
    }
}
