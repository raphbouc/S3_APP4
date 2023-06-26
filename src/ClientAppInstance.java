
    import java.io.IOException;
    public class ClientAppInstance {
        CouchePhysique couchePhysique;
        CoucheApplication coucheApplication;
        String filename;

        public ClientAppInstance(String filename, String destination_ip, String listening_port, boolean addErrors) throws IOException, InterruptedException {
            this.filename = filename;
            ClientInstanceBuild(destination_ip, listening_port, addErrors);
        }

        public void ClientInstanceBuild(String destination_ip, String listening_port, boolean addErrors) throws IOException {
            CoucheTransport coucheTransport = CoucheTransport.getInstance();
            CoucheReseau coucheReseau = CoucheReseau.getInstance();
            CoucheLiaisonDonnees coucheLiaisonDonnees = CoucheLiaisonDonnees.getInstance();
            couchePhysique = CouchePhysique.getInstance();
            coucheApplication = CoucheApplication.getInstance();
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
            couchePhysique.delayerreur = addErrors ? 10 : -1;
            couchePhysique.delay = 1;
            couchePhysique.setDestPort(25002);
            couchePhysique.setDestAddresseIp(destination_ip);
        }

        public void ClientStart() throws IOException, InterruptedException {
            System.out.println("Client Start");
            couchePhysique.start();
            coucheApplication.EnvoyeFichier(filename);
        }
    }

