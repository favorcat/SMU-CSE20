import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class SignUpFrame extends JFrame implements Runnable{
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("회원가입"); // 회원가입 버튼  선언
    JButton cancel = new JButton("취소"); // 취소 버튼 선언

    JTextField typeId = new JTextField(""); // id 받은곳 선언
    JPasswordField typePassword = new JPasswordField(""); // password 받은곳 선언 받으면 ** < 처럼 나옴
    JPasswordField typePassword2 = new JPasswordField(""); // password2 받은곳 선언 받으면 ** < 처럼 나옴
    JTextField typeName = new JTextField(""); // 이름 받은 곳 선언
    JTextField typeNum = new JTextField(""); // 연락처 받은 곳 선언
    JTextField typeNickname = new JTextField(""); // 닉네임 받은 곳 선언

    JLabel id = new JLabel("아이디"); // 라벨 type id
    JLabel password = new JLabel("비밀번호"); // 라벨 type password
    JLabel password2 = new JLabel("비밀번호 확인"); // 라벨 type password
    JLabel name = new JLabel("이   름"); // 라벨 type name
    JLabel num = new JLabel("연락처"); // 라벨 type num
    JLabel nickname = new JLabel("닉네임"); // 라벨 type nickname

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
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

        panel.add(id);              //  ID 추가
        panel.add(typeId);          // 입력된 ID 추가

        panel.add(password);        // PASSWORD 추가
        panel.add(typePassword);    // 입력된 PASSWORD 추가

        panel.add(password2);        // PASSWORD 추가
        panel.add(typePassword2);    // 입력된 PASSWORD 추가

        panel.add(name);            // 이름(닉네임) 추가
        panel.add(typeName);        // 입력된 이름 추가

        panel.add(num);             // 연락처 추가 
        panel.add(typeNum);         // 입력된 연락처 추가

        panel.add(nickname);
        panel.add(typeNickname);
    
        panel.add(enter);           // 회원가입 버튼 추가
        panel.add(cancel);          // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml); // Login 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 290);

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
            if (b.getText().equals("회원가입")) {
                if (!typeId.getText().equals("") && !typeName.getText().equals("") && !typeNum.getText().equals("") && !typeNickname.getText().equals("")){
                    //Password 컴포넌트에서 문자열 읽어오기
                    String pw = "";
                    for(int i=0; i<typePassword.getPassword().length; i++)
                    {   pw += typePassword.getPassword()[i];   }

                    //Password 확인 컴포넌트에서 문자열 읽어오기
                    String pw2 = "";
                    for(int j=0; j<typePassword2.getPassword().length; j++)
                    {   pw2 += typePassword2.getPassword()[j]; }

                    String id_pattern = "^[A-Za-z0-9]*$"; // 영어, 숫자
                    String pw_pattern = "^[A-Za-z0-9]{6,12}$"; // 영어, 숫자 포함 6~12자리
                    String name_pattern = "^[A-Za-z가-힣]*$"; // 영어, 한글
                    String num_pattern = "\\d{11}"; // 11자리 숫자
                    String nickname_pattern = "^[A-Za-z0-9가-힣]*$"; // 영어, 숫자, 한글

                    if (Pattern.matches(id_pattern, typeId.getText())) { // 아이디 정규식 검사
                        if (Pattern.matches(pw_pattern, pw)) { // 비밀번호 정규식 검사
                            if (pw.equals(pw2)) { // 비밀번호 확인
                                if (Pattern.matches(num_pattern, typeNum.getText())) { // 전화번호 정규식 검사
                                    if(Pattern.matches(name_pattern, typeName.getText())) { // 이름 정규식 검사
                                        if(Pattern.matches(nickname_pattern, typeNickname.getText())) { // 닉네임 정규식 검사
                                            // 회원가입 성공하면 채워넣은 정보들 빈칸으로 만든 후, 로그인창 활성화 및 회원가입창 닫기
                                            if(connector.sendSignupInformation(typeId.getText(), pw.toString(), typeName.getText(), typeNum.getText(), typeNickname.getText())) {
                                                mainOperator.lf.setVisible(true);
                                                typeId.setText("");
                                                typePassword.setText("");
                                                typePassword2.setText("");
                                                typeName.setText("");
                                                typeNum.setText("");
                                                typeNickname.setText("");
                                                dispose();
                                                // 회원가입 성공했다는 경고창 띄우기
                                                JOptionPane.showMessageDialog(null, "회원가입 성공!", "회원가입", JOptionPane.PLAIN_MESSAGE);
                                            }else {
                                                // 회원가입 실패했을 경우 채워넣은 정보들 빈칸으로 만든 후 경고창 띄우기
                                                typeId.setText("");
                                                typePassword.setText("");
                                                typePassword2.setText("");
                                                typeName.setText("");
                                                typeNum.setText("");
                                                typeNickname.setText("");
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
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "회원가입", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼 눌렀을 경우
            }else if (b.getText().equals("취소")) {
                // 채워넣은 정보들 빈칸으로 만듦
                typeId.setText("");
                typePassword.setText("");
                typePassword2.setText("");
                typeName.setText("");
                typeNum.setText("");
                typeNickname.setText("");

                // 로그인창 활성화 및 회원가입창 닫기
                mainOperator.lf.setVisible(true);
                dispose();
            }
        }
    }


}