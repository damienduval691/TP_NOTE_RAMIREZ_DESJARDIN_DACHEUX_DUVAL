package fr.itii25.option3.message;





public class MessageDeDonnees<TypeDeMessageDeDonnee> extends Message {

    private TypeDeMessageDeDonnee messageDeDonnee;

    //Constructeur en paramètre le messageDeDonnee
    public MessageDeDonnees(TypeDeMessageDeDonnee messageDeDonnee) {
        //On vérifie si le paramètre passé existe bien en mémoire, sinon on envoie une erreur
        if (messageDeDonnee == null)
            throw new IllegalArgumentException("messageDeDonnee is null");
        else this.messageDeDonnee = messageDeDonnee;

    }

    public TypeDeMessageDeDonnee getMessageDeDonnee(){
        return messageDeDonnee;
    }
}