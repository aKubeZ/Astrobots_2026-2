package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.autonomous.Localizer;
import org.firstinspires.ftc.teamcode.autonomous.Pose;
import org.firstinspires.ftc.teamcode.miscellaneous.Logger;

import org.firstinspires.ftc.teamcode.miscellaneous.Gyroscope;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.commands.*;

public class BotContainer {
    private static final ElapsedTime stopwatch = new ElapsedTime();

    /**
     * Returns the current timestamp.
     * @return the current timestamp.
     */
    public static double timestamp() { return stopwatch.seconds(); }

    // controllers
    private final GamepadEx pilot = Constants.pilot;
    private final GamepadEx copilot = Constants.copilot;

    // subsystems
    private final Drivetrain drivetrain = org.firstinspires.ftc.teamcode.subsystems.Drivetrain.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Indexer indexer = Indexer.getInstance();
    private final Verity verity = Verity.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();

    // others
    private final Bot.Alliance alliance;
    private final Bot.Config config;
    private Localizer localizer;
    private final Logger logger = Logger.getInstance();

    public BotContainer(Bot.Alliance alliance, Bot.Config config) {
        this.alliance = alliance;
        this.config = config;
        logger.schedule();
        setDefaultCommands();
        configBindings();
    }

    boolean shooterOn = false;

    /**
     * Sets the commands that are run by default
     * when no other command is running.
     */
    private void setDefaultCommands() {
        logger.addEntry("elapsed time", BotContainer::timestamp);
        drivetrain.resetZeroPowerBehavior();

        switch (config) {
            case AUTO: {
                localizer = org.firstinspires.ftc.teamcode.autonomous.Localizer.getInstance();
                logger.addEntry("x", () -> localizer.getPose().x);
                logger.addEntry("y", () -> localizer.getPose().y);
                logger.addEntry("h", () -> localizer.getPose().heading);
            } break;
            case LOCALIZED_DRIVE: {
                org.firstinspires.ftc.teamcode.autonomous.Localizer.getInstance(); // create localizer instance first (if not made already)
                new TestLocalizer().schedule(); // actually yk localize
            } // overflow!
            case LOCALIZATION_TEST: {
                localizer = org.firstinspires.ftc.teamcode.autonomous.Localizer.getInstance();
                logger.addEntry("x", () -> localizer.getPose().x);
                logger.addEntry("y", () -> localizer.getPose().y);
                logger.addEntry("h", () -> localizer.getPose().heading);
            } // overflow!
            case TELE: {
                // set drivetrain stuff for teleop
//                drivetrain.setDefaultCommand(new Drive(false, () -> 0, () -> 0, () -> 1));
                gyroscope.setCurrentHeading(
                        - Constants.Drive.teleStartHeading
                        + gyroscope.getHeading()
                );

                // drivetrain
                DriveHeadingPID driveCommand = new DriveHeadingPID(
                        () -> (1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
                                * pilot.getLeftX(),
                        () -> (1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
                                * pilot.getLeftY(),
                        () -> pilot.getRightX(), // multiplier based on right trigger :3
                        () -> -pilot.getRightY() // multiplier based on right trigger :3
//                        () -> -(1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
//                                * pilot.getRightX() // negative because turn is ccw now
                );
                pilot.getGamepadButton(GamepadKeys.Button.B).whenActive(() -> {
                    gyroscope.setCurrentHeading(0);
                    driveCommand.setDesiredHeading(0);
                }); // reset gyro
                drivetrain.setDefaultCommand(driveCommand);

                // intake
                intake.setDefaultCommand(new RunIntake(
                        () -> Constants.intakePower * (
                                copilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                                - copilot.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
                        )
                ));

                // shooter
                copilot.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenPressed(
                        new InstantCommand(() -> shooterOn = true));
                copilot.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
                        new InstantCommand(() -> shooterOn = false)); // shooter toggle
                shooter.setDefaultCommand(new ShootPID(
                        () -> copilot.getGamepadButton(GamepadKeys.Button.A).get()
                                ? -Constants.shooterRPM / 4
                                : shooterOn ? Constants.shooterRPM : 0
                ));

                // logger
                logger.addEntry("shooter rpm", shooter::getRPM);
            } break;
            case DRIVETRAIN_TEST: {
                drivetrain.setDefaultCommand(new TestDrive(
                        () -> (pilot.getGamepadButton(GamepadKeys.Button.Y).get() ? 1 : 0)
                                - (pilot.getGamepadButton(GamepadKeys.Button.DPAD_UP).get() ? 1 : 0),
                        () -> (pilot.getGamepadButton(GamepadKeys.Button.B).get() ? 1 : 0)
                                - (pilot.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).get() ? 1 : 0),
                        () -> (pilot.getGamepadButton(GamepadKeys.Button.X).get() ? 1 : 0)
                                - (pilot.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).get() ? 1 : 0),
                        () -> (pilot.getGamepadButton(GamepadKeys.Button.A).get() ? 1 : 0)
                                - (pilot.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).get() ? 1 : 0)
                ));
                // If you hold your controller rotated 45 degrees counterclockwise it's easy
                // X Y A B will correspond to positive rotation of the FL FR BL BR motors
                // UP RIGHT LEFT DOWN will correspond to the negative rotation of the FL FR BL BR motors
            } break;
            case ENCODER_DRIVE: {
                logger.addEntry("fl", () -> drivetrain.getDriveState().flEncoding);
                logger.addEntry("fr", () -> drivetrain.getDriveState().frEncoding);
                logger.addEntry("bl", () -> drivetrain.getDriveState().blEncoding);
                logger.addEntry("br", () -> drivetrain.getDriveState().brEncoding);
            } break;
            case CUSTOM: {

            } break;
        }
    }
    /**
     * Configures the commands that run when a button or trigger is pressed.
     */
    private void configBindings() {
        switch (config) {
            case AUTO:
                break;
            case TELE: {
                copilot.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(new InstantCommand(verity::knowEverything));
                copilot.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenPressed(new InstantCommand(verity::speakCow));
                copilot.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                        .whenPressed(new InstantCommand(logger::clearEntries));
            }
            break;
            case LOCALIZATION_TEST: // overflow!
            case LOCALIZED_DRIVE: {
                pilot.getGamepadButton(GamepadKeys.Button.B).whenActive(
                        () -> gyroscope.setCurrentHeading(0)
                );

                pilot.getGamepadButton(GamepadKeys.Button.A).whenActive(
                        () -> localizer.resetPose(new Pose(0, 0, 0))
                );
            }
            break;
            case DRIVETRAIN_TEST: {

            } break;
        }
    }

    /**
     * Returns the auto command.
     * @return the autonomous command to be used
     */
    public Command getAutonomousCommand() {
        switch (config) {
            case AUTO: return new AutoCommand(alliance);
            case LOCALIZATION_TEST: return new TestLocalizer();
        }

        return new InstantCommand(); // do nothing
    }
}
