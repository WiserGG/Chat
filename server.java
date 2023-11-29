import java.io.*;
import java.net.*;

/* il client si connette al server, il server chiede di inserire un nome utente, il client lo inserisce e lo manda indietro,
 * il server legge il nome utente e lo confronta con una lista di nomi utente che ha salvata in un file di teso, se dispononibile (
 * utilizzare un vettore può aiutare poichè ha il metodo contains()  ) lo salva e comunica che il nome utente è disponibile al cliente.
 * dopo questa operazione necessaria per tener traccia dei messaggi il server spedisce al cliente tutto lo storico dei messaggi 
*/

public class server extends Thread {
    static final int port = 3000;
    static ServerSocket socketBenvenuto;
    public static void main(String[] args) throws IOException {
        
        //creazione socket server
        socketBenvenuto = new ServerSocket();

        //bind del server socket all'indirizzo ip del pc e ad una porta specifica
        socketBenvenuto.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        System.out.println("Server socket: "+ socketBenvenuto.getLocalSocketAddress());

        try {
            while (true) {
                //server in ascolto di client
                Socket client_socket = socketBenvenuto.accept();
                System.out.println("Connessione accettata con il client: "+ client_socket.getLocalSocketAddress());
                try {
                    new server(client_socket);
                } catch (Exception e) {client_socket.close();}
            }
        }catch (IOException e) { 
            System.out.println("Accept fallito"); 
            System.exit(1);
        }
        socketBenvenuto.close();
    }

    public server(Socket client_socket) {

    }
}    