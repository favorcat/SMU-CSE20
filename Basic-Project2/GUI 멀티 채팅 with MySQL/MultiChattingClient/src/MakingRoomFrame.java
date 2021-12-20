import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class MakingRoomFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("생성");  // 채팅방 생성 버튼
    JButton cancel = new JButton("취소"); // 취소 버튼

    JTextField typeTitle = new JTextField("");  // 채팅방 제목 입력 받는 곳
    JPasswordField typePassword = new JPasswordField(""); // 채팅방 password 입력 받는 곳 -> ** 처럼 나옴
    JTextField typeNum = new JTextField(""); // 최대인원 받는 곳

    JLabel title= new JLabel("방 제목");       // typeTitle 라벨
    JLabel password = new JLabel("비밀번호");  // typePassword 라벨
    JLabel num = new JLabel("최대인원");       // typeNum 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 채팅방 생성 기능 구현 가능
    MyConnector connector;
    Operator mainOperator = null;

    public MakingRoomFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 - 채팅방 생성");

        title.setPreferredSize(new Dimension(70, 30));
        typeTitle.setPreferredSize(new Dimension(300, 30));

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(title);           // 채팅방 제목 라벨 추가
        panel.add(typeTitle);       // 채팅방 제목 입력 받는 곳 추가

        panel.add(password);        // 채팅방 비밀번호 라벨 추가
        panel.add(typePassword);    // 채팅방 비밀번호 입력 받는 곳 추가

        panel.add(num);             // 채팅방 최대인원 라벨 추가
        panel.add(typeNum);         // 채팅방 최대인원 입력 받는 곳 추가

        panel.add(enter);           // 채팅방 생성 버튼 추가
        panel.add(cancel);          // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml); // 채팅방 생성 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // 취소 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 180);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   // 프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener  {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 채팅방 생성 버튼을 눌렀을 경우
            if (b.getText().equals("생성")) {
                // 생성 가능한 지를 나타내 줄 플래그
                boolean flag = true;
                // 채팅방 제목이 입력 되었는지 검사
                if (!typeTitle.getText().equals("")) {
                    // 정규식
                    String pattern = "^[A-Za-z0-9가-힣]*$";
                    String pw_pattern = "^[A-Za-z0-9]*$";
                    String num_pattern = "\\d";
                    // Password 컴포넌트에서 문자열 읽어오기
                    String pw = "";
                    for(int i=0; i<typePassword.getPassword().length; i++) {
                        pw += typePassword.getPassword()[i];
                    }
                    if (Pattern.matches(pattern, typeTitle.getText())) { // 채팅방 제목 정규식 검사
                        // 채팅방 비밀번호가 입력 되었을 경우
                        if (!pw.equals("")) {
                            if (!Pattern.matches(pw_pattern, pw)) { // 채팅방 비밀번호 정규식 검사
                                // 정규식과 일치하지 않다면
                                // 비밀번호 입력 칸 초기화
                                typePassword.setText("");
                                // 팝업
                                JOptionPane.showMessageDialog(null, "[비밀번호] 영어, 숫자만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                                flag = false;   // 플래그 false
                            }
                        }
                        // 채팅방 최대인원이 입력 되었을 경우, 정규식 검사
                        if (!typeNum.getText().equals("") && !Pattern.matches(num_pattern, typeNum.getText())) {
                            // 정규식과 일치하지 않다면
                            // 최대인원 입력 칸 초기화
                            typeNum.setText("");
                            // 팝업
                            JOptionPane.showMessageDialog(null, "[최대인원] 숫자만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                            flag = false;   // 플래그 false
                        }

                        // 플래그가 true일 경우에만 채팅방 생성 가능
                        if (flag) {
                            // 채팅방 생성 정보 전송을 통해 얻은 문자열 - 채팅방 번호(경로)를 가져옴
                            String result = connector.makeRoomInformation(typeTitle.getText(), pw, typeNum.getText());
                            if (!result.equals("")) {
                                // 대기실 프레임 비활성화
                                mainOperator.mf.setVisible(false);
                                // 채팅방 프레임의 채팅방 경로 설정
                                mainOperator.cf.chatPath = result;
                                // 채팅방 프레임 활성화
                                mainOperator.cf.setVisible(true);
                                // 채팅방 프레임 타이틀 -> [방 반호] 채팅방 제목
                                mainOperator.cf.setTitle("[" + mainOperator.cf.chatPath + "] " + typeTitle.getText());
                                // 입력 칸 초기화
                                typeTitle.setText("");
                                typePassword.setText("");
                                typeNum.setText("");
                                // 채팅방 생성 프레임 닫기
                                dispose();
                                // 채팅방 생성 완료 팝업
                                JOptionPane.showMessageDialog(null, "채팅방 생성 완료", "채팅방 생성", JOptionPane.PLAIN_MESSAGE);
                            } else { // 생성 실패했을 경우
                                JOptionPane.showMessageDialog(null, "채팅방 생성 실패", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else { // 방 제목 정규식 불일치
                        typeTitle.setText("");
                        JOptionPane.showMessageDialog(null, "[방 제목] 영어, 숫자, 한글만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                    }
                    // 방 제목 입력 안했을 경우
                } else { JOptionPane.showMessageDialog(null, "[방 제목] 필수 입력", "채팅방 생성", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            } else if (b.getText().equals("취소")) {
                // 입력 칸 초기화
                typeTitle.setText("");
                typeNum.setText("");
                // 채팅방 생성 프레임 닫기
                dispose();
            }
        }
    }
}