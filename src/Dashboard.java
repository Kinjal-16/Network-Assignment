import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends  Thread {
    static Dashboardsend ob2;
    static final int DEFAULT_PORT = 50001;
    int c=0;
    HashMap<Integer,String> map=new HashMap<Integer,String>();

    private byte[] buf = new byte[256];
    private MulticastSocket socket;
    packet ob = new packet();

    Terminal terminal = new Terminal("Chatbot");
    DatagramPacket packet;
    InetAddress mcIPAddress;
    Dashboard(int port) {
        try {
             socket = new MulticastSocket(port);

        }
        catch(java.lang.Exception e) {e.printStackTrace();}
    }

    public void run()
    {
        try
        {
            mcIPAddress = InetAddress.getByName("239.255.255.255");
            socket.joinGroup(mcIPAddress);

                    while(true) {
            putMessage();
            sleep(1000);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void putMessage() throws InterruptedException
    {




        try
        {

                packet = new DatagramPacket(new byte[1024], 1024);
                packet.setPort(DEFAULT_PORT);
                packet.setAddress(mcIPAddress);
                socket.receive(packet);
                String rec = new String(packet.getData(),0,packet.getLength());
                //System.out.println(rec);
                OnReceipt();
        }catch (Exception e) {
        e.printStackTrace();
    }

    }
    public void OnReceipt() throws SocketException, UnknownHostException
    {           DatagramSocket soc;
                byte msg[]=packet.getData();
                int d=1;
                String rec = new String(packet.getData(),0,packet.getLength());
                switch(msg[0]) {

                    case 6:
                         c=0;
                         d=1;
                        for(int i=3;i>=0;i--)
                        {
                            c = c+(d*msg[i+1]);
                            d=d*10;
                        }
                        map.put(c,rec.substring(5));
                        break;
                    case 3:
                        soc=new DatagramSocket();
                         c=0;
                         d=1;
                        for(int i=3;i>=0;i--)
                        {
                            c = c+(d*msg[i+1]);
                            d=d*10;
                        }
                        if(ob2.list.contains(c))
                        {
                            terminal.println("Message Received: "+rec.substring(5));
                            ob.send(packet, terminal, soc, ob.ACK);

                        }
                }
    }



    public static void main(String[] args)
    {
        try {


                Dashboard ob = new Dashboard(DEFAULT_PORT);
                ob.start();
                ob2=new Dashboardsend(ob);
                ob2.start();

        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
