import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class MyConnector {
    // 소켓 생성
    Socket mySocket = null;

    OutputStream outStream = null;
    DataOutputStream dataOutStream = null;
    InputStream inStream = null;
    DataInputStream dataInStream=null;

    // 보내는 문자열이 로그인인지 회원가입인지, 검색을 위한 것인지 태그
    final String loginTag = "LOGIN";
    final String signupTag = "SIGNUP";
    final String searchTag = "SEARCH";

    MyConnector(){
        try {
            // 서버와 같은 포트로 socket 생성
            mySocket = new Socket("localhost", 20000);
            System.out.println("CLIENT LOG> 서버로 연결되었습니다.");

            // socket에서 얻어온 입출력 스트림 반영
            outStream = mySocket.getOutputStream();
            dataOutStream = new DataOutputStream(outStream);
            inStream = mySocket.getInputStream();
            dataInStream =  new DataInputStream(inStream);

        }catch(Exception e) {
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
    boolean sendSignupInformation(String uid, String upass){
        String msg = "";
        try {
            dataOutStream.writeUTF(signupTag + "//" + uid + "//" + upass);
            // 서버로부터 받아온 회원가입 정보 추가 성공/실패의 boolean 값 받아움
            msg = dataInStream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        } // 회원가입 성공했으면 true값 반환
        return msg.equals("SIGNUP_OK");
    }

    // 학생을 검색하기 위한 정보 전송
    void search(String _department, int _year, int _grade){
        try {
            dataOutStream.writeUTF(searchTag + "//" + _department + "//" + _year + "//" + _grade);
            System.out.println("SEARCH 요청 >> " + searchTag + "//" + _department + "//" + _year + "//" + _grade);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

// 스레드를 활용한 클라이언트가 서버에서 보내는 메세지를 받기 위한 클래스
class MessageListener extends Thread {
    // 데이터를 불러오기 위해 선언
    DataInputStream dataInStream;
    // 메인플레임을 들고 있어야 메인프레임에 관여할 수 있음
    MainFrame mf;

    // 학과 정보를 담은 태그
    String depTag = "DEP";
    // 학생 정보를 담은 태그
    String studentTag = "STUDENT";

    // 생성자로 DataInputStream과 메인프레임 생성
    MessageListener(DataInputStream data, MainFrame _mf){
        dataInStream = data;
        mf = _mf;
    }

    @Override
    public void run() {
        try {
            while(true) {
                // 스레드가 계속 돌면서 데이터를 받아옴
                String msg = dataInStream.readUTF();
                System.out.println("msg >> " + msg);

                // 기본적으로 문자열들은 "//"로 구분되어 있으니 StringTokenizer 사용
                StringTokenizer stk = new StringTokenizer(msg, "//");
                // 문자열의 맨 앞은 Tag 문자열
                String token = stk.nextToken();

                // 로그인 시, 학과 정보를 담고 있는 데이터를 받았을 때
                if (token.equals(depTag)){
                    // 콤보박스, 학과를 담고있는 배열 초기화
                    mf.comboBox.removeAllItems();
                    mf.departments.clear();
                    // 콤보박스의 첫번째에는 모든 학과를 선택하는 것을 담음
                    mf.comboBox.addItem("ALL");

                    // 학과를 "//"로 구분하여 받아왔으므로, 토큰이 존재할때까지 반복
                    while(stk.hasMoreTokens()){
                        String data = stk.nextToken();
                        // 콤보박스에 학과 추가
                        mf.comboBox.addItem(data);

                        // 트리 노드에 학과를 추가하여 업데이트
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
                        mf.root.add(node);
                        mf.treeModel = (DefaultTreeModel) mf.tree.getModel();
                        mf.treeModel.setRoot(mf.root);

                        // 학과를 담고있는 배열에 학과 추가
                        mf.departments.add(data);
                    }

                    // 학생 정보를 담고 있는 데이터를 받았을 때
                } else if (token.equals(studentTag)){
                    // 테이블 초기화
                    mf.tableModel.setRowCount(0);
                    // 학생별로의 정보는 "//"로 구분되어서 받아옴
                    while(stk.hasMoreTokens()){ // 학생의 정보가 존재할때까지 반복
                        // 학생 개개인의 정보는 data로 담음
                        String data = stk.nextToken();
                        System.out.println("STUDENT>> " + data);
                        // 학생 개개인의 정보는 ", "로 구분되어 있음
                        StringTokenizer sk = new StringTokenizer(data, ", ");
                        // 테이블에 학생의 정보를 추가하기 위해 배열로 정보를 담음
                        String[] student = new String[5];
                        int cnt = 0;
                        while(sk.hasMoreTokens()){ // 칼럼이 총 5개이므로 5번 반복, 데이터는 not null이므로 토큰이 존재할 때까지 반복문을 돌려도 됨
                            student[cnt] = sk.nextToken();
                            cnt++;
                        }
                        // 학생 정보를 테이블에 추가
                        mf.tableModel.addRow(student);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}