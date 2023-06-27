/**
 * Classe abstraite pour toutes les couche
 */
public abstract class Couche {

    private Couche layerUp;
    private Couche layerDown;

    /**
     * recevoir la data de la couche superieur
     * @param PDU
     */
    protected abstract void getFromUp(byte[] PDU);

    /**
     * Recevoir la data de la couche inferieur
     * @param PDU
     * @throws TransmissionErrorException
     */
    protected abstract void getFromDown(byte[] PDU) throws TransmissionErrorException;

    /**
     * Envoyer les donnees a la couche superieur
     * @param PDU
     * @throws TransmissionErrorException
     */
    protected void sendUp(byte[] PDU) throws TransmissionErrorException {
        layerUp.getFromDown(PDU);
    }

    /**
     * Envoyer les donnees a la couche inferieur
     * @param PDU
     */
    protected void sendDown(byte[] PDU){
        layerDown.getFromUp(PDU);
    }

    /**
     * Configurer la couche superieur
     * @param uplayer
     */
    public void setLayerUp(Couche uplayer){
        layerUp = uplayer;
    }

    /**
     * configurer la couche inferieur
     * @param downlayer
     */
    public void setLayerDown(Couche downlayer){
        layerDown = downlayer;
    }
}
