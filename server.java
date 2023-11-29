import java.io.*;
import java.net.*;
import java.util.Vector;

/* il client si connette al server, il server chiede di inserire un nome utente, il client lo inserisce e lo manda al server,
 * il server legge il nome utente e lo confronta con una lista di nomi utente che ha salvata in un file di testo, se dispononibile (
 * utilizzare un vettore può aiutare poichè ha il metodo contains()  ) lo salva e comunica che il nome utente è disponibile al client.
 * dopo questa operazione necessaria per tener traccia dei messaggi il server spedisce al client tutto lo storico dei messaggi 
*/

public class server extends Thread {
    static final int port = 3000;
    static int counter_client = 0;

    static ServerSocket socketBenvenuto;
    static Vector<Socket> list_socket = new Vector<Socket>(0, 1);
    BufferedReader in;
    PrintWriter out;
    

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
                System.out.println("Connessione accettata con il client: "+ client_socket.getRemoteSocketAddress());
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

    public server(Socket client_socket) throws IOException {
        //creazione dello Input/Output stream per la lettura e scrittura dei messaggi
        in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        out = new PrintWriter((new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream()))), true);
        ++counter_client;
        list_socket.add(client_socket);
        
        //sgancia e parte il thread
        start();
        System.out.println("Thread n." + counter_client +" sganciato");
    }

    @Override
    public void run() {
        out.println("Inserisci un nome utente: ");
    }
}    