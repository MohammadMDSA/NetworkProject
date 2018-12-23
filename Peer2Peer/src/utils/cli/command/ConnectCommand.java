package utils.cli.command;

public class ConnectCommand extends Command {
    private String ip;


    ConnectCommand(String ip) {
        super(CommandType.CONNECT);
        this.ip = ip;

    }

    public String getIp() {
        return ip;
    }


}
