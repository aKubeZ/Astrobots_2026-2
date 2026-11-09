package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.autonomous.Localizer;
import org.firstinspires.ftc.teamcode.autonomous.Path;
import org.firstinspires.ftc.teamcode.autonomous.PathTracer;
import org.firstinspires.ftc.teamcode.autonomous.Pose;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

/**
 * A command implementation of PathTracer.
 */
public class AutoDrive extends CommandBase {
    private final Localizer localizer = Localizer.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private final PathTracer tracer;
    private final Path path;
    private double previousTimestamp;
    private boolean initialized = false;

    /**
     * Create an AutoDrive command given a path.
     * @param path the path to follow.
     */
    public AutoDrive(Path path) {
        this.path = path;
        tracer = new PathTracer(localizer::getPose, path);
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Pose startPose = path.at(0);
        gyroscope.setCurrentHeading(startPose.heading);
        localizer.resetPose(startPose);
        previousTimestamp = BotContainer.timestamp();
    }

    @Override
    public void execute() {
        if (!initialized) {
            tracer.resetTimeOffset();
            initialized = true;
            return;
        }
        double timestamp = BotContainer.timestamp();
        double deltaT = timestamp - previousTimestamp;
        localizer.update(deltaT);
        tracer.trace();
        previousTimestamp = timestamp;
    }

    @Override
    public void end(boolean interrupted) {
        // so apparently end() in a command op mode doesnt work
        // so im moving ts to execute
        // kill me
//        Constants.AutoConstants.autoEndHeading = gyroscope.getHeading();
        initialized = false;
    }
}
