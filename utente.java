/* questa classe salva il nome utente, dopo conferma del server.
 * Le sue funzioni sono:
 * salvare il nome utente ricevuto tramite costruttore
 * restituire il nome utente che potrebbe servire per eseguire confronti con altri nome utente
*/

public class utente {
    private String username;

    public utente(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
