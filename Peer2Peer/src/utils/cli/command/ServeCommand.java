package utils.cli.command;

public class ServeCommand extends Command {

    private String name;
    private String path;

    ServeCommand(String path, String name) {
        super(CommandType.SERVE);
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
