import com.fazecast.jSerialComm.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.*;

public class Communicator implements SerialPortMessageListener {
    public static final int BAUD_RATE = 115200;

    public static final byte[] SERVO_UP = {(byte) 10, (byte) 5, (byte) 115, (byte) 101, (byte) 114, (byte) 118, (byte) 111, (byte) 4, (byte) 232, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 0};
    public static final byte[] SERVO_DOWN = {(byte) 10, (byte) 5, (byte) 115, (byte) 101, (byte) 114, (byte) 118, (byte) 111, (byte) 4, (byte) 164, (byte) 6, (byte) 1, (byte) 2, (byte) 1, (byte) 0};

    private SerialPort port = null;
    private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
    private Message response = null;
   
    public Communicator() throws Exception {
        System.out.println("Connecting to blot...");
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length > 0 && ports[0].getSystemPortName().equals("COM3")) {
            port = ports[0];
            port.setBaudRate(BAUD_RATE);
            port.openPort();
            port.addDataListener(this);
            System.out.println("Successfuly connected to " + port.getSystemPortName());
        } else {
            throw new Exception("Connection to serial port failed");
        }
    }

    @Override 
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public byte[] getMessageDelimiter() {
        return new byte[] {0x0a};
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        System.out.println("Incoming message...");
        byte[] data = event.getReceivedData();
        Message message = null;
        try {
            message = unpack(data);
        } catch (Exception e) {
            System.out.println("\tERROR reading data");
            return;
        }
        System.out.print("\tRECIEVED \"" + message.message + "\" [");
        for (byte b : message.payload) {
            System.out.print((int) b + " ");
        }
        System.out.println("]");
        this.response = message;
    }

    public void flushQueue() throws Exception {
        while (!queue.isEmpty()) {
            Message message = queue.poll();
            send(message);
            while (response == null) {
                Thread.sleep(200);
            }
            response = null;
        }
    }

    public static byte[] pack(Message message) throws Exception {
        String messageString = message.message;
        byte[] payload = message.payload;
        int messageCount = message.messageCount;

        ByteArrayOutputStream output = new ByteArrayOutputStream(1 + messageString.length() + 1 + payload.length + 1);
        if (messageString.length() > 255) {
            throw new Exception("Message length too long (" + messageString.length() + ")");
        }
        output.write(messageString.length());
        output.write(messageString.getBytes());
        
        if (payload.length > 255) {
            throw new Exception("Payload length too long (" + payload.length + ")");
        }
        output.write(payload.length);
        output.write(payload);        

        output.write(messageCount);

        return output.toByteArray();
    }

    public static Message unpack(byte[] bytes) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);

        int messageLength = input.read();
        byte[] messageBuffer = new byte[messageLength];
        input.read(messageBuffer);
        String messageString = new String(messageBuffer, StandardCharsets.US_ASCII);

        int payloadLength = input.read();
        byte[] payload = new byte[payloadLength];
        input.read(payload);

        int messageCount = input.read();

        return new Message(messageString, payload, messageCount);
    }

    public void send(Message message) throws Exception {
        byte[] packedMessage = pack(message);
        System.out.print("Packed message: [");
        for (byte b : packedMessage) {
            System.out.print((int) (b & 0xff) + " ");
        }
        System.out.println("]");
        packedMessage = COBS_encode(packedMessage);
        System.out.print("COBS encoded: [");
        for (byte b : packedMessage) {
            System.out.print((int) (b & 0xff) + " ");
        }
        System.out.println("]");

        write(packedMessage);
    }

    public void write(byte[] buffer) {
        port.writeBytes(buffer, buffer.length);
    }

    public static Message servo(int angle) throws Exception {
        byte[] bytes = intsToBytes(new int[] {angle});
        return new Message("servo", bytes, 0);
    }

    public static Message goTo(float x, float y) throws Exception {
        byte[] bytes = floatsToBytes(new float[] {x, y});
        return new Message("go", bytes, 0);
    }

    public static byte[] intsToBytes(int[] array) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4 * array.length);
        for (int i : array) {
            byte[] buffer = new byte[4];
            buffer[0] = (byte) i;
            buffer[1] = (byte) (i >> 8);
            buffer[2] = (byte) (i >> 16);
            buffer[3] = (byte) (i >> 24);
            output.write(buffer);
        }
        return output.toByteArray();
    }

    public static byte[] floatsToBytes(float[] array) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4 * array.length);
        for (float f : array) {
            byte[] buffer = new byte[4];
            int intBits = Float.floatToIntBits(f);
            buffer[0] = (byte) intBits;
            buffer[1] = (byte) (intBits >> 8);
            buffer[2] = (byte) (intBits >> 16);
            buffer[3] = (byte) (intBits >> 24);
            output.write(buffer);
        }
        return output.toByteArray();
    }

    public static byte[] COBS_encode(byte[] buffer) {
        ArrayList<Byte> output = new ArrayList<Byte>();
        output.add((byte) 0x00);
        int pointer_index = 0;
        byte pointer_val = 0x01;

        for (byte b : buffer) {
            if (b == 0) {
                output.set(pointer_index, pointer_val);
                pointer_index = output.size();
                output.add((byte) 0x00);
                pointer_val = 0x01;
            } else {
                output.add(b);
                pointer_val += 1;
                if (pointer_val == 0xff) {
                    output.set(pointer_index, pointer_val);
                    pointer_index = output.size();
                    output.add((byte) 0x00);
                    pointer_val = 0x01;
                }
            }
        }
        output.set(pointer_index, pointer_val);
        output.add((byte) 0x00);

        byte[] bytearray = new byte[output.size()];
        for (int i = 0; i < output.size(); i++) {
            bytearray[i] = output.get(i);
        }
        return bytearray;
    }

    public static byte[] COBS_decode(byte[] buffer) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int i = 0; i < buffer.length; ) {
            byte next0 = buffer[i++];
            for (int j = 1; j < (next0 & 0xff); j++) {
                output.write(buffer[i++]);
            }
            if (next0 < 0xff && i < buffer.length) {
                output.write((byte) 0);
            }
        }
        return output.toByteArray();
    }

    private static class Message {
        public String message;
        public byte[] payload;
        public int messageCount;

        public Message(String message, byte[] payload, int messageCount) {
            this.message = message;
            this.payload = payload;
            this.messageCount = messageCount;
        }
    }

    public static void main(String[] arg0) throws Exception {
        JFrame frame = new JFrame();
        frame.setTitle("This is just to keep the program running");
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JButton("Hello, world!"));
        frame.pack();
        Thread comThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Communicator com = new Communicator();
                    com.queue.add(new Message("setOrigin", new byte[] {}, 0));
                    com.queue.add(new Message("motorsOn", new byte[] {}, 0));
                    com.queue.add(goTo(25f, 25f));
                    com.queue.add(servo(1700));
                    com.queue.add(goTo(25f, 100f));
                    com.queue.add(goTo(100f, 100f));
                    com.queue.add(goTo(100, 25f));
                    com.queue.add(goTo(25f, 25f));
                    com.queue.add(new Message("motorsOff", new byte[] {}, 0));
                    com.queue.add(servo(1000));
                    com.flushQueue();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        comThread.run();
    }
}