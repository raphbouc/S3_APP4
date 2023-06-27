import java.io.IOException;

public class ServerInstance {
    private CouchePhysique couchePhysique;

    public ServerInstance(String listening_port) throws IOException {
        ServerInstanceBuild(listening_port);
    }

    private void ServerInstanceBuild(String listening_port) throws IOException {
        CoucheApplication coucheApplication = CoucheApplication.getInstance();
        CoucheTransport coucheTransport = CoucheTransport.getInstance();
        CoucheReseau coucheReseau = CoucheReseau.getInstance();
        CoucheLiaisonDonnees coucheLiaisonDonnees = CoucheLiaisonDonnees.getInstance();
        couchePhysique = CouchePhysique.getInstance();
        couchePhysique.setLayerUp(coucheLiaisonDonnees);
        coucheLiaisonDonnees.setLayerUp(coucheReseau);
        coucheLiaisonDonnees.setLayerDown(couchePhysique);
        coucheReseau.setLayerUp(coucheTransport);
        coucheReseau.setLayerDown(coucheLiaisonDonnees);
        coucheTransport.setLayerDown(coucheReseau);
        coucheTransport.setLayerUp(coucheApplication);
        coucheApplication.setLayerDown(coucheTransport);
        // set server
        couchePhysique.setReceptionThread(Integer.parseInt(listening_port));
        couchePhysique.setDestPort(25001);
        couchePhysique.setDestAddresseIp("localhost");
    }


    public void StartServer() throws IOException {
        couchePhysique.start();
        System.out.println("Running");
        System.out.println("Q: to kill");
        System.out.println("Then enter");
        while(couchePhysique.threadRunning()) {
            int command = System.in.read();
            switch (command) {
                case 113:
                case 81:
                    System.exit(0);
                    break;
            }
        }
    }
}