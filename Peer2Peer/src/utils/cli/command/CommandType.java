package utils.cli.command;

import java.util.ArrayList;
import java.util.Arrays;

public enum CommandType {
    RECEIVE("name"),
    SERVE("ip", "name", "path"),
    CONNECT("ip","port");

    private ArrayList<String> requiredFlags;

    CommandType(String... requiredFlags) {
        this.requiredFlags = new ArrayList<>();
        this.requiredFlags.addAll(Arrays.asList(requiredFlags));
    }

    public ArrayList<String> getRequiredFlags() {
        return new ArrayList<>(this.requiredFlags);
    }

    public static CommandType parse(String input) {
        switch (input.toLowerCase()) {
            case "receive":
                return CommandType.RECEIVE;
            case "serve":
                return CommandType.SERVE;
            default:
                return null;
        }
    }
}
