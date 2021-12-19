import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class MakingRoomFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언
    JButton enter = new JButton("생성"); // 채팅방 생성 버튼  선언
    JButton cancel = new JButton("취소"); // 취소 버튼 선언

    JTextField typeTitle = new JTextField(""); // title
    JPasswordField typePassword = new JPasswordField(""); // password 받은곳 선언 받으면 ** < 처럼 나옴
    JTextField typeNum = new JTextField(""); // 최대인원 받은곳 선언

    JLabel title= new JLabel("방 제목"); // 라벨 type 방 제목
    JLabel password = new JLabel("비밀번호"); // 라벨 type 이름
    JLabel num = new JLabel("최대인원"); // 라벨 type 연락처

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
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

        panel.add(title);
        panel.add(typeTitle);

        panel.add(password);
        panel.add(typePassword);

        panel.add(num);
        panel.add(typeNum);

        panel.add(enter);
        panel.add(cancel);

        setContentPane(panel);
        enter.addActionListener(ml); // 생성 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 180);

        //생성 창을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener  {
        //이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 채팅방 생성 버튼 눌렀을 경우
            if (b.getText().equals("생성")) {
                boolean flag = true;
                if (!typeTitle.getText().equals("")) {
                    String pattern = "^[A-Za-z0-9가-힣]*$";
                    String pw_pattern = "^[A-Za-z0-9]{6,12}$";
                    String num_pattern = "\\d";

                    String pw = "";
                    for(int i=0; i<typePassword.getPassword().length; i++)
                    {   pw += typePassword.getPassword()[i];   }

                    if (Pattern.matches(pattern, typeTitle.getText())) { // 방 제목 정규식 검사
                        if(!pw.equals("")){
                            if(!Pattern.matches(pw_pattern, pw)){
                                typePassword.setText("");
                                JOptionPane.showMessageDialog(null, "[비밀번호] 영어, 숫자만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                                flag = false;
                            }
                        }
                        if(!typeNum.getText().equals("") && !Pattern.matches(num_pattern, typeNum.getText())){
                            typeNum.setText("");
                            JOptionPane.showMessageDialog(null, "[최대인원] 숫자만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                            flag = false;
                        }
                        if (flag){
                            String result = connector.makeRoomInformation(typeTitle.getText(), pw, typeNum.getText());
                            if (!result.equals("")) {
                                System.out.println("[채팅방 생성 프레임] 경로 >> " + result);
                                mainOperator.mf.flag = false;
                                if (!mainOperator.cf.startFlag) mainOperator.cf.cml.start();
                                mainOperator.cf.flag = true;
                                System.out.println("플래그 전환");

                                mainOperator.mf.setVisible(false);
                                mainOperator.cf.chatPath = result;
                                mainOperator.cf.setVisible(true);
                                mainOperator.cf.setTitle(result + " - " + typeTitle.getText());

                                typeTitle.setText("");
                                typeNum.setText("");
                                dispose();
                                JOptionPane.showMessageDialog(null, "채팅방 생성 완료", "채팅방 생성", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "채팅방 생성 실패", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        typeTitle.setText("");
                        JOptionPane.showMessageDialog(null, "[방 제목] 영어, 숫자, 한글만 가능", "채팅방 생성", JOptionPane.ERROR_MESSAGE);
                    }
                } else { JOptionPane.showMessageDialog(null, "[방 제목] 필수 입력", "채팅방 생성", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            }else if (b.getText().equals("취소")) {
                dispose();
                typeTitle.setText("");
                typeNum.setText("");
            }
        }
    }
}