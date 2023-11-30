/* questa classe salva il nome utente, dopo conferma del server.
 * Le sue funzioni sono:
 * salvare le informazioni dell'utente che potrebbero essere usate per effettuare il login automatico o non
 * se la variabile booleana ricordaPassword è true non dovrà inserirla per effettuare l'accesso, dovrà utilizzare
 * solamente l'username che è univoco per ogni utente 
*/

public class utente {
    private String username;
    private String password; //generata automaticamente?
    private boolean ricordaPassword;

    //costruttore utilizzato per inviare i dati al server o per memorizzare l'utente al server
    public utente(String username, String password, boolean ricordaPassword){
        this.username = username;
        this.password = password;
        this.ricordaPassword = ricordaPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getRicordaPassword(){
        return ricordaPassword;
    }


    //utilizzato dal server per effettuare lavor
    @Override
    public String toString() {
        return username + "-" + password + "-" + ricordaPassword;
    }
}
