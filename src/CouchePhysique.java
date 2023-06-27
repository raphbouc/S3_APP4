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

    /**
     * Modele de conception singleton pour sassurer davoir une seul couche physique
     * @return la couche
     */
    static public CouchePhysique getInstance(){
        if (instance == null){
            instance = new CouchePhysique();
        }
        return instance;
    }

    /**
     * set ladresse ip de destination
     * @param addresse
     */
    public void setDestAddresseIp(String addresse){
        try{
            this.addressIP = InetAddress.getByName(addresse);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * set le port de destination
     * @param port
     */
    public void setDestPort(int port){
        this.port = port;
    }

    /**
     * commencer le thread
     */
    public void start(){
        thread.running = true;
        thread.start();
    }

    /**
     * terminer le thread
     */
    public void stop(){
        thread.running = false;
        thread.interrupt();
    }

    /**
     * regarder si le thread est lancer
     * @return vrai ou faux selon si le thread est lancer ou pas
     */
    public boolean threadRunning(){
        return thread.running;
    }

    /**
     * 
     * @param PDU
     */
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
