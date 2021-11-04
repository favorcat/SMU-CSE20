import java.io.*;
import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Server {   // 메인클래스
    ServerSocket ss = null;
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
    LoginChecker lc;
    public static void main(String[] args) {
        Server server = new Server();
        server.lc = new LoginChecker();
        try {
            server.ss = new ServerSocket(20000);
            System.out.println("Server > Server Socket is Created...");
            while(true) {
                Socket socket = server.ss.accept();
                ConnectedClient  c = new ConnectedClient(socket, server);
                server.clients.add(c);
                c.start();
            }

        }catch(SocketException e) {
            System.out.println("Server > 소켓 관련 예외 발생, 서버종료");
        }catch(IOException e) {
            System.out.println("Server > 입출력 예외 발생");
        }

    }

}