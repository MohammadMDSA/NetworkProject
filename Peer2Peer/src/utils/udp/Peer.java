package utils.udp;

import utils.cli.Cli;
import utils.cli.command.Command;
import utils.cli.command.ReceiveCommand;
import utils.cli.command.ServeCommand;

import java.io.*;
import java.net.*;

public class Peer {


    private static int portNumber = 55555;
    private InetAddress address = InetAddress.getLocalHost();
    private DatagramSocket socket = new DatagramSocket();
    private Thread thread;


    public Peer() throws IOException {

        DatagramSocket serverSocket = new DatagramSocket(portNumber);
        thread = new Service(serverSocket);
        thread.start();
        clientService();
    }


    public void clientService() throws IOException {
        Cli commander = new Cli();
        while (true) {
            Command cmd = commander.read();

            if (cmd instanceof ServeCommand) {

                String ip = ((ServeCommand) cmd).getIp();


                byte[] buffer = new byte[64000];
                FileInputStream fileInputStream = new FileInputStream(new File("../files/" + ((ServeCommand) cmd).getName()));
                int x = fileInputStream.read(buffer);
                DatagramPacket packet;
                while (x == 64000) {
                    packet = new DatagramPacket(buffer, x, InetAddress.getByName(ip), 55555);
                    socket.send(packet);
                    x = fileInputStream.read(buffer);
                }
                packet = new DatagramPacket(buffer, x, InetAddress.getByName(ip), 55555);
                socket.send(packet);

                // send to specific IP

            } else if (cmd instanceof ReceiveCommand) {

                //Broadcast Message
                InetAddress broadcast = InetAddress.getByName("255.255.255.255");
                String fileName = ((ReceiveCommand) cmd).getName();
                byte[] name = fileName.getBytes();
                DatagramPacket packet = new DatagramPacket(name, 0, name.length, broadcast, 55555);
                socket.send(packet);

                boolean found = false;
                InetAddress address = null;
                byte[] ans = new byte[3];
                DatagramPacket temp = new DatagramPacket(ans, ans.length);

                while (!found) {
                    socket.receive(temp);
                    String str = new String(ans);
                    if (str.equals("OK!")) {
                        found = true;
                        address = temp.getAddress();
                    }
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter("../files/" + fileName));
                byte[] buffer = new byte[64000];
                DatagramPacket DP = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    socket.receive(DP);
                    if (!DP.getAddress().getHostName().equals(address.getHostName()) && DP.getData().toString().equals("OK!")) {
                        byte[] rej = "NOK".getBytes();
                        DatagramPacket reject = new DatagramPacket(rej, rej.length, DP.getAddress(), DP.getPort());
                    } else if (buffer.toString().equals("end!")) {
                        break;
                    } else if (DP.getData().toString().equals("NOK")) {

                    } else {
                        writer.write(buffer.toString());
                        writer.flush();
                    }
                }

            }

        }
    }


    public InetAddress getAddress() {
        return address;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public Thread getThread() {
        return thread;
    }
}
