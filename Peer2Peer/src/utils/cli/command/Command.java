package utils.cli.command;

import java.util.HashMap;

public abstract class Command {
    private CommandType type;

    Command(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }

    static Command getCommand(CommandType type, HashMap<String, String> flags) {
        switch (type) {
            case SERVE:
                return new ServeCommand(flags.get("path"), flags.get("name"));
            case RECEIVE:
                return new ReceiveCommand(flags.get("name"));
            default:
                return null;
        }
    }
}
