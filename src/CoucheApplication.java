import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static java.lang.System.arraycopy;

public class CoucheApplication extends Couche {
    private static CoucheApplication instance;

    /**
     * Modele de conception singleton pour la couche application
     * @return la couche sil elle nest pas deja creee
     */
    public static CoucheApplication getInstance(){
        if (instance == null){
            instance = new CoucheApplication();
        }
        return instance;
    }

    /**
     * Recevoir des donnees de la couche superieur
     * @param PDU donnees a recevoir
     */
    @Override
    protected void getFromUp(byte[] PDU) {
        //DERNIERE COUCHE
    }

    /**
     * Recevoir des donnes de la couche inferieur
     * @param PDU donnee a recevoir
     * @throws TransmissionErrorException
     */
    @Override
    protected void getFromDown(byte[] PDU) throws TransmissionErrorException {
        System.out.println("Receiving");
        String title = new String(Arrays.copyOfRange(PDU, 0, 188), StandardCharsets.US_ASCII).trim();
        byte[] data_bytes = Arrays.copyOfRange(PDU, 188, PDU.length);
        try {
            String filePath = new File("").getAbsolutePath();
            File file = new File(filePath + "/dest/" + title);
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                System.out.println("Fichier Cree: " + file.getName());
            } else {
                System.out.println("Fichier existant");
            }
            try (FileOutputStream fos = new FileOutputStream(file.getPath())) {
                System.out.println("Écrire Stream");
                fos.write(data_bytes);
                System.out.println("Écrire fini");
            }
        } catch (Exception exception) {
            System.out.println("Erreur");
            exception.printStackTrace();
        }
    }

    /**
     * Envoyer le fichier a la couche transport
     * @param path fichier a transmettre
     * @throws IOException exception
     * @throws InterruptedException exception
     */
    public void EnvoyeFichier(String path) throws IOException, InterruptedException{
        File file = new File(path);
        byte[] APDU;
        byte[] nomFichier = file.getName().getBytes();
        Path FichierPath = file.toPath();
        byte[] fileBytes = Files.readAllBytes(FichierPath);
        APDU = new byte[188 + fileBytes.length];
        arraycopy(nomFichier, 0, APDU, 0, nomFichier.length);
        arraycopy(fileBytes,0,APDU,188,fileBytes.length);
        sendDown(APDU);
        Thread.sleep(1000);
        System.exit(0);
    }
}

