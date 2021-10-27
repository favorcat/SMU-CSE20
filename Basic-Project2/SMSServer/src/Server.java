import java.io.*;
import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server {
    ServerSocket ss = null;
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
    LoginChecker lc = null;

    public static void main(String[] args) {
        Server server = new Server();
        server.lc = new LoginChecker();

        try {
            server.ss = new ServerSocket(555556);
            System.out.println("Server> Server Socket is Created");
            while(true){
                Socket socket = server.ss.accept();
                // 소켓이 없어지기 전에 소켓을 상속시키는 것
                ConnectedClient c = new ConnectedClient(socket, server);
                server.clients.add(c);

                c.start();
            }
        } catch(Exception e) {
            System.out.println("Sever> 예외 발생");
        }
    }
}
