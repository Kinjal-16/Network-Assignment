import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class temp extends Thread {
    Terminal t;
temp(Terminal t)
{
    this.t=t;
}
    public void run()
    {
        while(true)
        {
            Ack();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void Ack() {
        int mcPort= 18777;

        MulticastSocket mcSocket = null;
        byte msg[]=null;
        try {
            mcSocket = new MulticastSocket(mcPort);
            InetAddress mcIPAddress = InetAddress.getByName("224.1.1.1");

            mcSocket.joinGroup(mcIPAddress);

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            packet.setAddress(mcIPAddress);
            packet.setPort(mcPort);


            mcSocket.receive(packet);
            msg=packet.getData();
            String rec = new String(packet.getData(),0,packet.getLength());
            int length = rec.charAt(1);
            switch(msg[0]) {
                case 0:
                    t.println("Sent");
                    break;
                case 2:
                    t.println("Received:\n"+rec.substring(6,(length+6)));

                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    }

