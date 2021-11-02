import java.io.IOException;
import java.net.*;
import java.util.Vector;

class UDPMultiCastReceiver extends Thread {
    static final int MAX = 1;
    private Vector messages = new Vector();

    Terminal terminal;
    UDPMultiCastReceiver(Terminal t)
    {
        terminal = t;
    }
    @Override
    public void run()
    {
        try{
                while (true) {

                    putMessage();

                    sleep(2000);
                }


        }catch(InterruptedException E){}
    }


    public synchronized void putMessage() throws InterruptedException {
        int mcPort = 18777;
        MulticastSocket mcSocket=null;

        String msg;
        try {
            InetAddress mcIPAddress = InetAddress.getByName("230.1.1.1");


            try {
                mcSocket = new MulticastSocket(mcPort);
                terminal.println("Multicast Receiver running at:"
                        + mcSocket.getLocalSocketAddress());

                mcSocket.joinGroup(mcIPAddress);

                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);


                    terminal.println("Waiting for a multicast message...");
                    mcSocket.receive(packet);
                    msg = new String(packet.getData(),
                            packet.getOffset(),
                            packet.getLength());
                    terminal.println(msg.substring(5));
                    while (messages.size() == MAX)
                        wait();
                    messages.addElement(packet);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(UnknownHostException E){}


    }




    public synchronized DatagramPacket getMessage() throws InterruptedException
    {

        while(messages.size()==0)
            wait();
        DatagramPacket msg = (DatagramPacket) messages.firstElement();
        messages.removeElement(msg);
        return msg;
    }

}
public class Server extends Thread
{
    int topicNo=0;

    UDPMultiCastReceiver send;
    int c=0;
    int mcPort = 50001;
    Terminal terminal;
    String s;
    Server() throws UnknownHostException {

    }
    Server(UDPMultiCastReceiver s, Terminal t,String str) throws UnknownHostException {
        send = s;
        terminal = t;
        this.s = str;
    }

    InetAddress mcIPAddress = InetAddress.getByName("239.255.255.255");
    @Override
    public void run()
    {
        try {
            while(true)
            {

                sendMessage();
                sleep(2000);}

            }catch (InterruptedException e){}
        }




    public synchronized void sendMessage()
    {

        try( DatagramSocket socket = new DatagramSocket();) {

            DatagramPacket msg = send.getMessage();

            msg.setAddress(mcIPAddress);
            msg.setPort(mcPort);
            terminal.println("Routing the message to Dashboard");
            socket.send(msg);

            sleep(1000);

        }catch( Exception e){e.printStackTrace();};

    }


    public static void main(String args[]) throws UnknownHostException {
        Terminal ob2 = new Terminal("Server");

        UDPMultiCastReceiver ob = new UDPMultiCastReceiver(ob2);
        ob.start();
        new Server(ob,ob2,"239.255.255.255").start();
        new getReceipt(ob2,"239.255.255.254").start();
        new getReceipt(ob2,"239.255.255.250").start();

    }
}
 class getReceipt extends Thread
{

    DatagramSocket socket =null;
    packet pck = new packet();
    int mcPort = 50001;
    MulticastSocket mcSocket=null;
    Terminal t;
    String str;
    getReceipt(Terminal t,String str)
    {
        this.t=t;
        this.str=str;
    }
    public void run()
    {
        while(true){
            try {
                receive();


                sleep(1000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void receive() throws IOException {
        mcSocket = new MulticastSocket(mcPort);
        InetAddress mcIPAddress = InetAddress.getByName(str);

        mcSocket.joinGroup(mcIPAddress);

        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        packet.setAddress(mcIPAddress);
        packet.setPort(mcPort);



        mcSocket.receive(packet);
        String rec = new String(packet.getData(),0,packet.getLength());
        System.out.println(rec);
        byte [] msg = packet.getData();
        if(msg[0]==0) {
            OnReceipt(packet);
        }
        else
            OnReceipt(rec);


    }
    public void OnReceipt(DatagramPacket packet) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        pck.sendAck2(packet,t,socket)

;

    }
    public  void OnReceipt(String str) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        pck.sendMsg(str,t,socket);
    }

}
