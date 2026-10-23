package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.PathBuilder;
import org.firstinspires.ftc.teamcode.autonomous.Pose;

/**
 * Default autonomous command.
 */
public class AutoCommand extends ParallelCommandGroup {
    private final Bot.Alliance alliance;

    /**
     * Creates the auto command.
     * @param alliance The alliance of the robot
     */
    public AutoCommand(Bot.Alliance alliance) {
        this.alliance = alliance;
//        addCommands(new AutoDrive(new PathBuilder(0, 0, 0)
//                .moveTo(20, 0, 0, 1)
//                .moveTo(20, 20, 0, 1)
//                .moveTo(0, 20, 0, 1)
//                .moveTo(0, 0, 0, 1)
//                .build()
//        )); // add the drive command
        addCommands(new AutoDrive(new PathBuilder(0, 0, 0)
                .moveTo(36, 0, 0, 0)
                .stall(3000).build()
        )); // add the drive command

        addCommands(new WaitCommand(5) // add other commands like this
                .andThen(new WaitCommand(5))
                .andThen(new WaitCommand(5))
                .andThen(new WaitCommand(5))
                .andThen(new WaitCommand(5))
                .andThen(new WaitCommand(5))
        ); // other commands
    }
}
