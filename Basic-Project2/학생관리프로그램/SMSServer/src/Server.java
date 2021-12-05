import java.io.*;
import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


public class Server {
    // 서버 소켓 생성
    ServerSocket ss = null;
    // 연결된 클라이언트 관리를 위한 배열
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
    DBChecker dc;
    public static void main(String[] args) {
        Server server = new Server();
        server.dc = new DBChecker();

        try {
            server.ss = new ServerSocket(20000);
            System.out.println("Server > Server Socket is Created...");
            while (true) {
                Socket socket = server.ss.accept();
                ConnectedClient c = new ConnectedClient(socket, server);
                server.clients.add(c);
                c.start();
            }

        } catch (SocketException e) {
            System.out.println("Server > 소켓 관련 예외 발생, 서버종료");
        } catch (IOException e) {
            System.out.println("Server > 입출력 예외 발생");
        }
    }
}

// 스레드를 활용한 연결된 클라이언트 관리 클래스
class ConnectedClient extends Thread{
    // 소켓 및 서버 선언
    Socket socket;
    Server server;

    // 데이터를 가져오고 주기 위해 선언
    OutputStream outStream;
    DataOutputStream dataOutStream;
    InputStream inStream;
    DataInputStream dataInStream;

    // 서버에 날려줄 메세지
    String msg;

    // 보낸 문자열이 로그인 정보인지, 회원가입 정보인지, 검색 정보인지 알기 위한 태그
    String loginTag = "LOGIN";
    String signTag = "SIGNUP";
    String searchTag = "SEARCH";

    // 생성자에 소켓과 서버를 넣어줌
    ConnectedClient(Socket _s, Server _ss){
        this.socket = _s;
        this.server = _ss;
    }

    @Override
    public void run() {
        DBChecker dc = new DBChecker();

        try {
            // 정상적으로 연결이 되면 연결되었다고 출력
            System.out.println("Server> " + this.socket.toString() + "에서의 접속이 연결되었습니다.");

            // 데이터를 받고 주기 위해 생성
            outStream = this.socket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = this.socket.getInputStream();
            dataInStream = new DataInputStream(inStream);

            while (true) {
                // 계속 루프를 돌면서 문자열 받는 것 검사
                msg = dataInStream.readUTF();
                // 기본적으로 문자열들은 "//"로 구분되어 있으니 StringTokenizer 사용
                StringTokenizer stk = new StringTokenizer(msg, "//");
                // 문자열의 맨 앞은 tag 문자열
                String token = stk.nextToken();

                // 만약 태그가 로그인이라면
                if (token.equals(loginTag)) {
                    // 문자열에 있는 ID와 PW를 Tokenizer로 가져옴
                    String id = stk.nextToken();
                    String pass = stk.nextToken();

                    // 로그인을 위한 아이디와 암호 확인 작업 필요
                    if (server.dc.check(id, pass)) { // 만약 DB에 로그인 정보가 있다면
                        // 로그인 성공
                        dataOutStream.writeUTF("LOGIN_OK");
                        // department 테이블에서 학과 정보 데이터 송신
                        String re = server.dc.departmentCheck();
                        dataOutStream.writeUTF("DEP" + re);
                        // 모든 학생 데이터 송신
                        String st = server.dc.searchStudent("ALL", 0, 0);
                        dataOutStream.writeUTF("STUDENT" + st);
                    } else { // 로그인 실패
                        dataOutStream.writeUTF("LOGIN_FAIL");
                    }

                    // 회원가입을 위한 정보를 받았다면
                } else if (token.equals((signTag))){
                    String id = stk.nextToken();
                    String pass = stk.nextToken();

                    // DB에 회원 정보 추가
                    if (server.dc.signupCheck(id, pass)) {
                        // 회원가입 성공
                        dataOutStream.writeUTF("SIGNUP_OK");
                    } else { // 회원가입 실패
                        dataOutStream.writeUTF("SIGNUP_FAIL");
                    }

                    // 학생 검색을 위한 정보를 받았다면
                } else if (token.equals(searchTag)){
                    // 첫 번째 토큰은 학과 정보
                    String dep = stk.nextToken();
                    // 두 번째 토큰은 학년 정보, 문자열로 받았으니 int로 형변환
                    int year = Integer.parseInt(stk.nextToken());
                    // 세 번째 토큰은 학부생/대학원생 정보, 문자열로 받았으니 int로 형변환
                    int grade = Integer.parseInt(stk.nextToken());

                    // DB에 요청하여 결과를 문자열로 받고 데이터 송신
                    String st = server.dc.searchStudent(dep, year, grade);
                    dataOutStream.writeUTF("STUDENT" + st);
                }
            }
        } catch (IOException e) {
            System.out.println("Server> 클라이언트 연결 종료");
            e.printStackTrace();
        }
    }
}