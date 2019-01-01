package utils.udp;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Service extends Thread {

    DatagramSocket connecton;

    public Service(DatagramSocket socket){
        connecton = socket;
    }



    @Override
    public void run() {
        while(true){
        byte[] buf = new byte[64000];
        DatagramPacket DP = new DatagramPacket(buf,buf.length);
            try {

                connecton.receive(DP);
                InetAddress address = DP.getAddress();
                int port = DP.getPort();
                String cmd = new String(DP.getData(),0, DP.getLength());
                if(cmd.substring(0,7).equals("sending")){
                    String[] str = cmd.split(" ");

                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new FileWriter(str[1] + "\\" + str[2]));

                    while (true){
                        connecton.receive(DP);
                        cmd = new String(DP.getData(),0,4);
                        if(cmd.equals("end!"))
                            break;
                        else {
                            bufferedWriter.write(new String(DP.getData()));
                            bufferedWriter.flush();
                        }
                    }


                } else{
                    File file = new File("../files/" + cmd);
                    DatagramPacket ans;
                    if(file.exists()){
                        byte[] found = "OK!".getBytes();
                        ans = new DatagramPacket(found, 0, found.length, connecton.getInetAddress(), connecton.getPort());
                        connecton.send(ans);

                        byte[] buff = new byte[3];
                        DatagramPacket temp = new DatagramPacket(buff, buff.length);
                        connecton.receive(temp);

                        String send = new String(buff);
                        if(send.equals("NOK"))
                            continue;

                        FileInputStream fileInputStream = new FileInputStream(file);
                        int x = fileInputStream.read(buf);
                        while(x == 64000){
                            ans = new DatagramPacket(buf, x, address, port);
                            connecton.send(ans);
                            x = fileInputStream.read(buf);
                        }
                        ans = new DatagramPacket(buf, x, address, port);
                        connecton.send(ans);

                        byte[] end = "end!".getBytes();
                        ans = new DatagramPacket(end, end.length, address, port);
                        connecton.send(ans);

                    } else{
                        byte[] bytes = "NOK".getBytes();
                        ans = new DatagramPacket(bytes, bytes.length, address, port);
                        connecton.send(ans);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
