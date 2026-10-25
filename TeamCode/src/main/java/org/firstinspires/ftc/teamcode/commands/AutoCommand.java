package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
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

    public class ResetTimestamp extends CommandBase {
        @Override
        public void execute() {
            if (initialTimestamp == -1)
                initialTimestamp = BotContainer.timestamp();
        }
    }

    /**
     * Creates the auto command.
     * @param alliance The alliance of the robot
     */
    public AutoCommand(Bot.Alliance alliance) {
        addCommands(new ResetTimestamp());

        this.alliance = alliance;
//        addCommands(new AutoDrive(new PathBuilder(0, 0, 0)
//                .moveTo(0, 72, Math.PI / 2, 3)
//                .moveTo(0, 72, 0, 1)
//                .stall(30000)
//                .build()
//        )); // add the drive command
        addCommands(new AutoDrive(new PathBuilder(0, 0, 0)
                .stall(20)
                .moveTo(0, -12, 0, 1)
                .moveTo(60, -120, 0, 7)
                .build()
        ));

        addCommands(
                new ShootPID(() -> {
                    double t = BotContainer.timestamp() - initialTimestamp;
                    if (t < 20) return Constants.shooterRPM;
                    else return 0;
                }),
                new RunIndexer(() -> {
                    double t = BotContainer.timestamp() - initialTimestamp;
                    if (t < 6.7) return 0;
                    else return Constants.indexerPower;
                })
        );
    }
}
