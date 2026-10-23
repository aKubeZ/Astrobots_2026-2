package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name = "localization test")
public class LocalizationTest extends CommandOpMode {
    private Robot bot;

    @Override
    public void initialize() {
        Constants.hardwareMap = hardwareMap;
        Constants.telemetry = telemetry;
        Constants.pilot = new GamepadEx(gamepad1);
        Constants.copilot = new GamepadEx(gamepad2);
        Constants.AutoConstants.neutralDrivetrain = true;

        bot = new Bot(Bot.OpMode.AUTO, Bot.Alliance.BLUE, Bot.Config.LOCALIZATION_TEST);
    }
}
