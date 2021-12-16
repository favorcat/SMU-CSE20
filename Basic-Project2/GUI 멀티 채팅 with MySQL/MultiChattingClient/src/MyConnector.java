import java.io.*;
import java.net.*;
import java.util.Vector;

public class MyConnector {
    // 소켓 생성
    Socket mySocket = null;

    OutputStream outStream = null;
    DataOutputStream dataOutStream = null;
    InputStream inStream = null;
    DataInputStream dataInStream = null;

    // 보내는 문자열이 로그인인지 채팅인지 태그를 달기 위해 선언
    String loginTag = "LOGIN";
    String findIdTag = "FID";
    String findPwTag = "FPW";
    String signTag = "SIGNUP";

    String chatTag = "CHAT";
    String makeChatTag = "MC";
    String chatInTag = "CI";
    String chatOutTag = "CO";

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

    // 로그인을 위한 정보 전송
    boolean sendLoginInformation(String uid, String upass){
        String msg = null;
        try {
            dataOutStream.writeUTF(loginTag+ "//" + uid + "//"+upass);
            msg = dataInStream.readUTF();
            System.out.println("로그인(서버응답): "+ msg);
        }catch(Exception e) {
            e.printStackTrace();
        } // 로그인 성공했으면 true값 반환
        return msg.equals("LOGIN_OK");
    }

    // 회원가입을 위한 정보 전송
    boolean sendSignupInformation(String uid, String upass, String uname, String unum, String unickname){
        String msg = "";
        try {
            dataOutStream.writeUTF(signTag + "//" + uid + "//" + upass + "//" + uname + "//" + unum + "//" + unickname);
            // 서버로부터 받아온 회원가입 정보 추가 성공/실패의 boolean 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        } // 회원가입 성공했으면 true값 반환
        return msg.equals("SIGNUP_OK");
    }

    String findIDInformation(String uname, String unum){
        String msg = "";
        try {
            dataOutStream.writeUTF(findIdTag + "//" + uname + "//" + unum);
            // 서버로부터 받아온 아이디 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    String findPWInformation(String uid, String uname, String unum){
        String msg = "";
        try {
            dataOutStream.writeUTF(findPwTag + "//" + uid + "//" + uname + "//" + unum);
            // 서버로부터 받아온 비밀번호 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    // 채팅창의 전송 버튼 누를 떄 실행할 메소드, 매개변수는 입력받은 텍스트
    void sendChat(String chat){
//        try {
//            // 입력받은 텍스트로 앞에 채팅 태그를 달아서 데이터 전송
//            dataOutStream.writeUTF(chatTag + "//" + chat);
//            System.out.println("sendCAHT");
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }
}

// 스레드를 활용한 클라이언트가 서버에서 보내는 채팅메세지를 받기 위한 클래스
class MessageListener extends Thread {
    // 데이터를 불러오기 위해 선언
    DataInputStream dataInStream;
    // 메인플레임을 들고 있어야 메인프레임의 textarea에 채팅메세지를 append할 수 있음
    MainFrame mf;
    ChatFrame cf;

    String nameTag = "NAME//";
    String userTag = "USER//";
    String [] userArray = null;

    // 생성자로 DataInputStream과 메인프레임 생성
    MessageListener(DataInputStream data, MainFrame _mf, ChatFrame _cf){
        dataInStream = data;
        mf = _mf;
        cf = _cf;
    }

    @Override
    public void run() {
//        try {
//            System.out.println("MainFrame flag >> " + cf.flag);
//            while(true) {
//                // 스레드가 계속 돌면서 데이터를 받아옴
//                String msg = dataInStream.readUTF();
//                // 유저 정보가 담긴 리스트가 온 지 검사를 하기 위해 문자열 앞을 잘라서 검사
//                String token = msg.substring(0,6);
//
//                if (token.equals(userTag)) {
//                    String user = msg.substring(7, msg.length()-1);
//                    userArray = user.split(", ");
//                    cf.vec = new Vector<String>();
//                    cf.vec.addElement("<<접속자 목록>>");
//                    for(int i=0; i< userArray.length; i++){
//                        cf.vec.addElement(userArray[i]);
//                    }
//                    cf.onlineUser.setListData(cf.vec);
//                } else if (token.equals(nameTag)) {
//                    cf.myName = msg.substring(6, msg.length());
//                } else {
//                    cf.textArea.append(msg+"\n");
//                }
//                System.out.println(msg);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

    }
}