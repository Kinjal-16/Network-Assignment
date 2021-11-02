import javax.xml.crypto.Data;
import java.net.*;
import java.util.HashMap;

class User extends Thread {
        Server ob = new Server();
        HashMap<Integer,String> map=new HashMap<Integer,String>();
        Terminal terminal;
        int mcPort = 18777;
        String mcIPStr = "230.1.1.1";
        User(Terminal t) throws UnknownHostException {
            terminal=t;
        }
        packet pck=new packet();
    int c=1;
        @Override
        public void run() {

            try {
                while (true) {

                    int n=Integer.parseInt(terminal.read("1. Create new topic \n 2. Publish Message"));
                    terminal.println("Entered:"+Integer.valueOf(n));
                    terminal.println("Enter a name for your topic");
                    String key="";
                    switch(n){
                        case 1:
                            String str=terminal.read("Input");
                            map.put(c,str);
                            terminal.println("You have Entered:"+str);
                            DatagramPacket packet2=pck.createPackets(pck.NTOPIC,c,str);;
                            send(packet2);
                            terminal.println("Packet created");
                            c++;
                            sleep(1000);
                            continue;
                        case 2:
                            terminal.println(map.toString());
                            terminal.println("Type the respective key for the topic");
                            key=terminal.read("Enter");
                            terminal.println("Enter a message");
                            String data = terminal.read("Enter: ");
                            terminal.println("You: " + data);
                            DatagramPacket packet4 = pck.createPackets(pck.PUBLICATION,Integer.parseInt(key),data);;
                            send(packet4);
                            sleep(1000);

                    }

                }

            } catch (Exception e) {
            }
        }

        public synchronized void send(DatagramPacket packet) {


            try (DatagramSocket udpSocket = new DatagramSocket();) {

                InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);

                packet.setAddress(mcIPAddress);
                packet.setPort(mcPort);
                udpSocket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void main(String args[]) throws UnknownHostException {
            Terminal t = new Terminal("Teacher");


            temp ob2 = new temp(t);
            User ob = new User(t);
            ob.start();
            ob2.start();


        }
    }





