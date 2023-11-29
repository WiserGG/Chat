import java.io.*;
import java.net.*;

/* il client si connette al server, il server chiede di inserire un nome utente, il client lo inserisce e lo manda indietro,
 * il server legge il nome utente e lo confronta con una lista di nomi utente che ha salvata in un file di teso, se dispononibile (
 * utilizzare un vettore può aiutare poichè ha il metodo contains()  ) lo salva e comunica che il nome utente è disponibile al cliente.
 * dopo questa operazione necessaria per tener traccia dei messaggi il server spedisce al cliente tutto lo storico dei messaggi 
*/

public class client extends Thread {
    static BufferedReader in;
    static PrintWriter out;
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getByName("zuzu.sytes.net"), 3000));
        System.out.println("Client Socket: "+ socket);
        //creazione stream di input e output 
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter((new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))), true);
        System.out.print(in.readLine());
        
    }

}
