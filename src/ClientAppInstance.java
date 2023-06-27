
    import java.io.IOException;
    public class ClientAppInstance {
        CouchePhysique couchePhysique;
        CoucheApplication coucheApplication;
        String filename;

        /**
         * Fonction pour envoyer le fichier
         * @param filename nom du fichier
         * @param destination_ip adresse ip de destination
         * @param listening_port port
         * @param addErrors savoir si on ajoute des erreur vonlontairement ou non
         * @throws IOException exception
         * @throws InterruptedException exception
         */
        public ClientAppInstance(String filename, String destination_ip, String listening_port, boolean addErrors) throws IOException, InterruptedException {
            this.filename = filename;
            ClientInstanceBuild(destination_ip, listening_port, addErrors);
        }

        /**
         * Fonction pour envoyer le fichier
         * @param destination_ip adresse ip de destination
         * @param listening_port port
         * @param addErrors facteur pour ajouter des erreurs volontairement ou non
         * @throws IOException execption
         */
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

        /**
         * Commencer a ajouter le fichier au serveur
         * @throws IOException
         * @throws InterruptedException
         */
        public void ClientStart() throws IOException, InterruptedException {
            System.out.println("Client Start");
            couchePhysique.start();
            coucheApplication.EnvoyeFichier(filename);
        }
    }

