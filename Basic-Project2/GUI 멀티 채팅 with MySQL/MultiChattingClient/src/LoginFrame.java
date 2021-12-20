import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 쓰레드 인터페이스 사용
public class LoginFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("로그인");        // 로그인 버튼
    JButton signup = new JButton("회원가입");     // 회원가입 버튼

    JButton findid = new JButton("ID 찾기");      // ID 찾기 버튼
    JButton findpw = new JButton("PW 찾기");      // PW 찾기 버튼

    JTextField typeId = new JTextField();               // ID 입력 받는 곳
    JPasswordField typePassword = new JPasswordField(); // password 입력 받는 곳 -> ** 처럼 나옴

    JLabel id = new JLabel("아이디");             // typeId 라벨
    JLabel password = new JLabel("비밀번호");     // typePassword 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능 구현 가능
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

        panel.add(id);           // ID 라벨 추가
        panel.add(typeId);       // ID 입력 받는 곳 추가

        panel.add(password);     // password 라벨 추가
        panel.add(typePassword); // password 입력 받는 곳 추가

        panel.add(enter);       // 로그인 버튼 추가
        panel.add(signup);      // 회원가입 버튼 추가

        panel.add(findid);      // 아이디 찾기 버튼 추가
        panel.add(findpw);      // 비밀번호 찾기 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml);    // 로그인 버튼에 이벤트 리스너 추가
        signup.addActionListener(ml);   // 회원가입 버튼에 이벤트 리스너 추가
        findid.addActionListener(ml);   // 아이디 찾기 버튼에 이벤트 리스너 추가
        findpw.addActionListener(ml);   // 비밀번호 찾기 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 180);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   // 프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        setVisible(true);       // 가장 먼저 로그인 창이 활성화
    }

    class MyActionListener implements ActionListener  {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 로그인 버튼을 눌렀을 경우
            if (b.getText().equals("로그인")) {
                // Password 컴포넌트에서 문자열 읽어오기
                String pw = "";
                for(int i=0; i<typePassword.getPassword().length; i++) {
                    pw += typePassword.getPassword()[i];
                }

                // 모든 곳이 입력 되었는지 검사
                if (!typeId.getText().equals("") && !pw.equals("")) {
                    // 로그인에 성공했다면
                    if (connector.sendLoginInformation(typeId.getText(), pw.toString())) {
                        // 대기실 플래그 true
                        mainOperator.mf.flag = true;
                        // 대기실 프레임 활성화
                        mainOperator.mf.setVisible(true);
                        // 입력 칸 초기화
                        typeId.setText("");
                        typePassword.setText("");
                        // 로그인 프레임 닫기
                        dispose();

                        // 메인 프레임에서 메세지리스너 시작
                        mainOperator.mf.ml.start();
                    } else {
                        // 로그인 실패의 경우 경고창 생성
                        JOptionPane.showMessageDialog(null, "로그인 실패!", "로그인", JOptionPane.ERROR_MESSAGE);
                    }
                    // 모든 항목을 입력하지 않고 정보를 넘길 경우, 클라이언트 에러가 생김
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "로그인", JOptionPane.ERROR_MESSAGE); }

                // 회원가입 버튼을 눌렀을 경우
            } else if (b.getText().equals("회원가입")) {
                // 회원가입 프레임 활성화
                mainOperator.sf.setVisible(true);
                // 입력 칸 초기화
                typeId.setText("");
                typePassword.setText("");
                // 로그인 프레임 닫기
                dispose();

                // 아이디 찾기 버튼을 눌렀을 경우
            } else if (b.getText().equals("ID 찾기")) {
                // 아이디 찾기 프레임 활성화
                mainOperator.fidf.setVisible(true);
                // 입력 칸 초기화
                typeId.setText("");
                typePassword.setText("");
                // 로그인 프레임 닫기
                dispose();

                // 비밀번호 찾기 버튼을 눌렀을 경우
            } else if (b.getText().equals("PW 찾기")) {
                // 비밀번호 찾기 프레임 활성화
                mainOperator.fpwf.setVisible(true);
                // 입력 칸 초기화
                typeId.setText("");
                typePassword.setText("");
                // 로그인 프레임 닫기
                dispose();
            }
        }
    }
}