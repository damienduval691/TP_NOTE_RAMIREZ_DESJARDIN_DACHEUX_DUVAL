package fr.itii25.option4.message;

public class MessageDeCommande extends Message {

    private String messageDeCommande;

    //Constructeur de la classe MessageDeCommande
    public MessageDeCommande(String messageDeCommande) {
        //Vérification que le paramètre entré existe bien en mémoire afin de ne pas avoir un objet null
        if (messageDeCommande == null)
            //Si c'est null, alors on indique une erreur
            throw new IllegalArgumentException("messageDeDonnee is null");
        else if(messageDeCommande.isBlank())
            System.out.println("Ce message ne peut pas être vide !");
        else this.messageDeCommande = messageDeCommande;
    }

    public String getMessageDeCommande() {
        return messageDeCommande;
    }
}
