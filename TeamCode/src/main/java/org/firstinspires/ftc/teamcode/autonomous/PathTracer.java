package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.pid.PIDController;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

import java.util.function.Supplier;

/**
 * Command that attempts to make the robot trace a path using PID.
 */
public class PathTracer {
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private final Path path;
    private final Supplier<Pose> localizer;

    private final PIDController xPID = new PIDController(Constants.AutoConstants.xyPID, false);
    private final PIDController yPID = new PIDController(Constants.AutoConstants.xyPID, false);
    private final PIDController headingPID = new PIDController(Constants.AutoConstants.headingPID, true);

    private double timeOffset;
    private double previousTimestamp;
    private double driveX = 0;
    private double driveY = 0;
    private double driveRot = 0;

    /**
     * Create a path tracer.
     * @param localizer a supplier that constantly gives the robot pose.
     * @param path the path the tracer will follow.
     */
    public PathTracer(Supplier<Pose> localizer, Path path) {
        this.localizer = localizer;
        this.path = path;
    }

    /**
     * Sets the tracer's time offset. This should be run
     * not in initialize but in execute once it begins running.
     */
    public void resetTimeOffset() {
        timeOffset = BotContainer.timestamp();
    }

    /**
     * Traces the path!
     */
    public void trace() {
        double timestamp = BotContainer.timestamp() - timeOffset;
        Pose desiredPose = path.at(timestamp);
        double deltaT = timestamp - previousTimestamp;
        driveX = xPID.update(desiredPose.x, localizer.get().x, deltaT);
        driveY = yPID.update(desiredPose.y, localizer.get().y, deltaT);
        driveRot = headingPID.update(desiredPose.heading, localizer.get().heading, deltaT);
        drivetrain.driveFieldAuto(driveX, driveY, driveRot, localizer.get().heading);

        // update previous timestamp
        previousTimestamp = timestamp;
    }
}
