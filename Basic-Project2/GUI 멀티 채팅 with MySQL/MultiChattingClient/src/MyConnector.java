import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class MyConnector {
    // 소켓 생성
    Socket mySocket = null;

    OutputStream outStream = null;
    DataOutputStream dataOutStream = null;
    InputStream inStream = null;
    DataInputStream dataInStream = null;

    // 보내는 문자열의 종류를 구분하기 위해 태그 선언
    String loginTag = "LOGIN";          // 로그인
    String logoutTag = "LOGOUT";        // 로그아웃

    String findIdTag = "FID";           // 아이디 찾기
    String findPwTag = "FPW";           // 비밀번호 찾기
    String signTag = "SIGNUP";          // 회원가입

    String makeChatTag = "MC";          // 채팅방 생성
    String chatPathTag = "PATH";        // 채팅방 번호
    String chatSearchTag = "SEARCH";    // 채팅방 입장을 위해, 최대인원 및 비밀번호 유무 검사
    String chatInTag = "CI";            // 채팅방 입장
    String chatOutTag = "CO";           // 채팅방 퇴장
    String chatTag = "CHAT";            // 채팅 메세지

    MyConnector(){
        try {
            // 서버와 같은 포트로 socket 생성
            mySocket = new Socket("localhost", 60000);
            System.out.println("Client> 서버로 연결되었습니다.");

            // socket에서 얻어온 입출력 스트림 반영
            outStream = mySocket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = mySocket.getInputStream();
            dataInStream =  new DataInputStream(inStream);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 로그인을 위한 정보 전송 (아이디, 비밀번호)
    boolean sendLoginInformation(String uid, String upass){
        String msg = "";
        try {
            dataOutStream.writeUTF(loginTag+ "//" + uid + "//"+upass);
            msg = dataInStream.readUTF();
            System.out.println("로그인(서버응답): "+ msg);
        }catch(Exception e) {
            e.printStackTrace();
        } // 로그인 성공했으면 true값 반환
        return msg.equals("LOGIN_OK");
    }

    // 로그아웃을 위한 정보 전송 (닉네임)
    void logOut(String _name){
        try {
            dataOutStream.writeUTF(logoutTag + "//" + _name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 회원가입을 위한 정보 전송 (아이디, 비밀번호, 이름, 연락처, 닉네임)
    boolean sendSignupInformation(String uid, String upass, String uname, String unum, String unickname){
        String msg = "";
        try {
            dataOutStream.writeUTF(signTag + "//" + uid + "//" + upass + "//" + uname + "//" + unum + "//" + unickname);
            // 서버로부터 받아온 회원가입 정보 문자열을 받아옴
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        } // 회원가입 성공했으면 true값 반환
        return msg.equals("SIGNUP_OK");
    }

    // 아이디 찾기를 위한 정보 전송 (이름, 연락처)
    String findIDInformation(String uname, String unum){
        String msg = "";
        try {
            dataOutStream.writeUTF(findIdTag + "//" + uname + "//" + unum);
            // 서버로부터 받아온 아이디 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        } // 아이디 값 반환
        return msg;
    }

    // 비밀번호 찾기를 위한 정보 전송 (아이디, 이름, 연락처)
    String findPWInformation(String uid, String uname, String unum){
        String msg = "";
        try {
            dataOutStream.writeUTF(findPwTag + "//" + uid + "//" + uname + "//" + unum);
            // 서버로부터 받아온 비밀번호 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        } // 비밀번호 값 반환
        return msg;
    }

    // 채팅방 생성을 위한 정보 전송 (채팅방 제목, 채팅방 비밀번호, 최대인원)
    String makeRoomInformation(String _title, String _pw, String _num){
        String msg = "";
        try {
            if (_pw.equals("")) _pw = "NULL";   // 비밀번호를 입력하지 않았다면 NULL 전송
            if (_num.equals("")) _num = "5";    // 최대인원을 설정하지 않았다면 기본값 5 전송
            dataOutStream.writeUTF(makeChatTag + "//" + _title + "//" +_pw + "//" +_num);
            msg = dataInStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringTokenizer stk = new StringTokenizer(msg, "//");
        if (stk.hasMoreTokens()) {
            if (stk.nextToken().equals(chatPathTag)) msg = stk.nextToken();
        }
        return msg;
    }

        // 채팅방 입장을 위해 채팅방 정보 요청 (채팅방 번호)
    String searchRoom(String _path){
        String msg = "";
        try {
            dataOutStream.writeUTF(chatSearchTag + "//" + _path);
            msg = dataInStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } // 채팅방 비밀번호 유무 및 최대인원인지에 대한 문자열 반환
        return msg;
    }

    // 채팅방 입장을 위한 정보 전송 (채팅방 번호, 채팅방 비밀번호)
    String enterRoom(String _path, String _pw){
        String msg = "";
        try {
            dataOutStream.writeUTF(chatInTag + "//" + _path + "//" +_pw);
            msg = dataInStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } // 입장 가능한지에 대한 문자열 반환
        return msg;
    }

    // 채팅 메세지 전송 (채팅 메세지, 채팅방 번호)
    void sendChat(String _chat, String _path){
        try {
            // 입력받은 텍스트로 앞에 채팅 태그를 달아서 데이터 전송
            dataOutStream.writeUTF(chatTag + "//" + _path + "//" + _chat);
            System.out.println(chatTag + "//" + _path + "//" + _chat);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 채팅방 퇴장 정보 전송 (닉네임)
    void exitRoom(String _nickname){
        try {
            dataOutStream.writeUTF(chatOutTag + "//" + _nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 스레드를 활용한 클라이언트가 서버에서 보내는 채팅메세지를 받기 위한 클래스
class MessageListener extends Thread {
    // 데이터를 불러오기 위해 선언
    DataInputStream dataInStream;
    // 메인플레임을 들고 있어야 메세지를 append할 수 있음
    MainFrame mf;

    String nameTag = "NAME";            // 닉네임
    String mainUserTag = "MAIN_USER";   // 대기실 접속자 목록
    String chatUserTag = "CHAT_USER";   // 채팅방 접속자 목록
    String chatListTag = "LIST";        // 채팅방 목록
    String chatPathTag = "PATH";        // 채팅방 번호
    String chatTag = "CHAT";            // 채팅 메세지
    String [] userArray = null;         // 접속자 목록을 저장할 배열

    // 생성자로 DataInputStream과 메인프레임 생성
    MessageListener(DataInputStream data, MainFrame _mf) {
        dataInStream = data;
        mf = _mf;
    }
    @Override
    public void run() {
        try {
            while (true) {
                if (mf.flag) {
                    // 스레드가 계속 돌면서 데이터를 받아옴
                    String msg = dataInStream.readUTF();
                    // 기본적으로 문자열들은 "//"로 구분되어 있으니 StringTokenizer 사용
                    StringTokenizer stk = new StringTokenizer(msg, "//");
                    System.out.println(msg);

                    // 문자열의 앞은 태그가 붙어서 오니 검사
                    String token = stk.nextToken();
                    if (stk.hasMoreTokens()) {
                        // 대기실 접속자 목록을 받았을 때
                        if (token.equals(mainUserTag)) {
                            String user = stk.nextToken();
                            userArray = user.split(", ");
                            mf.vec = new Vector<String>();
                            mf.vec.addElement("<<접속자 목록>>");
                            for (String s : userArray) {
                                mf.vec.addElement(s);
                            }
                            mf.onlineUser.setListData(mf.vec);

                            // 유저 닉네임 받았을 때
                        } else if (token.equals(nameTag)) {
                            mf.myName = stk.nextToken();            // 유저 닉네임 설정
                            mf.mainOperator.cf.myName = mf.myName;  // 채팅방 닉네임 설정
                            mf.nameArea.setText("\t" + mf.myName);  // 대기실에 보여지는 닉네임 설정

                            // 채팅 목록을 받았을 때
                        } else if (token.equals(chatListTag)) {
                            // 테이블 초기화
                            mf.tableModel.setRowCount(0);
                            stk.nextToken();
                            // 채팅방의 정보는 "//"로 구분되어서 받아옴
                            while(stk.hasMoreTokens()) { // 정보가 존재할때까지 반복
                                // 채팅방 개개인의 정보는 data로 담음
                                String data = stk.nextToken();
                                // 채팅방 개개인의 정보는 ", "로 구분되어 있음
                                StringTokenizer sk = new StringTokenizer(data, ", ");
                                // 테이블에 채팅방 정보를 추가하기 위해 배열로 정보를 담음
                                String[] chatList = new String[4];
                                int cnt = 0;
                                // 칼럼이 총 4개이므로 4번 반복
                                // 데이터는 not null이므로 토큰이 존재할 때까지 반복문을 돌려도 됨
                                while (sk.hasMoreTokens()) {
                                    chatList[cnt] = sk.nextToken();
                                    cnt++;
                                }
                                // 채팅방 정보를 테이블에 추가
                                mf.tableModel.addRow(chatList);
                            }

                            // 채팅방 접속자 목록을 받았을 때
                        } else if(token.equals(chatUserTag)) {
                            String path = stk.nextToken();
                            // 채팅 메세지의 경로와 현재 유저의 접속한 경로와 일치할 때,
                            if (mf.mainOperator.cf.chatPath.equals(path)) {
                                String user = stk.nextToken();
                                userArray = user.split(", ");
                                mf.mainOperator.cf.vec = new Vector<String>();
                                mf.mainOperator.cf.vec.addElement("<<접속자 목록>>");
                                for (String s : userArray) {
                                    mf.mainOperator.cf.vec.addElement(s);
                                }
                                mf.mainOperator. cf.onlineUser.setListData(mf.mainOperator.cf.vec);
                            }

                            // 채팅방 경로를 받았을 때
                        } else if (token.equals(chatPathTag)) {
                            // 채팅방 경로가 0이면 변경
                            if (mf.mainOperator.cf.chatPath.equals("0")) {
                                mf.mainOperator.cf.chatPath = stk.nextToken();
                            }

                            // 채팅 메세지를 받았을 때
                        } else if (token.equals(chatTag)) {
                            // 채팅 경로와 메세지의 경로가 같으면 채팅방 프레임의 textArea에 메세지를 append
                            if (mf.mainOperator.cf.chatPath.equals(stk.nextToken())) {
                                mf.mainOperator.cf.textArea.append(stk.nextToken()+"\n");
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}