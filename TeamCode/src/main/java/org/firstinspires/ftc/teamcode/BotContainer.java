package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.autonomous.Localizer;
import org.firstinspires.ftc.teamcode.commands.AutoCommand;
import org.firstinspires.ftc.teamcode.commands.DataLog;
import org.firstinspires.ftc.teamcode.commands.RunIndexer;
import org.firstinspires.ftc.teamcode.commands.RunIntake;
import org.firstinspires.ftc.teamcode.commands.ShootPID;
import org.firstinspires.ftc.teamcode.commands.TestDrive;
import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.commands.TestLocalizer;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;

import org.firstinspires.ftc.teamcode.commands.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.IntakeServos;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

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
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Gyroscope gyroscope = Gyroscope.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Intake intake = Intake.getInstance();
    private final IntakeServos intakeServos = IntakeServos.getInstance();
    private final Indexer indexer = Indexer.getInstance();
    private Localizer localizer;

    // others
    private final Bot.Alliance alliance;
    private final Bot.Config config;

    public BotContainer(Bot.Alliance alliance, Bot.Config config) {
        this.alliance = alliance;
        this.config = config;
        Constants.logger = new DataLog();
        Constants.logger.schedule();
        setDefaultCommands();
        configBindings();
    }

    boolean shooterOn = false;

    /**
     * Sets the commands that are run by default
     * when no other command is running.
     */
    private void setDefaultCommands() {
        Constants.logger.addEntry("elapsed time", BotContainer::timestamp);
        drivetrain.resetZeroPowerBehavior();

        switch (config) {
            case AUTO: {
                localizer = Localizer.getInstance();
                Constants.logger.addEntry("x", () -> localizer.getPose().x);
                Constants.logger.addEntry("y", () -> localizer.getPose().y);
                Constants.logger.addEntry("h", () -> localizer.getPose().heading);
            } break;
            case LOCALIZED_DRIVE: {
                Localizer.getInstance(); // create localizer instance first (if not made already)
                new TestLocalizer().schedule(); // actually yk localize
            } // overflow!
            case LOCALIZATION_TEST: {
                localizer = Localizer.getInstance();
                Constants.logger.addEntry("x", () -> localizer.getPose().x);
                Constants.logger.addEntry("y", () -> localizer.getPose().y);
                Constants.logger.addEntry("h", () -> localizer.getPose().heading);
            } // overflow!
            case TELE: {
                drivetrain.setDefaultCommand(new Drive(true,
                        () -> (1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
                                * pilot.getLeftX(),
                        () -> (1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
                                * pilot.getLeftY(),
                        () -> (1 - .75 * pilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
                                * pilot.getRightX() // multiplier based on right trigger :3
                ));
                intake.setDefaultCommand(new RunIntake(
                        () -> -copilot.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                ));
                shooter.setDefaultCommand(new ShootPID(
                        () -> copilot.getGamepadButton(GamepadKeys.Button.A).get()
                                ? -Constants.shooterRPM / 4
                                : shooterOn ? Constants.shooterRPM : 0
                ));
                Constants.logger.addEntry("shooter rpm", shooter::getRPM);
                indexer.setDefaultCommand(new RunIndexer(
                        () -> Constants.indexerPower * (
                                (copilot.getGamepadButton(GamepadKeys.Button.X).get() ? 1 : 0)
                                - (copilot.getGamepadButton(GamepadKeys.Button.Y).get() ? 1 : 0)
                        ) // X runs Y reverses
                ));
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
            case CUSTOM: {
                Constants.logger.addEntry("fl", () -> drivetrain.getDriveState().flPower);
            } break;
        }
    }
    /**
     * Configures the commands that run when a button or trigger is pressed.
     */
    private void configBindings() {
        switch (config) {
            case AUTO: break;
            case TELE: {
                copilot.getGamepadButton(GamepadKeys.Button.B)
                        .whenPressed(new RunCommand(intakeServos::intake))
                        .whenReleased(new RunCommand(intakeServos::outtake));

                copilot.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenPressed(
                        new InstantCommand(() -> shooterOn = true));
                copilot.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
                        new InstantCommand(() -> shooterOn = false));

                pilot.getGamepadButton(GamepadKeys.Button.B).whenActive(
                        () -> drivetrain.setEastHeading(gyroscope.getHeading())
                ); // reset gyro
            } break;
            case LOCALIZATION_TEST: // overflow!
            case LOCALIZED_DRIVE: {
                pilot.getGamepadButton(GamepadKeys.Button.B).whenActive(
                        () -> drivetrain.setEastHeading(gyroscope.getHeading())
                );

                pilot.getGamepadButton(GamepadKeys.Button.A).whenActive(
                        () -> localizer.resetPose()
                );
            } break;
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
