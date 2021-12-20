import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class FindIDFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("찾기");  // 찾기 버튼 선언
    JButton cancel = new JButton("취소"); // 취소 버튼 선언

    JTextField typeName = new JTextField(); // 이름 입력 받는 곳
    JTextField typeNum = new JTextField();  // 연락처 입력 받는 곳

    JLabel name = new JLabel("이  름"); // typeName 라벨
    JLabel num = new JLabel("연락처"); // typePassword 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 아이디 찾기 기능 구현 가능
    MyConnector connector;
    Operator mainOperator = null;

    public FindIDFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 - 아이디 찾기");

        name.setPreferredSize(new Dimension(70, 30));
        typeName.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(name);        // 이름 라벨 추가
        panel.add(typeName);    // 이름 입력 받는 곳 추가

        panel.add(num);         // 연락처 라벨 추가
        panel.add(typeNum);     // 연락처 입력 받는 곳 추가

        panel.add(enter);       // 찾기 버튼 추가
        panel.add(cancel);      // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml);  // 찾기 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // 취소 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 150);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 아이디 찾기 버튼을 눌렀을 경우
            if (b.getText().equals("찾기")) {
                // 모든 곳이 입력 되었는지 검사
                if (!typeName.getText().equals("") && !typeNum.getText().equals("")) {
                    Pattern pattern = Pattern.compile("\\d{11}"); // 11자리 숫자 정규식
                    Matcher matcher = pattern.matcher(typeNum.getText());
                    if (matcher.matches()) { // 연락처 정규식 검사
                        // 아이디 찾기 정보 전송을 통해 받아온 문자열 저장
                        String result = connector.findIDInformation(typeName.getText(), typeNum.getText());
                        if (!result.equals("NF")) { // 아이디를 찾았을 때
                            // 로그인 프레임 활성화
                            mainOperator.lf.setVisible(true);
                            // 입력 칸 초기화
                            typeName.setText("");
                            typeNum.setText("");
                            // 아이디 찾기 프레임 닫기
                            dispose();
                            // 아이디 찾기 결과 팝업창 안내
                            JOptionPane.showMessageDialog(null, result, "아이디 찾기 - 결과", JOptionPane.PLAIN_MESSAGE);
                        } else { // 아이디 찾기 실패의 경우 팝업창
                            JOptionPane.showMessageDialog(null, "아이디 찾기 실패", "아이디 찾기", JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // 연락처 정규식 불일치
                        typeNum.setText("");
                        JOptionPane.showMessageDialog(null, "[전화번호] 11자리 숫자만 입력", "아이디 찾기", JOptionPane.ERROR_MESSAGE);
                    }
                    // 모든 항목을 입력하지 않고 정보를 넘길 경우, 클라이언트 에러가 생김
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "아이디 찾기", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            } else if (b.getText().equals("취소")) {
                // 로그인 프레임 활성화
                mainOperator.lf.setVisible(true);
                // 입력 칸 초기화
                typeName.setText("");
                typeNum.setText("");
                // 아이디 찾기 프레임 닫기
                dispose();
            }
        }
    }
}