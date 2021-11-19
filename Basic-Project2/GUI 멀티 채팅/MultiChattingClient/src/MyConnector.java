import java.io.*;
import java.net.*;

public class MyConnector {
    Socket mySocket = null;

    OutputStream outStream = null;
    DataOutputStream dataOutStream = null;

    InputStream inStream = null;
    DataInputStream dataInStream = null;

    //MessageListener ml = null;

    final String loginTag = "LOGIN";
    final String chatTag = "CHAT";

    MyConnector(){
        try {
            // socket 생성
            mySocket = new Socket("localhost", 60000);
            System.out.println("Client> 서버로 연결되었습니다.");

            // socket에서 얻어온 입출력 스트림 반영
            outStream = mySocket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = mySocket.getInputStream();
            dataInStream =  new DataInputStream(inStream);
            //ml = new MessageListener(dataInStream);
            //ml.start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    boolean sendLoginInformation(String uid, String upass){
        String msg = "";
        try {
            dataOutStream.writeUTF(loginTag + "//" + uid + "//" + upass);
            System.out.println("><");
            msg = dataInStream.readUTF();
            System.out.println(msg);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return msg.equals("LOGIN_OK");
    }

    void sendChat(String chat){
        try {
            dataOutStream.writeUTF(chatTag + "//" + chat);
            System.out.println("sendcAHT");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class MessageListener extends Thread {
    DataInputStream dataInStream;
    MainFrame mf;

    MessageListener(DataInputStream data, MainFrame _mf){
        dataInStream = data;
        mf = _mf;
    }

    @Override
    public void run() {
        try {
            System.out.println(mf.flag);
            //if (mf.flag){
                while(true) {
                    System.out.println("RURNRURN");
                    String msg = dataInStream.readUTF();
                    System.out.println(msg);
                    mf.textArea.append(msg);
                }
            //}
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}