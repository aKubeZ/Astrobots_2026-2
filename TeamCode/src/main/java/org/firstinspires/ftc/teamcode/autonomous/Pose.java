package org.firstinspires.ftc.teamcode.autonomous;

/**
 * An immutable 2D Point + heading.
 * Inherited because I feel like it.
 */
public class Pose extends Point {
    public final double heading;
    
    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = heading;
    }
}