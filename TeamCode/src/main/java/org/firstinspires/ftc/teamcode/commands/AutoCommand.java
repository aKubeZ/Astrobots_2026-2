package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.Constants;
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

        addCommands(new AutoDrive(new PathBuilder(10, 10, 1)
                .moveTo(-20, 20, -1, 3)
                .build()
        ));

//        addCommands(new AutoDrive(new PathBuilder(0, 0, 0)
//                .stall(20)
//                .moveTo(0, -12, 0, 1)
//                .moveTo(60, -120, 0, 7)
//                .build()
//        ));
//
//        addCommands(
//                new ShootPID(() -> {
//                    double t = BotContainer.timestamp() - initialTimestamp;
//                    if (t < 20) return Constants.shooterRPM;
//                    else return 0;
//                }),
//                new RunIndexer(() -> {
//                    double t = BotContainer.timestamp() - initialTimestamp;
//                    if (t < 6.7) return 0;
//                    else return Constants.indexerPower;
//                })
//        );
    }
}
