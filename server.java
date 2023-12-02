import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

/* il client si connette al server, il client a seconda dell'operazione che vuole effettuare manda un codice STANDARD al server,
 * quest'ultimo a seconda di cosa riceva elabora la risposta e ne restituisce un altro a sua volta che verrà poi gestito lato client
 * per alleggerire il carico sul server.
 * --------------------------------------------
 * Il server effettua i controlli sul nome utente e la password che è stata inviata dal client confrontandoli con una lista di nomi utente e password presenti in un file di testo.
 * --------------------------------------------
 * L'univocità di un utente è determinata dal fatto che nessun utente può avere il medesimo username.
 * --------------------------------------------
 * Ogni operazione che viene richiesta dal client viene elaborata e DEVE obbligatoriamente restituire un codice di STATO dell'operazione.
 * --------------------------------------------
 * Lo storico dei messaggi può essere restituito solo e SOLAMENTE SE l'operazione di registrazione o accesso sono TERMINATE
 * --------------------------------------------
 * Il backup dei messaggi viene effettuato ogni 20 secondi (può variare il tempo oppure a seconda dell'utilizzo della CPU)
 * --------------------------------------------
 * I messaggi inviati dal client verranno ritrasmessi a tutti gli utenti collegati ESCLUSO il mittente.
*/

public class server extends Thread {
    static final int port = 3000;
    static int counter_client = 0;
    //Codici per la registrazione 0 - 9

    //ODR = Operazione di Registrazione
    static final int ODR = 0;

    //NND = Nome Non Disponibile --> cerca di fare la registrazione ma l'username esiste già
    static final int NND = 1;


    //Codici per l'accesso 10 - 20

    //ODA = Operazione di Accesso
    static final int ODA = 10;

    //UNT = Utente Non Trovato --> prova a fare l'accesso ma non risulta nessun utente con le credenziali inserite --> deve registrarsi riprovare 
    static final int UNT = 11;

    //Codice per procedere

    //PRG = Prosegui --> prosegue nelle varie operazioni
    static final int PRG = 69;

    static ServerSocket socketBenvenuto;
    static Vector<Socket> list_socket = new Vector<Socket>(0, 1);
    BufferedReader in;
    PrintWriter out;
    utente user;
    public static void main(String[] args) throws IOException {
        //creazione socket server
        socketBenvenuto = new ServerSocket();
        new Service();
        
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
        list_socket.add(client_socket);

        //sgancia e parte il thread
        start();
        System.out.println("Thread n." + counter_client +" sganciato");
    }

    @Override
    public void run() {
        //recuperiamo il client socket e aumentiamo il contatore dei client connessi
        Socket mittente = list_socket.elementAt(counter_client++);
        try {
            //blocco che gestisce l'accesso o la registrazione
            while (true) {
                int codice = Integer.parseInt(in.readLine());
                System.out.println(codice);
                switch (codice) {
                    case ODR:
                        //indica al client di proseguire con le proprie operazioni di registrazione
                        codice = Registrazione();
                        System.out.println(codice);
                        if(codice == NND) out.println(NND);
                        
                        //il nome utente è disponibile, aggiorniamo la lista utenti e ritorniamo il valore PRG
                        else if(codice == PRG){
                            out.println(PRG);
                            String password = in.readLine();
                            user.setPassword(password);
                            Service.AggionrnaUserList(user.toString());
                        }
                        break;
                    case ODA:
                        if(Accesso()){
                            //ci salviamo il valore del codice poichè servirà successivamente
                            codice = PRG;
                            out.println(codice);
                        } 
                        else out.println(NND);
                        break;
                    default:
                        break;
                }
                //operazione di accesso o registrazione conclusa, si esce dal ciclo, il server ora può inviare lo storico dei messaggi
                if(codice == PRG) break;
            }
        } catch (IOException e) { System.out.println(e);}

        //fase per inviare lo storico dei messaggi al client
        InviaDati();

        //invio dei messaggi in arrivo da un client in broadcast
        while (true) {
            try {
                //leggiamo il messaggio ricevuto dal client e salviamolo
                String messaggio = in.readLine();
                //chiamiamo la funzione per scrivere il messaggio arrivato nello storico
                Service.ScriviMessaggio(user.getUsername()+": "+messaggio);
                //per ogni socket destinatario diverso dal mittente mandiamo il messaggio in broadcast
                for (Socket destinatario : list_socket) {
                    if(mittente != destinatario){
                        new PrintWriter(destinatario.getOutputStream()).println(user.getUsername()+" "+messaggio);
                    }
                }
            } catch (IOException e) { System.out.println(e); }
        }
    }

