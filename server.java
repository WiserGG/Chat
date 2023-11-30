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
    //Codici per la registrazione 0 - 9

    //ODR = Operazione di Registrazione
    static final int ODR = 0;

    //UND = Nome Non Disponibile --> cerca di fare la registrazione ma l'username esiste già
    static final int NND = 1;

    //UGE = Utente Già Esistente --> cerca di fare la registrazione ma risulta che quell'utente è già registrato --> accesso automatico con quell
    static final int UGE = 2;


    //Codici per l'accesso 10 - 20

    //ODA = Operazione di Accesso
    static final int ODA = 10;

    //UNT = Utente Non Trovato --> prova a fare l'accesso ma non risulta nessun utente con le credenziali inserite --> deve registrarsi o riprovare 
    static final int UNT = 11;

    //PNV = Password Non Valida --> prova a fare l'accesso, l'username esiste ma non la password --> deve inserire nuovamente la password
    static final int PNV = 12; 

    //Codice per procedere

    //PRG = Prosegui --> prosegue nelle varie operazioni
    static final int PRG = 69;


    static ServerSocket socketBenvenuto;
    static Vector<Socket> list_socket = new Vector<Socket>(0, 1);
    BufferedReader in;
    PrintWriter out;
    

    public static void main(String[] args) throws IOException {

        chatService Service = new chatService("Messaggi.txt", "backupMessaggi.bak");
        
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
        try {
            while (true) {
                int codice = Integer.parseInt(in.readLine());
                switch (codice) {
                    case ODR:
                        //indica al client di proseguire con le proprie operazioni di registrazione
                        out.print(PRG);
                        Registrazione();
                        break;
                    case ODA:
                        //indica al client di proseguire con le proprie operazioni di accesso
                        out.print(PRG);
                        Accesso();
                    default:
                        break;
                }
            }
        } catch (IOException e) { System.out.println("Errore nella lettura dei file --> "+ e);}
    }

    private void Accesso() {
    }

    private void Registrazione() {
    }
}    