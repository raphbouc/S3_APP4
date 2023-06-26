import java.io.*;
import java.net.*;
public class CouchePhysique extends Couche {
    int port = 0;
    public int delay = 0;
    public int delayerreur = -1;
    public int packetenvoye = 0;
    InetAddress addressIP = null;
    protected ReceptionThread thread;
    private static CouchePhysique instance;
    private CouchePhysique(){

    };
    static public CouchePhysique getInstance(){
        if (instance == null){
            instance = new CouchePhysique();
        }
        return instance;
    }
    public void setDestAddresseIP(InetAddress address){
        try {
            this.addressIP = address;
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public void setDestAddresseIp(String addresse){
        try{
            this.addressIP = InetAddress.getByName(addresse);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
    }
    public void setDestPort(int port){
        this.port = port;
    }
    public void start(){
        thread.running = true;
        thread.start();
    }
    public void stop(){
        thread.running = false;
        thread.interrupt();
    }
    public boolean threadRunning(){
        return thread.running;
    }
    @Override
    protected void getFromUp(byte[] PDU) {
        DatagramSocket Dsocket = null;
        try {
            Dsocket = new DatagramSocket();
        } catch (SocketException exception){
            exception.printStackTrace();
        }
        packetenvoye++;
        if(packetenvoye == delayerreur) {
            PDU[10] <<= 1;
        }
        DatagramPacket Dpacket = new DatagramPacket(PDU, PDU.length, addressIP, port);
        try {
            Dsocket.send(Dpacket);
            Thread.sleep(delay);
        } catch (IOException | InterruptedException exception){
            exception.printStackTrace();
        }
    }

    @Override
    protected void getFromDown(byte[] PDU) throws TransmissionErrorException {
        sendUp(PDU);
    }
    public void setReceptionThread(int port) throws IOException{
        this.thread = new ReceptionThread(port, this);
    }

    private class ReceptionThread extends Thread{
        protected  DatagramSocket Dsocket = null;
        private CouchePhysique PhysLayer;
        public boolean running = true;

        public ReceptionThread(int port, CouchePhysique PhysLayer) throws IOException{
            super("PhysicalLayer ReceptionThread" + Math.random());
            Dsocket = new DatagramSocket(port);
            this.PhysLayer = PhysLayer;
        }
        public void run(){
            while (running){
                try {
                        byte[] buffer = new byte[204];
                        DatagramPacket Dpacket = new DatagramPacket(buffer, buffer.length);
                        Dsocket.receive(Dpacket);
                        PhysLayer.getFromDown(Dpacket.getData());
                } catch (IOException | TransmissionErrorException exception){
                    running = false;
                    Dsocket.close();
                    System.out.println(exception.getLocalizedMessage());
                }
            }
            Dsocket.close();
        }
    }

}
