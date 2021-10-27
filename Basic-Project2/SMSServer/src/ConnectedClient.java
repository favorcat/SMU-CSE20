import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ConnectedClient extends Thread{
    Socket socket;
    Server server;

    OutputStream outStream;
    DataOutputStream dataOutStream;
    InputStream inStream;
    DataInputStream dataInStream;

    String msg; // 외부에서 들어올 문자열 정보값
    String uName; // 유저 이름

    // 주고 받는 데이터가 무엇과 관련되어 있는지 알려주기 위해서 태그 이용
    String loginTag = "LOGIN";
    String queryTag = "QUERY";

    ConnectedClient(Socket _s, Server _server){
        this.server = _server;
        this.socket = _s;
    }

    // 쓰레드를 상속받는 클래스에는 쓰레드가 동작하는 메소드를 구현해야 한다
    // 메인 쓰레드랑 병렬로 동작하게 된다.
    // 쓰레드의 시작점을 알려줌
    @Override
    public void run() {
        try {
            System.out.println("Server> "+ this.socket.getInetAddress() + "에서의 접속이 연결되었습니다.");

            outStream = this.socket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);

            inStream = this.socket.getInputStream();
            dataInStream = new DataInputStream(inStream);

            while(true){
                msg = dataInStream.readUTF();
                StringTokenizer stk = new StringTokenizer(msg, "//");
                if(stk.nextToken().equals(loginTag)){
                    String id = stk.nextToken();
                    String pass = stk.nextToken();
                    // 로그인을 위한 아이디와 암호 확인 작업 필요
                }
            }
        } catch(Exception e) {

        }
    }
}
