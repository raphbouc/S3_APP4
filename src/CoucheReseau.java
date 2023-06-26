public class CoucheReseau extends Couche {
    private static CoucheReseau instance;
    public  static CoucheReseau getInstance(){
        if (instance == null){
            instance = new CoucheReseau();
        }
        return instance;
    }
    @Override
    protected void getFromUp(byte[] PDU) {
        sendDown(PDU);
    }

    @Override
    protected void getFromDown(byte[] PDU) throws TransmissionErrorException {
        sendUp(PDU);
    }
}
