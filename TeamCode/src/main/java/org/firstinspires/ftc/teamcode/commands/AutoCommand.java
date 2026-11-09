package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.autonomous.Path;
import org.firstinspires.ftc.teamcode.autonomous.PathBuilder;
import org.firstinspires.ftc.teamcode.autonomous.Pose;

/**
 * Default autonomous command.
 */
public class AutoCommand extends ParallelCommandGroup {
    private final Bot.Alliance alliance;
    private double initialTimestamp = -1;

    /**
     * Creates the auto command.
     * @param alliance The alliance of the robot
     */
    public AutoCommand(Bot.Alliance alliance) {
        this.alliance = alliance;
        addCommands(new InstantCommand(() -> initialTimestamp = BotContainer.timestamp()));

//        double pi = Math.PI;
//        addCommands(new AutoDrive(t -> {
//            if (t < 10) {
//                return new Pose(
//                        54, 0, pi
//                );
//            } else if (t < 15) {
//                double progress = (t - 10) / 5;
//                return new Pose(
//                        54, 110 * progress, pi
//                );
//            } else if (t < 18) {
//                double progress = (t - 15) / 3;
//                return new Pose(
//                        54 - 54 * progress, 110, pi
//                );
//            } else return new Pose(-20, 110, pi);
//        }));
//        addCommands(new AutoDrive(t -> {
//            return new Pose(0, 10 * t, 0);
//        }));
        addCommands(new AutoDrive(t -> {
            return new Pose(0, 0, t / 5 + 1);
        }));

        addCommands(
                new ShootPID(() -> {
                    double t = BotContainer.timestamp() - initialTimestamp;
                    if (t < 10) return Constants.shooterRPM;
                    else return 0;
                }),
                new RunIndexer(() -> {
                    double t = BotContainer.timestamp() - initialTimestamp;
                    if (t < 3) return 0;
                    else if (t < 10) return Constants.indexerPower;
                    else return 0;
                })
        );
    }
}
