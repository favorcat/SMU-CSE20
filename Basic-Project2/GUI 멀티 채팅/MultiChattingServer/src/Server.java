import java.io.*;
import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    // 서버 소켓 생성
    ServerSocket ss = null;
    // 연결된 클라이언트 관리를 위한 배열
    ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
    ArrayList<String> connectedUser = new ArrayList<String>();
    // 로그인 검사할 변수 설정
    LoginChecker lc;

    public static void main(String[] args) {
        // 서버 생성
        Server server = new Server();
        // 서버의 LoginChecker 생성
        server.lc = new LoginChecker();
        try {
            // 서버소켓 포트 60000번 설정
            server.ss = new ServerSocket(60000);
            System.out.println("Server> Server Socket is Created...");
            while(true) {
                // 서버에서 계속 클라이언트가 접속하는지 보고 수락해서 연결해줌
                // 연결 후 연결된 클라이언트 배열에 추가 및 실행
                Socket socket = server.ss.accept();
                ConnectedClient c = new ConnectedClient(socket, server);
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

    // 서버에 날려줄 메세지와 해당 메세지를 보낸 유저의 이름
    String msg;
    String uName;

    // 보낸 문자열이 로그인인지, 채팅인지 확인하기 위한 태그
    String loginTag = "LOGIN";
    String signTag = "SIGNUP";
    String chatTag = "CHAT";

    // 생성자에 소켓과 서버를 넣어줌
    ConnectedClient(Socket _s, Server _ss){
        this.socket = _s;
        this.server = _ss;
    }

    @Override
    public void run() {
        LoginChecker lc = new LoginChecker();
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat simple = new SimpleDateFormat(" (a hh:mm)");

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
                    if (server.lc.check(id, pass)) {
                        dataOutStream.writeUTF("LOGIN_OK");
                        // 유저의 이름을 가져옴
                        uName = server.lc.getName(id);
                        // 유저를 연결된 유저 리스트에 추가
                        server.connectedUser.add(uName);
                        // 업데이트 된 유저 리스트 데이터 전송
                        dataOutStream.writeUTF("USER//" + server.connectedUser);
                        // 채팅창에서 쓰여질 내 이름 전송
                        dataOutStream.writeUTF("NAME//" + uName);
                        // 입장 메세지 출력
                        dataOutStream.writeUTF("[" + uName + "] 님이 입장하셨습니다." + simple.format(now));

                        // 다른 클라이언트에서 들어오는 것 출력
                        for (int i = 0; i < server.clients.size(); i++) {
                            if (!(this.uName.equals(server.clients.get(i).uName))) {
                                // 다른 유저의 데이터 스트림을 받아옴
                                outStream = server.clients.get(i).socket.getOutputStream();
                                // 가져온 데이터로 dataOutStream 생성
                                dataOutStream = new DataOutputStream(outStream);
                                // writeUTF로 채팅 메세지 설정
                                dataOutStream.writeUTF("[" + this.uName + "] 님이 입장하셨습니다." + simple.format(now));
                                dataOutStream.writeUTF("USER//" + this.server.connectedUser);
                            }
                        }
                    } else {
                        dataOutStream.writeUTF("LOGIN_FAIL");
                    }
                    // 회원가입을 위한 정보를 받았다면
                } else if (token.equals((signTag))){
                    String id = stk.nextToken();
                    String pw = stk.nextToken();
                    String name = stk.nextToken();
                    String num = stk.nextToken();

                    // 유저 정보가 저장되어 있는 DB 파일 경로 지정해서 가져옴
                    File dataFile = new File("/Users/favorcat/Github/SMU-CSE20/Basic-Project2/GUI 멀티 채팅/MultiChattingServer/users.txt");
                    // 덮어쓰기가 아니라 아래에 추가할 수 있도록 append를 true로 해줌
                    Writer wr = new BufferedWriter(new FileWriter(dataFile, true));
                    wr.append(name).append("//").append(pw).append("//").append(name).append("//").append(num).append("\n");
                    wr.close();
                    // DB가 업데이트 되었으므로 서버가 들고있는 유저정보를 업데이트
                    lc.DBchecker();
                    dataOutStream.writeUTF("SIGNUP_OK");

                    // 만약 태그가 채팅이라면
                } else if (token.equals(chatTag)) {
                    // 태그 뒤에 바로 메세지가 옴
                    msg = stk.nextToken();
                    // 연결된 클라이언트들의 배열 중에서 다른 클라이언트가 보낸 것 검사
                    for (int i = 0; i < server.clients.size(); i++) {
                        if (!(this.uName.equals(server.clients.get(i).uName))) {
                            // 해당 클라이언트의 소켓에서 데이터를 가져옴
                            outStream = server.clients.get(i).socket.getOutputStream();
                            // 가져온 데이터로 dataOutStream 생성
                            dataOutStream = new DataOutputStream(outStream);
                            // writeUTF로 채팅 메세지 설정
                            dataOutStream.writeUTF("[" + this.uName + "] : " + msg + simple.format(now));
                            System.out.println("[" + this.uName + "] : " + msg + simple.format(now));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server> 클라이언트 연결 종료");
            e.printStackTrace();
            try {
                // 끊겼으니 퇴장하였다는 메세지 출력
                dataOutStream.writeUTF("[" + this.uName + "] 님이 퇴장하셨습니다." + simple.format(now));
                // 연결된 유저 리스트 목록에서 삭제
                server.connectedUser.remove(this.uName);
                // 업데이트 된 연결된 유저 목록 데이터 전송
                dataOutStream.writeUTF("USER//" + server.connectedUser);
            } catch (IOException ex) {
                ex.printStackTrace();
                // 연결된 유저 리스트 목록에서 삭제
                server.connectedUser.remove(this.uName);
            }
        }
    }
}