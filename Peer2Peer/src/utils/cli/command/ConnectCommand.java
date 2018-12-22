package utils.cli.command;

public class ConnectCommand extends Command {
    private String ip;
    private int port;

    ConnectCommand(String ip, String port) {
        super(CommandType.CONNECT);
        this.ip = ip;
        this.port = Integer.valueOf(port);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
