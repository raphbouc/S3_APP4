public abstract class Layer {
    private Layer layerUp;
    private Layer layerDown;
    protected abstract void getFromUp(byte[] PDU);
    protected abstract void getFromDown(byte[] PDU) throws TransmissionErrorException;
    protected void sendUp(byte[] PDU) throws TransmissionErrorException {
        layerUp.getFromDown(PDU);
    }
    protected void sendDown(byte[] PDU){
        layerDown.getFromUp(PDU);
    }
    public void setLayerUp(Layer uplayer){
        layerUp = uplayer;
    }
    public void setLayerDown(Layer downlayer){
        layerDown = downlayer;
    }
}
