public abstract class Couche {
    private Couche layerUp;
    private Couche layerDown;
    protected abstract void getFromUp(byte[] PDU);
    protected abstract void getFromDown(byte[] PDU) throws TransmissionErrorException;
    protected void sendUp(byte[] PDU) throws TransmissionErrorException {
        layerUp.getFromDown(PDU);
    }
    protected void sendDown(byte[] PDU){
        layerDown.getFromUp(PDU);
    }
    public void setLayerUp(Couche uplayer){
        layerUp = uplayer;
    }
    public void setLayerDown(Couche downlayer){
        layerDown = downlayer;
    }
}
