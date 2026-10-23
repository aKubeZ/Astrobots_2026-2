package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.autonomous.Localizer;
import org.firstinspires.ftc.teamcode.autonomous.Path;
import org.firstinspires.ftc.teamcode.autonomous.PathTracer;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

public class AutoDrive extends CommandBase {
    private final Localizer localizer = Localizer.getInstance();
    private final PathTracer tracer;
    private final Path path;
    private double previousTimestamp;

    public AutoDrive(Path path) {
        this.path = path;
        tracer = new PathTracer(localizer::getPose, path);
        addRequirements(Drivetrain.getInstance()); // this is stupid
    }

    @Override
    public void initialize() {
        localizer.resetPose();
        tracer.initialize();
        previousTimestamp = BotContainer.timestamp();
    }

    @Override
    public void execute() {
        double timestamp = BotContainer.timestamp();
        double deltaT = timestamp - previousTimestamp;
        localizer.update(deltaT);
        tracer.trace();
        previousTimestamp = timestamp;
    }
}
