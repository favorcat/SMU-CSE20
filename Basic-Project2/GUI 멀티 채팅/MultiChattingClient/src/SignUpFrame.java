import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 쓰레드 인터페이스 사용
public class SignUpFrame extends JFrame implements Runnable{
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("SignUp"); // 회원가입 버튼  선언
    JButton cancel = new JButton("Cancel"); // 취소 버튼 선언

    JTextField typeId = new JTextField(""); // id 받은곳 선언
    JPasswordField typePassword = new JPasswordField(""); // password 받은곳 선언 받으면 ** < 처럼 나옴
    JTextField typeName = new JTextField(""); // 이름 받은 곳 선언
    JTextField typeNum = new JTextField(""); // 연락처 받은 곳 선언

    JLabel id = new JLabel("I   D"); // 라벨 type id
    JLabel password = new JLabel("Password"); // 라벨 type password
    JLabel name = new JLabel("이   름"); // 라벨 type name
    JLabel num = new JLabel("연락처"); // 라벨 type num

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
    MyConnector connector;
    Operator mainOperator;

    public SignUpFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 Sign Up");

        id.setPreferredSize(new Dimension(70, 30));
        typeId.setPreferredSize(new Dimension(300, 30));

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        name.setPreferredSize(new Dimension(70, 30));
        typeName.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(id);              //  ID 추가
        panel.add(typeId);          // 입력된 ID 추가

        panel.add(password);        // PASSWORD 추가
        panel.add(typePassword);    // 입력된 PASSWORD 추가

        panel.add(name);            // 이름(닉네임) 추가
        panel.add(typeName);        // 입력된 이름 추가

        panel.add(num);             // 연락처 추가 
        panel.add(typeNum);         // 입력된 연락처 추가
    
        panel.add(enter);           // 회원가입 버튼 추가
        panel.add(cancel);          // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml); // Login 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 220);

        //회원가입 창을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        setVisible(false);
    }

    @Override
    public void run() {

    }

    class MyActionListener implements ActionListener  {
        //이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();
            // 로그인 버튼 눌렀을 경우
            if (b.getText().equals("SignUp")) {
                //Password 컴포넌트에서 문자열 읽어오기
                StringBuilder pw = new StringBuilder();
                for(int i=0; i<typePassword.getPassword().length; i++) {
                    pw.append(typePassword.getPassword()[i]);
                }
                System.out.println(typeId.getText()+ "//" + pw + "//" + typeName.getText() + "//" + typeNum.getText());
                // 회원가입 성공하면 채워넣은 정보들 빈칸으로 만든 후, 로그인창 활성화 및 회원가입창 닫기
                if(connector.sendSignupInformation(typeId.getText(), pw.toString(), typeName.getText(), typeNum.getText())) {
                    mainOperator.lf.setVisible(true);
                    typeId.setText("");
                    typePassword.setText("");
                    typeName.setText("");
                    typeNum.setText("");
                    dispose();
                    // 회원가입 성공했다는 경고창 띄우기
                    JOptionPane.showMessageDialog(null, "회원가입 성공!");
                }else {
                    // 회원가입 실패했을 경우 채워넣은 정보들 빈칸으로 만든 후 경고창 띄우기
                    typeId.setText("");
                    typePassword.setText("");
                    typeName.setText("");
                    typeNum.setText("");
                    JOptionPane.showMessageDialog(null, "회원가입 실패!");
                }

                // 취소 버튼 눌렀을 경우
            }else if (b.getText().equals("Cancel")) {
                // 채워넣은 정보들 빈칸으로 만듦
                typeId.setText("");
                typePassword.setText("");
                typeName.setText("");
                typeNum.setText("");

                // 로그인창 활성화 및 회원가입창 닫기
                mainOperator.lf.setVisible(true);
                dispose();
            }
        }
    }


}