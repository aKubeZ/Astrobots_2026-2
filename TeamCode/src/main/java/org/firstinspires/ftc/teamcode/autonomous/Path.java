package org.firstinspires.ftc.teamcode.autonomous;

/**
 * Immutable 3D parametric equation of x, y, and heading.
 */
@FunctionalInterface
public interface Path {
    /**
     * Get the pose at a given timestamp.
     * @param t the timestamp (in seconds) to get the pose.
     * @return the pose at the given timestamp.
     */
    Pose at(double t);
}