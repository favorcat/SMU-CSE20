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
        // 서버의 DBChecker 생성, DB의 연결된 유저 및 채팅방 목록 초기화
        server.dc = new DBChecker();
        server.dc.delChatRoomAll();
        server.dc.delConnectedUserAll();
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
    String loginTag = "LOGIN";          // 로그인
    String logoutTag = "LOGOUT";        // 로그아웃

    String findIdTag = "FID";           // 아이디 찾기
    String findPwTag = "FPW";           // 비밀번호 찾기
    String signTag = "SIGNUP";          // 회원가입

    int chatPath = 0;                   // 채팅방 경로
    String makeChatTag = "MC";          // 채팅방 생성
    String chatSearchTag = "SEARCH";    // 채팅방 입장을 위해, 최대인원 및 비밀번호 유무 검사
    String chatInTag = "CI";            // 채팅방 입장
    String chatOutTag = "CO";           // 채팅방 퇴장
    String chatTag = "CHAT";            // 채팅 메세지
    String chatPathTag = "PATH";

    String mainUserTag = "MAIN_USER";   // 대기실 접속자 목록
    String chatUserTag = "CHAT_USER";   // 채팅방 접속자 목록
    String chatListTag = "LIST";        // 채팅방 목록
    String nameTag = "NAME";            // 유저 닉네임

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

                // 로그인을 위한 정보 (아이디, 비밀번호)
                if (token.equals(loginTag)) {
                    String id = stk.nextToken();
                    String pass = stk.nextToken();
                    // 로그인을 위한 아이디와 암호 확인 작업 필요
                    String result = server.dc.loginCheck(id, pass);
                    if (!result.equals("")) {
                        dataOutStream.writeUTF("LOGIN_OK");
                        uName = result;
                        dataOutStream.writeUTF(nameTag + "//" + uName);
                        dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUserList(0));
                        // 대기실에 있는 다른 유저에게 변동사항 출력
                        outStreamToMain();
                    } else {
                        dataOutStream.writeUTF("LOGIN_FAIL");
                    }

                    // 로그아웃을 위한 정보 (닉네임)
                } else if (token.equals(logoutTag)) {
                    socket.close();

                    // 회원가입을 위한 정보 (아이디, 비밀번호, 이름, 연락처, 닉네임)
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

                    // 아이디 찾기를 위한 정보 (이름, 연락처)
                } else if (token.equals(findIdTag)){
                    String name = stk.nextToken();
                    String num = stk.nextToken();

                    String id = server.dc.findUserID(name, num);
                    if (!id.equals("")){ // 아이디 찾기 성공, 아이디 반환
                        dataOutStream.writeUTF(id);
                    } else {
                        dataOutStream.writeUTF("NF");
                    }

                    // 비밀번호 찾기를 위한 정보 (아이디, 이름, 연락처)
                } else if (token.equals(findPwTag)) {
                    String id = stk.nextToken();
                    String name = stk.nextToken();
                    String num = stk.nextToken();

                    String pw = server.dc.findUserPW(id, name, num);
                    if (!id.equals("")) { // 비밀번호 찾기 성공, 비밀번호 반환
                        dataOutStream.writeUTF(pw);
                    } else {
                        dataOutStream.writeUTF("NF");
                    }

                    // 채팅방 생성을 위한 정보 (채팅방 제목, 채팅방 비밀번호, 채팅방 최대인원)
                } else if (token.equals(makeChatTag)) {
                    String title = stk.nextToken();
                    String pw = stk.nextToken();
                    String num = stk.nextToken();

                    // 채팅방 번호(경로) 반환
                    int result = server.dc.makeChatRoom(title, pw, num, uName);
                    if (result != -1){
                        // 유저의 채팅 경로를 해당 채팅방으로 변경
                        chatPath = result;
                        // 채팅방 번호(경로) 출력
                        dataOutStream.writeUTF(chatPathTag + "//" + chatPath);
                        dataOutStream.writeUTF(chatPathTag + "//" + chatPath);
                        // 채팅방 접속자 목록 출력
                        dataOutStream.writeUTF(chatUserTag + "//" + chatPath + "//" + server.dc.getOnlineUserList(chatPath));
                        // 채팅방 입장 메세지 출력
                        dataOutStream.writeUTF(chatTag + "//" + chatPath + "//[" + this.uName + "] 님이 입장하셨습니다." + simple.format(now));
                        // 대기실에 있는 다른 유저에게 변동사항 출력
                        outStreamToMain();
                    } else {
                        dataOutStream.writeUTF("MakeRoom_FAIL");
                    }

                    // 채팅방 입장을 위해 채팅방 정보 요청 (채팅방 번호)
                } else if(token.equals(chatSearchTag)) {
                    // 채팅방 번호 int로 변환
                    int path = Integer.parseInt(stk.nextToken());
                    String outmsg = "";
                    if (server.dc.getChatRoomUserCNT(path)) { // 해당 채팅방의 접속 인원이 최대인원인지 검사
                        String result = server.dc.getChatRoomPW(path);
                        if (result.equals("NULL")) {
                            outmsg = "ROOM_PW_NULL";
                        } else {
                            outmsg = "ROOM_INSERT_PW";
                        }
                    } else {
                        outmsg = "ROOM_MAX";
                    }
                    // 결과 반환
                    dataOutStream.writeUTF(outmsg);
                    dataOutStream.writeUTF(outmsg);

                    // 채팅방 접속을 위한 정보 (채팅방 번호, 채팅방 비밀번호)
                } else if(token.equals(chatInTag)){
                    // 채팅방 번호 int로 변환
                    int path = Integer.parseInt(stk.nextToken());
                    String pw = stk.nextToken();

                    // 클라이언트가 요청한 채팅방 비밀번호가 설정된 채팅방 비밀번호와 일치할 경환
                    if(pw.equals(server.dc.getChatRoomPW(path))) {
                        // 채팅 경로 변경
                        chatPath = path;
                        // 유저의 경로 변경
                        server.dc.moveUserPath(uName, path);
                        // 채팅방 입장 가능하다는 메세지 전송
                        dataOutStream.writeUTF("ROOM_IN");
                        // 채팅방 경로 전송
                        dataOutStream.writeUTF(chatPathTag + "//" + chatPath);

                        // 해당 채팅방에 접속해 있는 유저 목록을 배열로 가져옴
                        String[] userArray = server.dc.getOnlineUserList(chatPath).split(", ");
                        // 배열에 존재하는 클라이언트에 모두 데이터 전송
                        for (int i = 0; i < server.clients.size(); i++) {
                            for (int j = 0; j < userArray.length; j++) {
                                if (userArray[j].equals(server.clients.get(i).uName)) {
                                    // 해당 클라이언트의 소켓에서 데이터를 가져옴
                                    outStream = server.clients.get(i).socket.getOutputStream();
                                    // 가져온 데이터로 dataOutStream 생성
                                    dataOutStream = new DataOutputStream(outStream);
                                    // 채팅방 접속자 목록 전송
                                    dataOutStream.writeUTF(chatUserTag + "//" + chatPath + "//" + server.dc.getOnlineUserList(chatPath));
                                    // 입장 메세지 전송
                                    dataOutStream.writeUTF(chatTag + "//" + chatPath + "//[" + this.uName + "] 님이 입장하셨습니다." + simple.format(now));
                                }
                            }
                        }
                        // 대기실에 있는 유저들에게 변동사항 출력
                        outStreamToMain();
                    } else { // 비밀번호가 일치하지 않을 경우
                        dataOutStream.writeUTF("ROOM_FAIL");
                        dataOutStream.writeUTF("ROOM_FAIL");
                    }

                    // 채팅 메세지 (채팅방 번호(경로), 채팅 메세지)
                } else if (token.equals(chatTag)) {
                    chatPath = Integer.parseInt(stk.nextToken());
                    msg = stk.nextToken();

                    // 해당 채팅방에 접속해 있는 유저 목록을 배열로 가져옴
                    String[] userArray = server.dc.getOnlineUserList(chatPath).split(", ");
                    // 배열에 존재하는 클라이언트에 모두 데이터 전송
                    for (int i = 0; i < server.clients.size(); i++) {
                        for (int j = 0; j < userArray.length; j++) {
                                if (userArray[j].equals(server.clients.get(i).uName)) {
                                    // 해당 클라이언트의 소켓에서 데이터를 가져옴
                                    outStream = server.clients.get(i).socket.getOutputStream();
                                    // 가져온 데이터로 dataOutStream 생성
                                    dataOutStream = new DataOutputStream(outStream);
                                    // 채팅 메세지 전송
                                    dataOutStream.writeUTF(chatTag + "//" + chatPath + "//[" + this.uName + "] : " + msg + simple.format(now));
                                    System.out.println(chatTag + "//" + chatPath + "//[" + this.uName + "] : " + msg + simple.format(now));
                                }

                            }
                        }

                        // 채팅방 퇴장 정보
                    } else if(token.equals(chatOutTag)) {
                        // 퇴장 하기 전 현재의 채팅방 경로 저장
                        int currentPath = chatPath;
                        chatPath = 0;
                        // 유저 대기실로 이동
                        server.dc.moveUserPath(uName, chatPath);
                        // 대기실에 있는 유저들에게 변동사항 출력
                        outStreamToMain();

                        // 퇴장 할 채팅방에 접속해 있는 유저 목록을 배열로 가져옴
                        String[] userArray = server.dc.getOnlineUserList(currentPath).split(", ");
                        // 배열에 존재하는 클라이언트에 모두 데이터 전송
                        for (int i = 0; i < server.clients.size(); i++) {
                            for (int j = 0; j < userArray.length; j++) {
                                if (userArray[j].equals(server.clients.get(i).uName)) {
                                    // 해당 클라이언트의 소켓에서 데이터를 가져옴
                                    outStream = server.clients.get(i).socket.getOutputStream();
                                    // 가져온 데이터로 dataOutStream 생성
                                    dataOutStream = new DataOutputStream(outStream);
                                    // 채팅방 접속자 목록 업데이트
                                    dataOutStream.writeUTF(chatUserTag + "//" + currentPath + "//" + server.dc.getOnlineUserList(currentPath));
                                    // 유저 퇴장 메세지 전송
                                    dataOutStream.writeUTF(chatTag + "//" + currentPath + "//[" + this.uName + "] 님이 퇴장하셨습니다." + simple.format(now));
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 연결된 유저 리스트 목록에서 삭제
                    server.dc.delConnectedUser(this.uName);
                    // 유저가 대기실에 있었을 경우, 대기실 접속자에서 삭제
                    outStreamToMain();
                    // 유저가 채팅방에 있었을 경우, 채팅방 접속자에서 삭제
                    if (chatPath != 0) {
                        // 퇴장 할 채팅방에 접속해 있는 유저 목록을 배열로 가져옴
                        String[] userArray = server.dc.getOnlineUserList(chatPath).split(", ");
                        // 배열에 존재하는 클라이언트에 모두 데이터 전송
                        for (int i = 0; i < server.clients.size(); i++) {
                            for (int j = 0; j < userArray.length; j++) {
                                if (userArray[j].equals(server.clients.get(i).uName)) {
                                    // 해당 클라이언트의 소켓에서 데이터를 가져옴
                                    outStream = server.clients.get(i).socket.getOutputStream();
                                    // 가져온 데이터로 dataOutStream 생성
                                    dataOutStream = new DataOutputStream(outStream);
                                    // 채팅방 접속자 목록 업데이트
                                    dataOutStream.writeUTF(chatUserTag + "//" + chatPath + "//" + server.dc.getOnlineUserList(chatPath));
                                    // 유저 퇴장 메세지 전송
                                    dataOutStream.writeUTF(chatTag + "//" + chatPath + "//[" + this.uName + "] 님이 퇴장하셨습니다." + simple.format(now));
                                }
                            }
                        }
                    }

                } catch (IOException ex) {
                    // 연결된 유저 리스트 목록에서 삭제
                    server.dc.delConnectedUser(this.uName);
                    try {
                        outStreamToMain();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ex.printStackTrace();
                }
                System.out.println("Server> 클라이언트 연결 종료");
            }
        }

        // 대기실에 접속해 있는 유저에게 접속자 목록, 채팅방 목록 전송을 위한 메소드
        private void outStreamToMain() throws IOException {
            // 대기실 접속자 목록 배열
            String[] mainUserArray = server.dc.getOnlineUserList(0).split(", ");
            // 배열에 존재하는 클라이언트에 모두 데이터 전송
            for (int i = 0; i < server.clients.size(); i++) {
                for (int j = 0; j < mainUserArray.length; j++) {
                    if (mainUserArray[j].equals(server.clients.get(i).uName)) {
                        // 해당 클라이언트의 소켓에서 데이터를 가져옴
                        outStream = server.clients.get(i).socket.getOutputStream();
                        // 가져온 데이터로 dataOutStream 생성
                        dataOutStream = new DataOutputStream(outStream);
                        // 가져온 데이터로 dataOutStream 생성
                        dataOutStream = new DataOutputStream(outStream);
                        for(int k=0; k< mainUserArray.length; k++){
                            // 채팅방 목록 업데이트
                            dataOutStream.writeUTF(chatListTag + "//LIST" + server.dc.getChatRoomList());
                            // 대기실 접속자 목록 업데이트
                            dataOutStream.writeUTF(mainUserTag + "//" + server.dc.getOnlineUserList(0));
                        }
                    }
                }
            }
        }
    }