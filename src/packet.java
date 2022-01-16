import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class packet {

    static int PacketSize = 1400;
    static final int pubPort = 18777;
    static final int subPort = 50001;
    static final byte ACK = 0;
    static final byte SENDMESG = 2;
    static final byte NTOPIC=6;
    static final byte PUBLICATION = 3;


    protected DatagramPacket createPackets(int type, int topicNumber,String message) {

        byte[] messageArray = message.getBytes();
        byte[] data = new byte[PacketSize];
        data[0] = (byte) type;
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(topicNumber);
        byte[] topicNumberArray = byteBuffer.array();
        int len = message.length();
        data[1]=(byte)len;
        for (int i = 0; i < 4; i++) {
            data[i + 2] = topicNumberArray[i];
        }
        for (int i = 0; i < messageArray.length && i < PacketSize; i++) {
            data[i + 6] = messageArray[i];
        }
        DatagramPacket packet = new DatagramPacket(data, 0, data.length);

        return packet;
    }
    protected void setType(byte[] data, byte type)
    {
        data[0] = type;
    }


   protected void send(DatagramPacket receivedPacket, Terminal terminal, DatagramSocket socket,byte type ) throws UnknownHostException {

        InetAddress mcIPAddress = InetAddress.getByName("239.255.255.254");
        byte[] data = receivedPacket.getData();
        setType(data, type);
        DatagramPacket ack = new DatagramPacket(data, data.length);
        ack.setAddress(mcIPAddress);
        ack.setPort(subPort);
        try {
            socket.send(ack);
            //terminal.println("Sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMsg(String str, Terminal terminal, DatagramSocket socket ) throws UnknownHostException {

        InetAddress mcIPAddress = InetAddress.getByName("224.1.1.1");
        byte [] data = str.getBytes();

        DatagramPacket ack = new DatagramPacket(data, data.length);
        ack.setAddress(mcIPAddress);
        ack.setPort(18777);
        try {
            socket.send(ack);
            terminal.println("Routed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendAck2(DatagramPacket receivedPacket, Terminal terminal, DatagramSocket socket ) throws UnknownHostException {
        InetAddress mcIPAddress = InetAddress.getByName("224.1.1.1");
        byte[] data = receivedPacket.getData();
        setType(data, ACK);
        DatagramPacket ack = new DatagramPacket(data, data.length);
        ack.setAddress(mcIPAddress);
        ack.setPort(pubPort);
        try {
            socket.send(ack);
            terminal.println("Sent ACK.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
