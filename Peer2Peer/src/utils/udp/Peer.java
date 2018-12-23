package utils.udp;

import utils.cli.Cli;
import utils.cli.command.Command;
import utils.cli.command.ConnectCommand;
import utils.cli.command.ReceiveCommand;
import utils.cli.command.ServeCommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Vector;

public class Peer {

    private InetAddress address = InetAddress.getLocalHost();
    private DatagramSocket socket = new DatagramSocket();
    private Vector<String> ips = new Vector<>();
    private Thread thread;



    public Peer() throws IOException {

        DatagramSocket serverSocket = new DatagramSocket(55555);
        thread = new Service(serverSocket);
        thread.start();
        clientService();
    }


    public void clientService() throws IOException {
        Cli commander = new Cli();
        while (true){
            Command cmd = commander.read();

            if(cmd instanceof ServeCommand){

                int index = -1;
                String ip = ((ServeCommand) cmd).getIp();
                for (int i = 0; i < ips.size(); i++) {
                    if(ips.get(i).equals(ip)){
                        index = i;
                        break;
                    }
                }

                if(index == -1){
                    ips.add(ip);

                    index = ips.size() - 1;
                }

                byte[] buf = new byte[64000];
                FileInputStream fileInputStream = new FileInputStream(new File("../files/" + ((ServeCommand) cmd).getName()));
                int x = fileInputStream.read(buf);
                DatagramPacket packet;
                while(x == 64000){
                    packet = new DatagramPacket(buf, x, InetAddress.getByName(ips.get(index)), 55555);
                    socket.send(packet);
                    x = fileInputStream.read(buf);
                }
                packet = new DatagramPacket(buf, x, InetAddress.getByName(ips.get(index)), 55555);
                socket.send(packet);

                // send to specific IP

            } else if(cmd instanceof ReceiveCommand){

                //Broadcast and get answer from first interface contains file

            }else if(cmd instanceof ConnectCommand){
                String ip = ((ConnectCommand) cmd).getIp();
                ips.add(ip);
            }

        }
    }


    public InetAddress getAddress() {
        return address;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public Vector<String> getIps() {
        return ips;
    }

    public Thread getThread() {
        return thread;
    }
}
