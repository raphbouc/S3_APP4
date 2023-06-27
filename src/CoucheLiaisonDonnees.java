import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;

import static java.lang.System.arraycopy;

public class CoucheLiaisonDonnees extends Couche {
    private int ErreurCRC = 0;
    private int PacketRecu = 0;
    private int PacketEnvoye = 0;
    private static CoucheLiaisonDonnees instance;
    private CoucheLiaisonDonnees(){

    }

    /**
     * modele de conception singleton pour sassurer pour davoir une couche liaisondonnees
     * @return la couche
     */
    static  public CoucheLiaisonDonnees getInstance(){
        if (instance == null){
            instance = new CoucheLiaisonDonnees();
        }
        return instance;
    }

    /**
     * Reinitialiser la couche
     */
    public  void reset(){
        ErreurCRC = 0;
        PacketRecu = 0;
        PacketEnvoye = 0;
    }

    /**
     * Recevoir les donnees de la couche physique
     * @param PDU
     */
    @Override
    protected void getFromUp(byte[] PDU) {
        byte[] trame = new byte[PDU.length + 4];
        CRC32 CRC = new CRC32();
        CRC.update(PDU);
        long CRCValeur = CRC.getValue();
        byte[] CRCBytes = new byte[]{
                (byte) (CRCValeur >> 24),
                (byte) (CRCValeur >> 16),
                (byte) (CRCValeur >> 8),
                (byte) CRCValeur
        };
        arraycopy(CRCBytes, 0, trame, 0,CRCBytes.length);
        arraycopy(PDU, 0, trame, 4, PDU.length);
        PacketEnvoye++;
        log("Le paquet est envoyé. Numéro de paquet: " + PacketEnvoye);
        sendDown(trame);
    }


    /**
     * Recevoir un paquet de la couche reseau
     * @param PDU
     * @throws TransmissionErrorException
     */
    @Override
    protected void getFromDown(byte[] PDU) throws TransmissionErrorException {
        byte[] paquet = new byte[PDU.length -4];
        arraycopy(PDU, 4, paquet, 0, paquet.length);
        CRC32 CRC = new CRC32();
        CRC.update(paquet);
        int CRCValeur = (int) CRC.getValue();
        int CRCold = (((int) PDU[0] << 24) & 0xFF000000) | (((int) PDU[1] << 16) & 0x00FF0000) | (((int) PDU[2] << 8) & 0x0000FF00) | ((int) PDU[3] & 0x000000FF);
        if (CRCValeur != CRCold){
            System.out.println("Error CRC32");
            ErreurCRC++;
            log("Erreur CRC. Nombre total d'erreurs CRC: " + ErreurCRC);
            return;
        }
        PacketRecu++;
        log("Paquet reçu. Numéro de paquet: " + PacketRecu);
        sendUp(paquet);
    }

    /**
     * Affiche le message dans un document
     * @param message
     */
    private void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("liaisonDeDonnes.log", true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            out.println(timestamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}