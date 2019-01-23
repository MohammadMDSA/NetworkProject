import utils.cli.Cli;
import utils.udp.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        Cli cli = new Cli();

        new Peer();
        new Peer();
        byte[] t = new byte[4000];

        FileInputStream s = new FileInputStream(new File(new File("").getAbsolutePath() + "/Peer2Peer/src/utils/files/hello.txt"));

        s.read(t);
        for (int i = 0; i < 30; i++) {
            System.out.println((int)t[i]);
        }
    }
}
