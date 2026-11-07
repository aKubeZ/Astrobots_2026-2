package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.pid.PIDData;

/*
IMPORTANT NOTE IF SOMEONE THAT ISN'T ME IS GOING TO USE THIS OP MODE IN A MATCH
FOR SOME REASON:
- IT MAKES THE ROBOTS AUTO NONFUNCTIONAL IF YOU INITIALIZE THIS.
 */

@Autonomous(name = "drive pid test", group = "test")
public class DrivePIDTest extends CommandOpMode {
    private Robot bot;

    @Override
    public void initialize() {
        Constants.hardwareMap = hardwareMap;
        Constants.telemetry = telemetry;
        Constants.pilot = new GamepadEx(gamepad1);
        Constants.copilot = new GamepadEx(gamepad2);

        // reset the pid constants:
        Constants.AutoConstants.xyPID = new PIDData(
                RobotConfig.xyKP,
                RobotConfig.xyKI,
                RobotConfig.xyKD
        );
        Constants.AutoConstants.headingPID = new PIDData(
                RobotConfig.headingKP,
                RobotConfig.headingKI,
                RobotConfig.headingKD
        );

        bot = new Bot(Bot.OpMode.AUTO, Bot.Alliance.BLUE, Bot.Config.AUTO);
    }
}
