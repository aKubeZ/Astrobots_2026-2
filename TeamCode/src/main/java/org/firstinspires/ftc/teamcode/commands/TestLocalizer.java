package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.BotContainer;
import org.firstinspires.ftc.teamcode.autonomous.Localizer;

public class TestLocalizer extends CommandBase {
    private final Localizer localizer = Localizer.getInstance();
    private double previousTimestamp;

    @Override
    public void initialize() {
        previousTimestamp = BotContainer.timestamp();
    }

    @Override
    public void execute() {
        double timestamp = BotContainer.timestamp();
        localizer.update(previousTimestamp - timestamp);
        previousTimestamp = timestamp;
    }
}
