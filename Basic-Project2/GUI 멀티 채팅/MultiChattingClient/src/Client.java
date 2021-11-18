import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    Socket mySocket = null;
    MainFrame mf = null;

    public static void main(String[] args) {
        Client client = new Client();
        client.mf = new MainFrame();
        MessageListener msgListener = null;

        OutputStream outStream = null;
        DataOutputStream dataOutStream = null;
        Scanner scn = new Scanner(System.in);

        try {
            client.mySocket = new Socket("localhost", 60000);
            System.out.println("Client> 서버로 연결되었습니다.");
            msgListener = new MessageListener(client.mySocket);
            msgListener.start();

            outStream = client.mySocket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);

            System.out.println("Enter Your Name : ");
            String name = scn.nextLine();
            dataOutStream.writeUTF(name);

            while(true) {
                String msg = null;
                System.out.println("Enter Message> ");
                msg = scn.nextLine();
                dataOutStream.writeUTF(msg);
            }
        } catch(Exception e) {

        }
    }
}

class MessageListener extends Thread {
    Socket socket;
    InputStream inStream;
    DataInputStream dataInStream;
    MainFrame mainF;

    MessageListener(Socket _s, MainFrame _mf){
        socket = _s;
        mf = _mf;
    }

    @Override
    public void run() {
        try {
            inStream = this.socket.getInputStream();
            dataInStream = new DataInputStream(inStream);

            while(true) {
                String msg = dataInStream.readUTF();
                System.out.println(msg);
            }

        } catch(Exception e) {

        }

    }
}