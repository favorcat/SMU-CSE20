import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 쓰레드 인터페이스 사용
public class RoomPWFrame extends JFrame {
    String title = "";      // 들어갈 채팅방의 제목
    String path = "";       // 들어갈 채팅방의 경로

    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("확인");  // 확인 버튼
    JButton cancel = new JButton("취소"); // 취소 버튼

    JPasswordField typePassword = new JPasswordField(); // 채팅방 password 입력 받는 곳 -> ** 처럼 나옴
    JLabel password = new JLabel("비밀번호");     // typePassword 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 채팅방 비밀번호 입력 기능 구현 가능
    MyConnector connector;
    Operator mainOperator = null;

    public RoomPWFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(password);        // 채팅방 비밀번호 라벨 추가
        panel.add(typePassword);    // 채팅방 비밀번호 입력 받는 곳 추가

        panel.add(cancel);          // 취소 버튼 추가
        panel.add(enter);           // 확인 버튼 추가

        setContentPane(panel);
        cancel.addActionListener(ml); // 취소 버튼에 이벤트 리스너 추가
        enter.addActionListener(ml);  // 확인 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 100);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener  {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 확인 버튼을 눌렀을 경우
            if (b.getText().equals("확인")) {
                //Password 컴포넌트에서 문자열 읽어오기
                String pw = "";
                for(int i=0; i<typePassword.getPassword().length; i++) {
                    pw += typePassword.getPassword()[i];
                }

                // 채팅방 입장을 위한 정보 전송을 통해 받아온 문자열
                String enterResult = connector.enterRoom(path, pw);
                // 입장 성공일 경우
                if (!enterResult.equals("ROOM_FAIL")){
                    // 대기실 프레임 비활성화
                    mainOperator.mf.setVisible(false);
                    // 채팅방 경로를 해당 경로로 변경
                    mainOperator.cf.chatPath = path;
                    // 채팅방 프레임 활성화
                    mainOperator.cf.setVisible(true);
                    // 채팅방 프레임 타이틀 -> [방 반호] 채팅방 제목
                    mainOperator.cf.setTitle("[" + path + "] " + title);
                    // 입력 칸 초기화
                    typePassword.setText("");
                    // 채팅방 비밀번호 입력 프레임 닫기
                    dispose();
                } else { // 입장 실패일 경우
                    // 입력 칸 초기화
                    typePassword.setText("");
                    // 팝업
                    JOptionPane.showMessageDialog(null, "채팅방 비밀번호 불일치", title, JOptionPane.ERROR_MESSAGE);
                }

                // 취소 버튼을 눌렀을 경우
            } else if (b.getText().equals("취소")) {
                // 입력 칸 초기화
                typePassword.setText("");
                // 채팅방 비밀번호 입력 프레임 닫기
                dispose();
            }
        }
    }
}