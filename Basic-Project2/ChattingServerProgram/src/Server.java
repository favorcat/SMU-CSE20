import java.io.*;
import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    ServerSocket ss = null;
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.ss = new ServerSocket(12345);
            System.out.println("Server> Server Socket is Created...");
            while(true) {
                Socket socket = server.ss.accept();
                ConnectedClient c = new ConnectedClient(socket);
                server.clients.add(c);
                c.start();
            }

        } catch(SocketException e) {
            System.out.println("Server> 서버종료");
        } catch(IOException e) {
            System.out.println("Server> 입출력 예외 발생");
        }
    }
}

class ConnectedClient extends Thread{
    Socket socket;
    OutputStream outStream;
    DataOutputStream dataOutStream;
    InputStream inStream;
    DataInputStream dataInStream;

    ConnectedClient(Socket _s){
        this.socket = _s;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server> " + this.socket.toString() + "에서의 접속이 연결되었습니다.");
            outStream = this.socket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = this.socket.getInputStream();
            dataInStream = new DataInputStream(inStream);

            dataOutStream.writeUTF("Welcome to this server!!!");
            while(true) {
                String msg = dataInStream.readUTF();
                System.out.println("Server> " + this.socket.toString() + ": " + msg);
            }

        } catch(IOException e) {
            System.out.println("Server> 입출력 예외 발생");
        }
    }
}