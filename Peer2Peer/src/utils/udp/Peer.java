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
        File file = new File( new File("").getAbsolutePath() + "/Peer2Peer/src/utils/files");
        if(!file.exists())
            file.mkdir();
        while (true) {
            Command cmd = commander.read();

            if (cmd instanceof ServeCommand) {

                String ip = ((ServeCommand) cmd).getIp();

                byte[] send = ("sending " + ((ServeCommand) cmd).getPath() +((ServeCommand) cmd).getName()).getBytes();
                DatagramPacket pack = new DatagramPacket(send, send.length, InetAddress.getByName(ip), 55555);
                socket.send(pack);
                byte[] buffer = new byte[64000];
                File t = new File(file.getAbsolutePath() + "/" + ((ServeCommand) cmd).getName());
                if(!t.exists()){
                    System.out.println("No such File Available!");
                    continue;
                }
                FileInputStream fileInputStream = new FileInputStream(t);

                int x = fileInputStream.read(buffer);
                DatagramPacket packet;
                while (x == 64000) {
                    packet = new DatagramPacket(buffer, x, InetAddress.getByName(ip), 55555);
                    socket.send(packet);
                    x = fileInputStream.read(buffer);
                }
                packet = new DatagramPacket(buffer, x, InetAddress.getByName(ip), 55555);
                socket.send(packet);

                byte[] end = "end!".getBytes();
                DatagramPacket endpacket = new DatagramPacket(end, end.length, InetAddress.getByName(ip), 55555);

                fileInputStream.close();

            } else if (cmd instanceof ReceiveCommand) {

                //Broadcast Message
                InetAddress broadcast = InetAddress.getByName("255.255.255.255");
                String fileName = ((ReceiveCommand) cmd).getName();
                byte[] name = fileName.getBytes();
                DatagramPacket packet = new DatagramPacket(name, name.length, broadcast, 55555);
                socket.send(packet);

                boolean found = false;
                InetAddress address = null;
                byte[] ans = new byte[3];
                DatagramPacket temp = new DatagramPacket(ans, ans.length);

                while (!found) {
                    socket.receive(temp);
                    System.out.println("GOT " + new String(ans));
                    System.out.println(temp.getAddress().getHostName());
                    String str = new String(ans);
                    if (str.equals("OK!")) {
                        found = true;
                        address = temp.getAddress();
                        ans = "OK!".getBytes();
                        temp = new DatagramPacket(ans, ans.length, address, 55555);
                        socket.send(temp);
                    }
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(file + "/" + fileName));
                byte[] buffer = new byte[64000];
                DatagramPacket DP = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    socket.receive(DP);
                    System.out.println(DP.getAddress().getHostName());
                    System.out.println(DP.getPort());
                    if (!DP.getAddress().getHostName().equals(address.getHostName()) && new String(DP.getData(),0,DP.getLength()).equals("OK!")) {
                        byte[] rej = "NOK".getBytes();
                        DatagramPacket reject = new DatagramPacket(rej, rej.length, DP.getAddress(), DP.getPort());
                        socket.send(reject);
                    } else if (new String(DP.getData(),0,DP.getLength()).equals("end!")) {
                        writer.close();
                        break;
                    } else if (new String(DP.getData(),0,DP.getLength()).equals("NOK")) {

                    } else {
                        writer.write(new String(DP.getData(),0,DP.getLength()));
                        writer.flush();
                        System.out.println("wrote!");
                        System.out.println(new String(DP.getData(),0,DP.getLength()));
                    }
                }

            }

        }
    }


    public static void main(String[] args) throws IOException {
        new Peer();
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
