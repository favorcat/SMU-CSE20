import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class SignUpFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("회원가입");    // 회원가입 버튼
    JButton cancel = new JButton("취소");       // 취소 버튼

    JTextField typeId = new JTextField("");                      // ID 입력 받는 곳
    JPasswordField typePassword = new JPasswordField("");  // password 입력 받는 곳 -> ** 처럼 나옴
    JPasswordField typePassword2 = new JPasswordField(""); // password 확인 입력 받는 곳 -> ** 처럼 나옴
    JTextField typeName = new JTextField("");                    // 이름 입력 받는 곳
    JTextField typeNum = new JTextField("");                     // 연락처 입력 받는 곳
    JTextField typeNickname = new JTextField("");                // 닉네임 입력 받는 곳

    JLabel id = new JLabel("아이디");                      // typeId 라벨
    JLabel password = new JLabel("비밀번호");              // typePassword 라벨
    JLabel password2 = new JLabel("비밀번호 확인");        // typePassword2 라벨
    JLabel name = new JLabel("이   름");                   // typeName 라벨
    JLabel num = new JLabel("연락처");                     // typeNum 라벨
    JLabel nickname = new JLabel("닉네임");                // typeNickname 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 회원가입 기능 구현 가능
    MyConnector connector;
    Operator mainOperator;

    public SignUpFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 - 회원가입");

        id.setPreferredSize(new Dimension(70, 30));
        typeId.setPreferredSize(new Dimension(300, 30));

        password.setPreferredSize(new Dimension(70, 30));
        typePassword.setPreferredSize(new Dimension(300, 30));

        password2.setPreferredSize(new Dimension(70, 30));
        typePassword2.setPreferredSize(new Dimension(300, 30));

        name.setPreferredSize(new Dimension(70, 30));
        typeName.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        nickname.setPreferredSize(new Dimension(70, 30));
        typeNickname.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(id);              // ID 라벨 추가
        panel.add(typeId);          // ID 입력 받는 곳 추가

        panel.add(password);        // password 라벨 추가
        panel.add(typePassword);    // password 입력 받는 곳 추가

        panel.add(password2);        // password 확인 라벨 추가
        panel.add(typePassword2);    // password 확인 입력 받는 곳 추가

        panel.add(name);            // 이름 라벨 추가
        panel.add(typeName);        // 이름 입력 받는 곳 추가

        panel.add(num);             // 연락처 라벨 추가
        panel.add(typeNum);         // 연락처 입력 받는 곳 추가

        panel.add(nickname);        // 채팅 닉네임 라벨 추가
        panel.add(typeNickname);    // 채팅 닉네임 입력 받는 곳 추가
    
        panel.add(enter);           // 회원가입 버튼 추가
        panel.add(cancel);          // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml); // 회원가입 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // 취소 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 290);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   // 프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        setVisible(false);
    }

    class MyActionListener implements ActionListener {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 회원가입 버튼을 눌렀을 경우
            if (b.getText().equals("회원가입")) {
                // 모든 곳이 입력 되었는지 검사
                if (!typeId.getText().equals("") && !typeName.getText().equals("") && !typeNum.getText().equals("") && !typeNickname.getText().equals("")) {
                    // Password 컴포넌트에서 문자열 읽어오기
                    String pw = "";
                    for(int i=0; i<typePassword.getPassword().length; i++) {
                        pw += typePassword.getPassword()[i];
                    }

                    // Password 확인 컴포넌트에서 문자열 읽어오기
                    String pw2 = "";
                    for(int j=0; j<typePassword2.getPassword().length; j++) {
                        pw2 += typePassword2.getPassword()[j];
                    }

                    // 정규식 선언
                    String id_pattern = "^[A-Za-z0-9]*$"; // 영어, 숫자
                    String pw_pattern = "^[A-Za-z0-9]{6,12}$"; // 영어, 숫자 포함 6~12자리
                    String name_pattern = "^[A-Za-z가-힣]*$"; // 영어, 한글
                    String num_pattern = "\\d{11}"; // 11자리 숫자
                    String nickname_pattern = "^[A-Za-z0-9가-힣]*$"; // 영어, 숫자, 한글

                    if (Pattern.matches(id_pattern, typeId.getText())) { // 아이디 정규식 검사
                        if (Pattern.matches(pw_pattern, pw)) { // 비밀번호 정규식 검사
                            if (pw.equals(pw2)) { // 비밀번호 일치 확인
                                if (Pattern.matches(num_pattern, typeNum.getText())) { // 전화번호 정규식 검사
                                    if (Pattern.matches(name_pattern, typeName.getText())) { // 이름 정규식 검사
                                        if (Pattern.matches(nickname_pattern, typeNickname.getText())) { // 닉네임 정규식 검사
                                            // 회원가입 성공하면
                                            if (connector.sendSignupInformation(typeId.getText(), pw.toString(), typeName.getText(), typeNum.getText(), typeNickname.getText())) {
                                                // 로그인 프레임 활성화
                                                mainOperator.lf.setVisible(true);
                                                // 입력 칸 초기화
                                                typeId.setText("");
                                                typePassword.setText("");
                                                typePassword2.setText("");
                                                typeName.setText("");
                                                typeNum.setText("");
                                                typeNickname.setText("");
                                                // 회원가입 프레임 닫기
                                                dispose();
                                                // 회원가입 성공 팝업
                                                JOptionPane.showMessageDialog(null, "회원가입 성공!", "회원가입", JOptionPane.PLAIN_MESSAGE);

                                            } else { // 회원가입 실패했을 경우
                                                // 입력 칸 초기화
                                                typeId.setText("");
                                                typePassword.setText("");
                                                typePassword2.setText("");
                                                typeName.setText("");
                                                typeNum.setText("");
                                                typeNickname.setText("");
                                                // 팝업 생성
                                                JOptionPane.showMessageDialog(null, "아이디 혹은 닉네임 중복으로 인한 실패!", "회원가입", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                        else { // 닉네임 정규식 불일치
                                            typeNickname.setText("");
                                            JOptionPane.showMessageDialog(null, "[닉네임] 영어 & 숫자 & 한글만 입력", "회원가입", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } else { // 이름 정규식 불일치
                                        typeName.setText("");
                                        JOptionPane.showMessageDialog(null, "[이름] 한글만 입력", "회원가입", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else { // 전화번호 정규식 불일치
                                    typeNum.setText("");
                                    JOptionPane.showMessageDialog(null, "[전화번호] 11자리 숫자만 입력", "회원가입", JOptionPane.ERROR_MESSAGE);
                                }
                            } else { // 비밀번호 확인 불일치
                                typePassword.setText("");
                                typePassword2.setText("");
                                JOptionPane.showMessageDialog(null, "비밀번호 불일치!", "회원가입", JOptionPane.ERROR_MESSAGE);
                            }
                        } else { // 비밀번호 정규식 불일치
                            typePassword.setText("");
                            typePassword2.setText("");
                            JOptionPane.showMessageDialog(null, "[비밀번호] 문자,숫자 포함의 6~12자리 이내", "회원가입", JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // 아이디 정규식 검사
                        typeId.setText("");
                        JOptionPane.showMessageDialog(null, "[아이디] 영어 & 숫자만 입력", "회원가입", JOptionPane.ERROR_MESSAGE);
                    }
                    // 모든 항목을 입력하지 않고 정보를 넘길 경우, 클라이언트 에러가 생김
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "회원가입", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            } else if (b.getText().equals("취소")) {
                // 입력 칸 초기화
                typeId.setText("");
                typePassword.setText("");
                typePassword2.setText("");
                typeName.setText("");
                typeNum.setText("");
                typeNickname.setText("");
                // 로그인 프레임 활성화 및 회원가입 프레임 닫기
                mainOperator.lf.setVisible(true);
                dispose();
            }
        }
    }
}