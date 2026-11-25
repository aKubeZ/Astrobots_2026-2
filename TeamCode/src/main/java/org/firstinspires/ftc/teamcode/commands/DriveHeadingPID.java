package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.miscellaneous.DriveHPControls;
import org.firstinspires.ftc.teamcode.pid.PIDController;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.miscellaneous.Gyroscope;

import java.util.function.BooleanSupplier;
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
    private final DoubleSupplier turnSupplier;
    private final BooleanSupplier sustainHeadingSupplier;
    private Runnable updateControls = () -> {};
    private final PIDController headingPID = new PIDController(Constants.Drive.headingPID, true);
    private double previousTimestamp = -1;
    private double desiredHeading = 0;
    private Logger logger = Constants.logger;
    private DriveHPControls controls;

    /**
     *
     */
    public DriveHeadingPID(DriveHPControls controls) {
        this(controls::x, controls::y, controls::cos, controls::sin, controls::turn, controls::sustainHeading);
        this.controls = controls;
        updateControls = controls::update;
    }

    /**
     * Create a stick drive command.
     * @param xSupplier supplies the input that strafes the robot
     * @param ySupplier supplies the input that moves the robot back and forth
     * @param cosSupplier supplies the y-input that sets the robot heading
     * @param sinSupplier supplies the x-input that sets the robot heading
     * @param turnSupplier directly modifies the desired heading
     * @param sustainHeadingSupplier if false then the desired heading becomes the current heading
     */
    public DriveHeadingPID(
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier cosSupplier,
            DoubleSupplier sinSupplier,
            DoubleSupplier turnSupplier,
            BooleanSupplier sustainHeadingSupplier
    ) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.cosSupplier = cosSupplier;
        this.sinSupplier = sinSupplier;
        this.turnSupplier = turnSupplier;
        this.sustainHeadingSupplier = sustainHeadingSupplier;

        addRequirements(drivetrain);
    }

    public void setDesiredHeading(double heading) {
        desiredHeading = heading;
    }

    @Override
    public void initialize() {
        // make sure the robot doenst spin on start
        setDesiredHeading(gyroscope.getHeading());
        previousTimestamp = BotContainer.timestamp();
    }

    double x = 0;
    double y = 0;
    double rot = 0;
    @Override
    public void execute() {
        updateControls.run();
        double timestamp = BotContainer.timestamp();
        double deltaT = timestamp - previousTimestamp;
        double currentHeading = gyroscope.getHeading();

        // find the heading we desire
        // if both suppliers are null do nothing
        double desiredCos = cosSupplier.getAsDouble();
        double desiredSin = sinSupplier.getAsDouble();
        double desiredTurn = turnSupplier.getAsDouble();
        double stickDistance = Math.hypot(desiredSin, desiredCos);
        boolean freeHeading = !sustainHeadingSupplier.getAsBoolean();

        if (stickDistance > 0.8)
            desiredHeading = Math.atan2(desiredSin, desiredCos);
        else if (freeHeading)
            desiredHeading = currentHeading;
        desiredHeading += turnSupplier.getAsDouble();


        double driveRot = headingPID.update(
                desiredHeading,
                currentHeading,
                deltaT
        );

        x = xSupplier.getAsDouble();
        y = ySupplier.getAsDouble();
        rot = driveRot;
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