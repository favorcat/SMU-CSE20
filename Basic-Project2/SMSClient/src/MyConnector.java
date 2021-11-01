import java.io.*;
import java.net.*;

public class MyConnector {
    Socket mySocket = null;
    OutputStream outStream = null;
    DataOutputStream dataOutStream = null;
    InputStream inStream = null;
    DataInputStream dataInStream=null;

    final String loginTag = "LOGIN";
    final String queryTag = "QUERY";

    MyConnector(){
        try {
            mySocket = new Socket("localhost", 20000);
            System.out.println("CLIENT LOG> 서버로 연결되었습니다.");
            outStream = mySocket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = mySocket.getInputStream();
            dataInStream =  new DataInputStream(inStream);
            Thread.sleep(100);
        }catch(Exception e) {

        }
    }

    boolean sendLoginInformation(String uid, String upass){
        boolean flag = false;
        String msg = null;
        try {
            dataOutStream.writeUTF(loginTag+ "//" + uid + "//"+upass);
            msg = dataInStream.readUTF();
        }catch(Exception e) {

        }

        if(msg.equals("LOGIN_OK")) {
            return true;
        }else {
            return false;
        }
    }

}