import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboardsend extends Thread {


    int port = 50001;
    Terminal t;
    String mcIPStr = "239.255.255.250";
    Dashboard ob;
    ArrayList<Integer> list = new ArrayList<Integer>();

    Dashboardsend(Dashboard ob) {
        t = ob.terminal;
        this.ob = ob;

    }

    public void run() {
        while (true) {
            try {
                send();
            } catch (IOException e) {
                e.printStackTrace();

            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized void send() throws IOException {
        DatagramSocket sendSocket = new DatagramSocket();


        String tp="";
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
        DatagramPacket pck[]=null;
        String data = t.read("1. Subscribe to a topic \n 2.Unsubscribe from a topic \n 3.Send message to publisher");
        t.println("You: " + data);


        int n=Integer.parseInt(data);
        switch(n){
            case 1:
                packet ob1 = new packet();
                t.println(ob.map.toString());

                     t.println("Enter a topic from the above list");
                     tp=t.read("Input");
                     list.add(Integer.valueOf(tp));
                     t.println("Subscribed");
                 break;

            case 2:

                    t.println(ob.map.toString());
                    t.println("Enter a topic from the above list");
                    tp=t.read("Input");
                    list.remove(Integer.valueOf(tp));
                t.println("Unsubscribed");
                break;

            case 3:
                packet ob3 = new packet();
                t.println("Enter a message");
                tp=t.read("Input");
                t.println("Entered");
                DatagramPacket packet = ob3.createPackets(ob3.SENDMESG,0,tp);;
                packet.setAddress(mcIPAddress);
                packet.setPort(port);
                sendSocket.send(packet);
                t.println("Message sent");
        }





    }

}


