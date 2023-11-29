import java.io.*;
import java.net.*;

public class client extends Thread {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        
        socket.connect(new InetSocketAddress(InetAddress.getByName("zuzu.sytes.net"), 3000));

        socket.close();
    }
}
