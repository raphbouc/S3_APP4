public class CoucheReseau extends Couche {
    private static CoucheReseau instance;

    /**
     * singleton pour s assurer davoir seulement une couche reseau
     * @return
     */
    public  static CoucheReseau getInstance(){
        if (instance == null){
            instance = new CoucheReseau();
        }
        return instance;
    }

    /**
     * envoyer les data a la couche superieur
     * @param PDU
     */
    @Override
    protected void getFromUp(byte[] PDU) {
        sendDown(PDU);
    }

    /**
     * envoyer les data a la couche inferieur
     * @param PDU
     * @throws TransmissionErrorException
     */

    @Override
    protected void getFromDown(byte[] PDU) throws TransmissionErrorException {
        sendUp(PDU);
    }
}
