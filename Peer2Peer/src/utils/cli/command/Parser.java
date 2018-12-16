package utils.cli.command;

import javafx.util.Pair;

import java.util.*;

import java.util.HashMap;

public class Parser {

    public static Command parseQuery(String input) throws Exception {
        Scanner sc = new Scanner(input);
        if (!sc.hasNext())
            throw new Exception("BAD_QUERY");
        String mainCommand = sc.next();
        CommandType commandType;
        if ((commandType = CommandType.parse(mainCommand)) == null)
            throw new Exception("No command " + mainCommand + " found");

        String flags = input.substring(mainCommand.length(), input.length());
        flags = flags.replaceFirst("[\\s|\\S]*?[-]", "-");
        System.out.println(flags);

        HashMap<String, String> flagMap = getFlagInput(flags);

        validate(commandType, flagMap);

        return Command.getCommand(commandType, flagMap);

    }

    private static void validate(CommandType type, HashMap<String, String> flagMap) throws Exception {
        for (String key : type.getRequiredFlags()) {
            if (!flagMap.containsKey(key))
                throw new Exception("Missing flag '" + key + "'");
        }
    }

    private static HashMap<String, String> getFlagInput(String input) {

        HashMap<String, String> map = new HashMap<>();

        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        Scanner flagDetector = new Scanner(input);
        flagDetector.useDelimiter("[-]");
        while (flagDetector.hasNext()) {
            Pair<String, String> flag = handleSingleFlag(flagDetector.next());
            if (flag == null)
                continue;
            map.put(flag.getKey(), flag.getValue());
        }
        return map;
    }

    private static Pair<String, String> handleSingleFlag(String input) {
        if ("".equals(input))
            return null;
        Scanner sc = new Scanner(input);
        if (!sc.hasNext())
            return null;
        String key = sc.next();
        String value = "";
        if (sc.hasNext())
            value = sc.next();
        return new Pair<>(key, value);
    }

//    public static void main(String[] args) throws Exception {
//        Scanner sc = new Scanner(System.in);
//        while (true) {
//            try {
//                parseQuery(sc.nextLine());
//
//            } catch (Exception e) {
//                System.err.println(e.getMessage());
//            }
//        }
//    }
}
