package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name = "encoder drive", group = "test")
public class EncoderDrive extends CommandOpMode {
    private Robot bot;

    @Override
    public void initialize() {
        Constants.hardwareMap = hardwareMap;
        Constants.telemetry = telemetry;
        Constants.pilot = new GamepadEx(gamepad1);
        Constants.copilot = new GamepadEx(gamepad2);
        Constants.AutoConstants.neutralDrivetrain = true;

        bot = new Bot(Bot.OpMode.TELEOP, Bot.Alliance.BLUE, Bot.Config.ENCODER_DRIVE);
    }
}
