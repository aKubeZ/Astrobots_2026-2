package org.firstinspires.ftc.teamcode.pid;

/**
 * A PID controller to control a machine's input based on
 * its output and a desired output.
 */
public class PIDController {
    // constants
    private final double kP;
    private final double kI;
    private final double kD;
    private final boolean cyclic;

    // values
    private double integral = 0;
    private double previousError;
    private boolean initialized = false;

    /**
     * Create a PID controller given PID data
     * @param pidData the object that stores the proportional, derivative, and integral constants
     * @param cyclic set this this to true if this is PID for the heading.
     *               This accounts for the jump in heading from pi to -pi.
     */
    public PIDController(PIDData pidData, boolean cyclic) {
        this.kP = pidData.kP;
        this.kI = pidData.kI;
        this.kD = pidData.kD;
        this.cyclic = cyclic;
    }

    /**
     * Updates the PID and returns the input. Be sure to setTimeOffset before using.
     * @param desiredOutput the desired output of the machine
     * @param output the output of the machine
     * @param deltaT the change in time
     * @return the new input to be used
     */
    public double update(double desiredOutput, double output, double deltaT) {
        double error = desiredOutput - output;
        if (cyclic) {
            if (error > Math.PI) error -= 2 * Math.PI;
            else if (error < -Math.PI) error += 2 * Math.PI;
        }

        if (!initialized) {
            previousError = error;
            initialized = true;
        }

        double derivative = (error - previousError) / deltaT;
        integral += error * deltaT;

        previousError = output;
        return kP * error + kI * integral + kD * derivative;
    }

    /**
     * Reset the integral like when the motor should stop
     */
    public void resetIntegral() { integral = 0; }
}
