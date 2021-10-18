import java.io.*;   // input,output 관련 라이브러리
import java.net.*;  // 네트워크 관련 라이브러리
import java.util.ArrayList;

public class ServerApplication {
    ServerSocket ss = null; // 필드 생성, null 로 초기화
    // 동적 배열
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();

    public static void main(String[] args) {
        // ServerApplication 객체화
        ServerApplication server = new ServerApplication();

        try {
            // 상대방 ip 주소는 필요없고 포트 번호만 필요함
            server.ss = new ServerSocket(55555);
            System.out.println("Server> 서버 소켓이 생성 되었습니다.");

            // 클라이언트의 연결을 기다림, 연결요청이 오면 socket 을 반환함
            Socket socket = server.ss.accept();
            System.out.println("Server> 클라이언트와 연결 되었습니다.");
            // 연결 되어있는 클라이언트의 ip 주소를 출력
            System.out.println("Server> 클라이언트 : " + socket.getInetAddress());

            ConnectedClient c = new ConnectedClient(socket);
            server.clients.add(c);
            c.start();

        } catch (SocketException e) {
            System.out.println("Server> 소켓 예외가 발생했습니다");
        } catch (IOException e) {
            System.out.println("Server> 입출력 예외가 발생했습니다");
        }
    }
}

class ConnectedClient extends Thread {
    Socket socket;

    ConnectedClient(Socket _s){
        this.socket = _s;
    }

    @Override
    public void run() {
        try{
            while(true){
                System.out.println("Client: " + socket.getInetAddress() + "와의 연결은 아직 살아있다.");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            System.out.println("Exception");
        }
    }
}