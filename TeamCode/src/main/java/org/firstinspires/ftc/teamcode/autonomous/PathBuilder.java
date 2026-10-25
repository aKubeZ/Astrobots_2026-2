package org.firstinspires.ftc.teamcode.autonomous;

public class PathBuilder {
    private Path path;
    private double nextPathStartTime = 0;

    public PathBuilder(double x, double y, double heading) {
        path = (double time) -> new Pose(x, y, heading);
    }

    /**
     * Append a path to the path being built, with the second path
     * starting at t = 0 when the previous paths end.
     * @param path the path to append.
     * @param duration the amount of time to run the appending path.
     * @return continue building.
     */
    public PathBuilder appendPath(Path path, double duration) {
        double pathStartTime = nextPathStartTime;
        Path oldPath = this.path;
        this.path = (double t) -> (t - pathStartTime < duration) ? oldPath.at(t) : path.at(t - duration);
        nextPathStartTime += pathStartTime;
        return this;
    }

    /**
     * Stay put and wait for a certain amount of time.
     * @param duration the amount of time to wait.
     * @return continue building.
     */
    public PathBuilder stall(double duration) {
        double pathStartTime = nextPathStartTime;
        Pose stallPose = path.at(pathStartTime);

        Path oldPath = path;
        path = (double t) -> (t < pathStartTime) ? oldPath.at(t) : stallPose;
        nextPathStartTime += duration;

        return this;
    }

    /**
     * Moves linearly to a given pose.
     * @param x the x to move to
     * @param y the y to move to
     * @param heading the heading to turn to
     * @param duration the amount of time this movement should take
     * @return continue building.
     */
    public PathBuilder moveTo(double x, double y, double heading, double duration) {
        double pathStartTime = nextPathStartTime;
        Pose startPose = path.at(pathStartTime);

        double offsetHeading;
        if (heading - startPose.heading > Math.PI) offsetHeading = heading - 2 * Math.PI;
        else if (heading - startPose.heading < -Math.PI) offsetHeading = heading + 2 * Math.PI;
        else offsetHeading = heading;

        Path oldPath = this.path;
        Path newPath = (double t) -> {
            double startMultiplier = (duration == 0) ? 0 : (duration - t) / duration;
            double endMultiplier = (duration == 0) ? 1 : (t - pathStartTime) / duration;
            return new Pose(
                    startMultiplier * startPose.x + endMultiplier * x,
                    startMultiplier * startPose.y + endMultiplier * y,
                    startMultiplier * startPose.heading + endMultiplier * offsetHeading
            );
        };

        this.path = (double t) -> (t < pathStartTime) ? oldPath.at(t) : newPath.at(t);
        nextPathStartTime += duration;

        return this;
    }

    /**
     * Build the path!
     * @return the path.
     */
    public Path build() {
        this.stall(30); // make it stop moving
        return path;
    }
}
