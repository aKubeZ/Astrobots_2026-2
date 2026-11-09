package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.pid.PIDController;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

import java.util.function.DoubleSupplier;

/**
 * A command that drives the robot using double suppliers
 * as inputs.
 */
public class DriveHeadingPID extends CommandBase {
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private final DoubleSupplier xSupplier;
    private final DoubleSupplier ySupplier;
    private final DoubleSupplier sinSupplier;
    private final DoubleSupplier cosSupplier;
    private final PIDController headingPID = new PIDController(Constants.AutoConstants.headingPID, true);
    private double previousTimestamp = -1;
    private double desiredHeading = 0;

    /**
     * Create a stick drive command.
     * @param xSupplier supplies the input that strafes the robot
     * @param ySupplier supplies the input that moves the robot back and forth
     * @param cosSupplier supplies the y-input that sets the robot heading
     * @param sinSupplier supplies the x-input that sets the robot heading
     */
    public DriveHeadingPID(
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier cosSupplier,
            DoubleSupplier sinSupplier
    ) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.cosSupplier = cosSupplier;
        this.sinSupplier = sinSupplier;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        previousTimestamp = BotContainer.timestamp();
    }

    @Override
    public void execute() {
        double timestamp = BotContainer.timestamp();
        double deltaT = timestamp - previousTimestamp;
        double currentHeading = gyroscope.getHeading();

        // find the heading we desire
        // if both suppliers are null do nothing
        double desiredCos = cosSupplier.getAsDouble();
        double desiredSin = sinSupplier.getAsDouble();

        if (desiredCos != 0 || desiredSin != 0)
            desiredHeading = Math.atan2(desiredSin, desiredCos);

        double driveRot = -headingPID.update(
                desiredHeading,
                currentHeading,
                deltaT
        );

        drivetrain.driveFieldCentric(
                xSupplier.getAsDouble(),
                ySupplier.getAsDouble(),
                driveRot,
                gyroscope.getHeading()
        );

        previousTimestamp = timestamp;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.brake();
    }
}