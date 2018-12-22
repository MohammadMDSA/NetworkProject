import utils.cli.Cli;
import utils.cli.command.Command;

public class Main {

    public static void main(String[] args) {
       // System.out.println("sending hello.txt".substring(9));
        Cli cli = new Cli();
        while(true) {
            Command x = cli.read();
            System.out.println(x.getType());
        }
    }
}
