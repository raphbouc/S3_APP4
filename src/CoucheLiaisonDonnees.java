import java.util.zip.CRC32;

import static java.lang.System.arraycopy;

public class DataLinkLayer extends Layer{
    private int ErreurCRC = 0;
    private int PacketRecu = 0;
    private int PacketEnvoye = 0;
    private static DataLinkLayer instance;
    private DataLinkLayer(){

    }
    static  public DataLinkLayer getInstance(){
        if (instance == null){
            instance = new DataLinkLayer();
        }
        return instance;
    }
    public  void reset(){
        ErreurCRC = 0;
        PacketRecu = 0;
        PacketEnvoye = 0;
    }
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
        sendDown(trame);
    }



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
            return;
        }
        PacketRecu++;
        sendUp(paquet);
    }
}
