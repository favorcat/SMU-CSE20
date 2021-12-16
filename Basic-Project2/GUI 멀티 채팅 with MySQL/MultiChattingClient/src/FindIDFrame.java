import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class FindIDFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언
    JButton enter = new JButton("찾기"); // 로그인 버튼  선언
    JButton cancel = new JButton("취소"); // 취소 버튼 선언

    JTextField typeName = new JTextField(); // name 받은곳  선언
    JTextField typeNum = new JTextField(); // num 받은곳 선언

    JLabel name = new JLabel("이  름"); // 라벨 type 이름
    JLabel num = new JLabel("연락처"); // 라벨 type 연락처

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
    MyConnector connector;
    Operator mainOperator = null;

    public FindIDFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("채팅 - 아이디 찾기");

        name.setPreferredSize(new Dimension(70, 30));
        typeName.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(name);
        panel.add(typeName);

        panel.add(num);
        panel.add(typeNum);

        panel.add(enter);
        panel.add(cancel);

        setContentPane(panel);
        enter.addActionListener(ml); // 찾기 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // Cancel 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 150);

        //로그인 창을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener  {
        //이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 아이디 찾기 버튼 눌렀을 경우
            if (b.getText().equals("찾기")) {
                if (!typeName.getText().equals("") && !typeNum.getText().equals("")) {
                    Pattern pattern = Pattern.compile("\\d{11}");
                    Matcher matcher = pattern.matcher(typeNum.getText());
                    if (matcher.matches()) {
                        String result = connector.findIDInformation(typeName.getText(), typeNum.getText());
                        if (!result.equals("NF")) {
                            mainOperator.lf.setVisible(true); // 로그인창 활성화
                            typeName.setText("");
                            typeNum.setText("");
                            dispose();
                            JOptionPane.showMessageDialog(null, result, "아이디 찾기 - 결과", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            // 로그인 실패의 경우 경고창 생성
                            JOptionPane.showMessageDialog(null, "아이디 찾기 실패!", "아이디 찾기", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        typeNum.setText("");
                        JOptionPane.showMessageDialog(null, "[전화번호] 11자리 숫자만 입력", "아이디 찾기", JOptionPane.ERROR_MESSAGE);
                    }
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "아이디 찾기", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            }else if (b.getText().equals("취소")) {
                mainOperator.lf.setVisible(true);
                typeName.setText("");
                typeNum.setText("");
                dispose();
            }
        }
    }
}