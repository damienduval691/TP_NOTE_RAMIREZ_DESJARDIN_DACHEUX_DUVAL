package fr.itii25.option1.main;

import fr.itii25.option1.message.Message;
import fr.itii25.option1.taches.Emetteur;
import fr.itii25.option1.taches.Recepteur;

import java.util.concurrent.LinkedBlockingQueue;

public class Utilisation {
    public static void main(String[] args) {

        //Déclaration du canal de communication
        LinkedBlockingQueue<Message> canalCommunication = new LinkedBlockingQueue<Message>();

        //Déclaration des Runnable Emtteur & Recepteur, avec en paramètre, le canal de communication
        Emetteur tacheEmettrice     = new Emetteur(canalCommunication);
        Recepteur tacheReceptrice   = new Recepteur(canalCommunication);

        //Décalaration des Thread avec en paramètre les tâches emetteur & recepteur
        Thread emetteurThread = new Thread(tacheEmettrice);
        Thread recepteurThread = new Thread(tacheReceptrice);

        //Démarrage des deux Threads
        emetteurThread.start();
        recepteurThread.start();
    }
}
