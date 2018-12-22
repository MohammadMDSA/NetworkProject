package utils.cli.command;

public class ServeCommand extends Command {

    private String name;
    private String path;
    private String ip;

    ServeCommand(String ip, String path, String name) {
        super(CommandType.SERVE);
        this.name = name;
        this.path = path;
        this.ip = ip;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
