package utils.udp;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

public class Service extends Thread {

    DatagramSocket connecton;

    Service(DatagramSocket socket){
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

                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new FileWriter("../files/" + cmd.substring(8)));

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
                        //must send file by divide in to 64000 bytes.
                    } else{
                        byte[] bytes = "No such file!".getBytes();
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
