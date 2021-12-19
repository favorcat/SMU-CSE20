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
    // 로그인 검사할 변수 설정
    DBChecker dc;

    public static void main(String[] args) {
        // 서버 생성
        Server server = new Server();
        // 서버의 DBChecker 생성
        server.dc = new DBChecker();
        server.dc.delRoomAll();
        server.dc.delConnectedAll();
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

    // 문자열을 확인하기 위한 태그
    String loginTag = "LOGIN";
    String findIdTag = "FID";
    String findPwTag = "FPW";
    String signTag = "SIGNUP";

    String chatTag = "CHAT";
    String makeChatTag = "MC";
    String chatPathTag = "PATH";
    int chatPath = 0;
    String chatInTag = "CI";
    String chatOutTag = "CO";

    String mainTag = "MAIN";
    String logoutTag = "LOGOUT";

    String nameTag = "NAME";
    String mainUserTag = "MAIN_USER";
    String chatUserTag = "CHAT_USER";

    // 생성자에 소켓과 서버를 넣어줌
    ConnectedClient(Socket _s, Server _ss){
        this.socket = _s;
        this.server = _ss;
    }

    @Override
    public void run() {
        DBChecker dc = new DBChecker();
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
                    String result = server.dc.check(id, pass);
                    if (!result.equals("")) {
                        dataOutStream.writeUTF("LOGIN_OK");
                        uName = result;
                        dataOutStream.writeUTF(nameTag + "//" + uName);
                        dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUser(0));

                        // 다른 클라이언트에서 들어오는 것 출력
                        for (int i = 0; i < server.clients.size(); i++) {
                            if (!(uName.equals(server.clients.get(i).uName))) {
                                // 다른 유저의 데이터 스트림을 받아옴
                                outStream = server.clients.get(i).socket.getOutputStream();
                                // 가져온 데이터로 dataOutStream 생성
                                dataOutStream = new DataOutputStream(outStream);
                                dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUser(0));
                            }
                        }
                    } else {
                        dataOutStream.writeUTF("LOGIN_FAIL");
                    }
                    // 회원가입을 위한 정보를 받았다면
                } else if (token.equals((signTag))) {
                    String id = stk.nextToken();
                    String pw = stk.nextToken();
                    String name = stk.nextToken();
                    String num = stk.nextToken();
                    String nickname = stk.nextToken();

                    // DB에 회원 정보 추가
                    if (server.dc.signupCheck(id, pw, name, num, nickname)) {
                        // 회원가입 성공
                        dataOutStream.writeUTF("SIGNUP_OK");
                    } else { // 회원가입 실패
                        dataOutStream.writeUTF("SIGNUP_FAIL");
                    }

                    // 만약 태그가 아이디 찾기라면
                } else if (token.equals(findIdTag)){
                    String name = stk.nextToken();
                    String num = stk.nextToken();

                    String id = server.dc.findID(name, num);
                    if (!id.equals("")){
                        dataOutStream.writeUTF(id);
                    } else {
                        dataOutStream.writeUTF("NF");
                    }

                    // 만약 태그가 비밀번호찾기라면
                } else if (token.equals(findPwTag)) {
                    String id = stk.nextToken();
                    String name = stk.nextToken();
                    String num = stk.nextToken();

                    String pw = server.dc.findPW(id, name, num);
                    if (!id.equals("")) {
                        dataOutStream.writeUTF(pw);
                    } else {
                        dataOutStream.writeUTF("NF");
                    }

                    // 만약 태그가 채팅방 생성이라면
                } else if (token.equals(makeChatTag)) {
                    String title = stk.nextToken();
                    String pw = stk.nextToken();
                    String num = stk.nextToken();

                    int result = server.dc.makeRoom(title, pw, num, uName);
                    if (result != -1){
                        chatPath = result;
                        dataOutStream.writeUTF(String.valueOf(chatPath));
                        dataOutStream.writeUTF(String.valueOf(chatPath));
                        dataOutStream.writeUTF(chatPathTag + "//" + chatPath);
                        dataOutStream.writeUTF(chatUserTag + "//" + chatPath + "//" + server.dc.getOnlineUser(result));
                        dataOutStream.writeUTF(chatUserTag + "//" + chatPath + "//" + server.dc.getOnlineUser(result));

                        for (int i = 0; i < server.clients.size(); i++) {
                            if (!(uName.equals(server.clients.get(i).uName))) {
                                // 다른 유저의 데이터 스트림을 받아옴
                                outStream = server.clients.get(i).socket.getOutputStream();
                                // 가져온 데이터로 dataOutStream 생성
                                dataOutStream = new DataOutputStream(outStream);
                                dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUser(0));
                            }
                        }
                    }
                    else {
                        dataOutStream.writeUTF("MakeRoom_FAIL");
                    }

                    // 만약 태그가 채팅이라면
                } else if (token.equals(chatTag)) {
                    chatPath = Integer.parseInt(stk.nextToken());
                    // Path 뒤에 바로 메세지가 옴
                    msg = stk.nextToken();

                    String [] userArray = server.dc.getOnlineUser(chatPath).split(", ");

                    // 연결된 클라이언트들의 배열 중에서 다른 클라이언트가 보낸 것 검사
                    for (int i = 0; i < server.clients.size(); i++) {
                        for (int j=0; j < userArray.length; j++){
                            if (userArray[j].equals(server.clients.get(i).uName)) {
                                // 해당 클라이언트의 소켓에서 데이터를 가져옴
                                outStream = server.clients.get(i).socket.getOutputStream();
                                // 가져온 데이터로 dataOutStream 생성
                                dataOutStream = new DataOutputStream(outStream);
                                // writeUTF로 채팅 메세지 설정
                                dataOutStream.writeUTF(chatTag + "//" + chatPath + "//[" + this.uName + "] : " + msg + simple.format(now));
                                System.out.println("[" + this.uName + "] : " + msg + simple.format(now));
                            }

                        }
                    }
                } else if(token.equals(mainTag)) {
                    server.dc.moveUserPath(uName, 0);
                    dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUser(0));

                } else if (token.equals(logoutTag)) {
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("갑자기???");
            e.printStackTrace();
        } finally {
            try {
                // 연결된 유저 리스트 목록에서 삭제
                dc.delConnected(this.uName);
                // 유저가 대기실에 있었을 경우, 대기실 접속자에서 삭제
                for (int i = 0; i < server.clients.size(); i++) {
                    if (!(uName.equals(server.clients.get(i).uName))) {
                        // 다른 유저의 데이터 스트림을 받아옴
                        outStream = server.clients.get(i).socket.getOutputStream();
                        // 가져온 데이터로 dataOutStream 생성
                        dataOutStream = new DataOutputStream(outStream);
                        dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUser(0));
                    }
                }
                // 유저가 채팅방에 있었을 경우, 채팅방 접속자에서 삭제

            } catch (IOException ex) {
                // 연결된 유저 리스트 목록에서 삭제
                dc.delConnected(this.uName);
                ex.printStackTrace();
            }
            System.out.println("Server> 클라이언트 연결 종료");
        }
    }
}