package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name = "drivetrain test")
public class DrivetrainTest extends CommandOpMode {
    private Robot bot;

    @Override
    public void initialize() {
        Constants.hardwareMap = hardwareMap;
        Constants.telemetry = telemetry;
        Constants.pilot = new GamepadEx(gamepad1);
        Constants.copilot = new GamepadEx(gamepad2);

        bot = new Bot(Bot.OpMode.NONE, Bot.Alliance.BLUE, Bot.Config.DRIVETRAIN_TEST);
    }
}
