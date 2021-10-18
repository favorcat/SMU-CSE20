import java.io.*;   // input,output 관련 라이브러리
import java.net.*;  // 네트워크 관련 라이브러리

public class ClientApplication {
    Socket mySocket = null; // 필드 생성, null 로 초기화

    public static void main(String[] args) {
        ClientApplication client = new ClientApplication();

        // 연결하고 싶은 상대방의 ip 주소가 필요함
        try {
            client.mySocket = new Socket("localhost", 55555);
            System.out.println("Client> 서버와 연결 되었습니다.");
            while(true){
                System.out.println("Client> 서버와의 연결은 아직 살아있다");
                Thread.sleep(500);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Connection Fail");
        }

    }
}