    public void InviaDati() {
        try {
            BufferedReader fIN = new BufferedReader(new FileReader(Service.originale));
            
            String dati = fIN.readLine();

            //se non ci sono messaggi allora è stata appena creata la chat
            if(dati == null) out.println("Nessun messaggio presente nello storico, manda un messaggio per primo!");
            
            //per ogni riga viene inviato un messaggio, se non ci sono messaggi non entrerà mai nel ciclo while poichè dati = null
            while(dati != null){
                out.println(dati);
                dati = fIN.readLine();
                //se la nuova riga letta è null allora non ci sono più messaggi da leggere e inviare
                if(dati == null) out.println("\nTutti i messaggi presenti nello storico sono stati recuperati\n");
            }
            fIN.close();
        }catch (Exception e) { System.out.println(e); }
    }

    public boolean Accesso() {
        boolean x = false;
        String username = null, password = null;
        try {
            //leggiamo sia la password che l'username
            String username_client = in.readLine();
            String password_client = in.readLine();
            BufferedReader fIN = new BufferedReader(new FileReader(Service.userList));
            String dati = in.readLine();

            //iniziamo a leggere le righe della userList e facciamo dei controlli sia sulla password che sull'esername
            while (dati != null) {
                StringTokenizer st = new StringTokenizer(dati, "-");
                while (st.hasMoreTokens()) {
                    username = st.nextToken();
                    password = st.nextToken();
                }

                //credenziali corrette, lo comunichiamo al client
                if((password_client == password) && (username_client == username)){
                    out.println(PRG);
                    x = true;
                    //se abbiamo trovato l'utente usciamo dal ciclo con break
                    break;
                }
                else x = false;

                //leggiamo la prossima riga del file
                dati = fIN.readLine();
            }
            //chiusura del lettore del file
            fIN.close();
        } catch (IOException e) { System.out.println(e); }
        //terminata la lettura del file, la funzione ritornerà true se è stato trovato, false altrimenti
        return x; 
    }

    private int Registrazione() {
        try {
            String username = in.readLine();
            BufferedReader fIN = new BufferedReader(new FileReader(Service.userList));
            boolean disponibile = false;
            
            //recupero tutti gli username dal file e lo confronto con quello ricevuto
            String dati = fIN.readLine();

            while(dati != null){
                byte n_token = 0;
                StringTokenizer st = new StringTokenizer(dati, "-");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    n_token++;
                    //token 1 corrisponde al nome utente
                    if(n_token == 1){
                        /*
                         * se l'username è diverso da quello estrapolato in quel momento e in quella
                         * riga impostiamo "disponibile" su true, se risulta già utilizzato interrompiamo
                         * la lettura del file e lo comunichiamo al client
                        */
                        if(!username.equals(token)) {
                            disponibile = true;
                            user = new utente(username);
                            break;
                        }
                        else {
                            disponibile = false;
                            break;
                        }
                    }
                }
                //leggiamo la prossima riga del file
                dati = fIN.readLine();
            }
            
            //chiusura del lettore del file
            fIN.close();

            //l'username NON è disponibile e ritorniamo il valore NND
            if(!disponibile) return NND;

            return PRG;

        } catch (IOException e) { System.out.println(e); }
        return 0;
    }
}    
