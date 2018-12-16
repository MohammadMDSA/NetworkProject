package utils.cli.command;

public class ReceiveCommand extends Command {

    private String name;

    ReceiveCommand(String name) {
        super(CommandType.RECEIVE);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
