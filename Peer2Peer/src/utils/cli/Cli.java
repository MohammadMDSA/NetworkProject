package utils.cli;

import utils.cli.command.Command;
import utils.cli.command.Parser;

import java.util.*;

public class Cli {

    private Scanner scanner;

    public Cli()
    {
        this.scanner = new Scanner(System.in);
    }

    public Command read() {
        try {
            return Parser.parseQuery(this.scanner.nextLine());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
