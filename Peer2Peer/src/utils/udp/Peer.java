package utils.udp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Peer {
    String name;

    Peer() throws SocketException {

        DatagramSocket serverSocket = new DatagramSocket(55555);
        Thread thread = new Service(serverSocket);
        thread.start();

    }


}
