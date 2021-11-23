import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 쓰레드 인터페이스 사용
public class LoginFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언
    JButton enter = new JButton("Login"); // 로그인 버튼  선언
    JButton signup = new JButton("SignUp"); // 취소 버튼 선언
    JTextField typeId = new JTextField(); // id 받은곳  선언
    JPasswordField typePassword = new JPasswordField(); // password 받은곳 선언 받으면 ** < 처럼 나옴
    JLabel id = new JLabel("I   D"); // 라벨 type id
    JLabel password = new JLabel("Password"); // 라벨 type password

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
    MyConnector connector;
    Operator mainOperator = null;

    public LoginFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 LOGIN");

        id.setPreferredSize(new Dimension(70, 30));
        typeId.setPreferredSize(new Dimension(300, 30));

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        signup.setPreferredSize(new Dimension(185, 30));

        panel.add(id); //  ID 추가
        panel.add(typeId); // 입력된 ID 추가

        panel.add(password); // PASSWORD 추가
        panel.add(typePassword); // 입력된 PASSWORD 추가

        panel.add(enter);
        panel.add(signup);

        setContentPane(panel);
        enter.addActionListener(ml); // Login 버튼에 이벤트 리스너 추가
        signup.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 150);

        //로그인 창을 화면 중앙에 배치시키기...
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        setVisible(true);
    }

    class MyActionListener implements ActionListener  {
        //이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();
            if (b.getText().equals("Login")) {  // 로그인버튼을 누르면...
                //Password 컴포넌트에서 문자열 읽어오기 1
                StringBuilder pw = new StringBuilder();
                for(int i=0; i<typePassword.getPassword().length; i++) {
                    pw.append(typePassword.getPassword()[i]);
                }
                System.out.println(typeId.getText()+ "//" + pw);
                if(connector.sendLoginInformation(typeId.getText(), pw.toString())) {
                    mainOperator.mf.flag = true; // 채팅 상태로 변경
                    mainOperator.mf.setVisible(true);
                    typeId.setText("");
                    typePassword.setText("");
                    dispose();
                    // 메인프레임에서 메세지리스너 시작
                    mainOperator.mf.ml.start();
                }else {
                    JOptionPane.showMessageDialog(null, "로그인 실패!");
                }

            }else if (b.getText().equals("SignUp")) {
                mainOperator.sf.setVisible(true);
                typeId.setText("");
                typePassword.setText("");
                dispose();
            }
        }
    }


}