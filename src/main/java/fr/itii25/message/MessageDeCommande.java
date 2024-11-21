package fr.itii25.message;

public class MessageDeCommande extends Message{

    private String messageDeCommande;

    public MessageDeCommande(String messageDeCommande) {
        if (messageDeCommande == null || messageDeCommande.isBlank())
            throw new NullPointerException("Ce message ne peut pas être vide !");
        else
            this.messageDeCommande = messageDeCommande;
    }

    public String getMessageDeCommande() {
        return messageDeCommande;
    }




}
