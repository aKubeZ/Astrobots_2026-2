package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

import java.util.function.DoubleSupplier;

/**
 * A command that drives the robot using double suppliers
 * as inputs.
 */
public class Drive extends CommandBase {
    boolean fieldCentric;
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private final DoubleSupplier xSupplier;
    private final DoubleSupplier ySupplier;
    private final DoubleSupplier rotSupplier;

    /**
     * Create a stick drive command.
     * @param fieldCentric if we drive using field centric or not
     * @param xSupplier supplies the input that strafes the robot
     * @param ySupplier supplies the input that moves the robot back and forth
     * @param rotSupplier supplies the input that turns the robot
     */
    public Drive(
            boolean fieldCentric,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier rotSupplier
    ) {
        this.fieldCentric = fieldCentric;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.rotSupplier = rotSupplier;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        if (fieldCentric)
            drivetrain.driveFieldCentric(
                    xSupplier.getAsDouble(),
                    ySupplier.getAsDouble(),
                    rotSupplier.getAsDouble(),
                    gyroscope.getHeading()
            );
        else
            drivetrain.driveRobotCentric(
                    xSupplier.getAsDouble(),
                    ySupplier.getAsDouble(),
                    rotSupplier.getAsDouble()
            );

    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.brake();
    }

    @Override
    public boolean isFinished() { return false; }
}