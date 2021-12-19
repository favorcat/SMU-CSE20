import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 쓰레드 인터페이스 사용
public class LoginFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("로그인"); // 로그인 버튼  선언
    JButton signup = new JButton("회원가입"); // 취소 버튼 선언

    JButton findid = new JButton("ID 찾기");
    JButton findpw = new JButton("PW 찾기");

    JTextField typeId = new JTextField(); // id 받은곳  선언
    JPasswordField typePassword = new JPasswordField(); // password 받은곳 선언 받으면 ** < 처럼 나옴

    JLabel id = new JLabel("아이디"); // 라벨 type id
    JLabel password = new JLabel("비밀번호"); // 라벨 type password

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
    MyConnector connector;
    Operator mainOperator = null;

    public LoginFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 - 로그인");

        id.setPreferredSize(new Dimension(70, 30));
        typeId.setPreferredSize(new Dimension(300, 30));

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        signup.setPreferredSize(new Dimension(185, 30));

        findid.setPreferredSize(new Dimension(185, 30));
        findpw.setPreferredSize(new Dimension(185, 30));

        panel.add(id); //  ID 추가
        panel.add(typeId); // 입력된 ID 추가

        panel.add(password); // PASSWORD 추가
        panel.add(typePassword); // 입력된 PASSWORD 추가

        panel.add(enter);
        panel.add(signup);

        panel.add(findid);
        panel.add(findpw);

        setContentPane(panel);
        enter.addActionListener(ml); // Login 버튼에 이벤트 리스너 추가
        signup.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가
        findid.addActionListener(ml);
        findpw.addActionListener(ml);

        setResizable(false);
        setSize(400, 180);

        //로그인 창을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        setVisible(true);
    }

    class MyActionListener implements ActionListener  {
        //이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 로그인 버튼 눌렀을 경우
            if (b.getText().equals("로그인")) {
                //Password 컴포넌트에서 문자열 읽어오기
                String pw = "";
                for(int i=0; i<typePassword.getPassword().length; i++)
                {   pw += typePassword.getPassword()[i];   }

                if (!typeId.getText().equals("") && !pw.equals("")) {

                    System.out.println(typeId.getText() + "//" + pw);
                    // 로그인에 성공했다면
                    if (connector.sendLoginInformation(typeId.getText(), pw.toString())) {
                        mainOperator.mf.flag = true; // 채팅 상태로 변경
                        mainOperator.mf.setVisible(true); // 채팅창 활성화
                        typeId.setText("");
                        typePassword.setText("");
                        dispose();
                        // 메인프레임에서 메세지리스너 시작
                        mainOperator.mf.ml.start();
                    } else {
                        // 로그인 실패의 경우 경고창 생성
                        JOptionPane.showMessageDialog(null, "로그인 실패!", "로그인", JOptionPane.ERROR_MESSAGE);
                    }
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "로그인", JOptionPane.ERROR_MESSAGE); }
            // 회원가입 버튼을 눌렀을 경우
            } else if (b.getText().equals("회원가입")) {
                // 회원가입창을 활성화 하고 현재 입력되어있는 ID,PW는 빈칸으로 처리, 로그인창 닫기
                mainOperator.sf.setVisible(true);
                typeId.setText("");
                typePassword.setText("");
                dispose();

            } else if (b.getText().equals("ID 찾기")){
                mainOperator.fidf.setVisible(true);
                typeId.setText("");
                typePassword.setText("");
                dispose();

            } else if (b.getText().equals("PW 찾기")){
                mainOperator.fpwf.setVisible(true);
                typeId.setText("");
                typePassword.setText("");
                dispose();
            }
        }
    }


}