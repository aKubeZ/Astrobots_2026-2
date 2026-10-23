package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.Command;

public class Bot extends Robot {
    public Command autoCommand = null;
    public final BotContainer container;
    public final OpMode opMode;

    public enum OpMode {
        TELEOP, AUTO, NONE
    }

    public enum Alliance {
        BLUE, RED
    }

    public enum Config {
        TELE,
        AUTO,
        DRIVETRAIN_TEST,
        LOCALIZATION_TEST,
        LOCALIZED_DRIVE,
        CUSTOM,
    }

    public Bot(OpMode opMode, Alliance alliance, Config config) {
        this.opMode = opMode;
        container = new BotContainer(alliance, config);

        switch (opMode) {
            case TELEOP: {
                initTele();
            } break;
            case AUTO: {
                initAuto();
            } break;
        }
    }

    public void initTele() {
        if (autoCommand != null) autoCommand.cancel();
    }

    public void initAuto() {
        if (autoCommand == null) autoCommand = container.getAutonomousCommand();
        autoCommand.schedule();
    }
}
