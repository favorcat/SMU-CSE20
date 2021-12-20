import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 쓰레드 인터페이스 사용
public class FindPWFrame extends JFrame {
    JPanel panel = new JPanel(new FlowLayout()); // 레이아웃 선언

    JButton enter = new JButton("찾기");  // 비밀번호 찾기 버튼
    JButton cancel = new JButton("취소"); // 취소 버튼

    JTextField typeId = new JTextField();       // ID 입력 받는 곳
    JTextField typeName = new JTextField();     // 이름 입력 받는 곳
    JTextField typeNum = new JTextField();      // 연락처 입력 받는 곳

    JLabel id= new JLabel("아이디");      // typeId 라벨
    JLabel name = new JLabel("이  름");   // typeName 라벨
    JLabel num = new JLabel("연락처");    // typeNum 라벨

    // 커넥터와 오퍼레이터를 들고 있어야 로그인 기능을 구현할 수 있음
    MyConnector connector;
    Operator mainOperator = null;

    public FindPWFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        MyActionListener ml = new MyActionListener();

        setTitle("멀티 채팅프로그램 - 비밀번호 찾기");

        id.setPreferredSize(new Dimension(70, 30));
        typeId.setPreferredSize(new Dimension(300, 30));

        name.setPreferredSize(new Dimension(70, 30));
        typeName.setPreferredSize(new Dimension(300, 30));

        num.setPreferredSize(new Dimension(70, 30));
        typeNum.setPreferredSize(new Dimension(300, 30));

        enter.setPreferredSize(new Dimension(185, 30));
        cancel.setPreferredSize(new Dimension(185, 30));

        panel.add(id);           // ID 라벨 추가
        panel.add(typeId);       // ID 입력 받는 곳 추가

        panel.add(name);        // 이름 라벨 추가
        panel.add(typeName);    // 이름 입력 받는 곳 추가

        panel.add(num);         // 연락처 라벨 추가
        panel.add(typeNum);     // 연락처 입력 받는 곳 추가

        panel.add(enter);       // 비밀번호 찾기 버튼 추가
        panel.add(cancel);      // 취소 버튼 추가

        setContentPane(panel);
        enter.addActionListener(ml);  // 비밀번호 찾기 버튼에 이벤트 리스너 추가
        cancel.addActionListener(ml); // 취소 버튼에 이벤트 리스너 추가

        setResizable(false);
        setSize(400, 180);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 비밀번호 찾기 버튼을 눌렀을 경우
            if (b.getText().equals("찾기")) {
                // 모든 곳이 입력 되었는지 검사
                if (!typeId.getText().equals("") && !typeName.getText().equals("") && !typeNum.getText().equals("")) {
                    Pattern pattern = Pattern.compile("\\d{11}"); // 11자리 숫자 정규식
                    Matcher matcher = pattern.matcher(typeNum.getText());
                    if (matcher.matches()) { // 연락처 정규식 검사
                        // 비밀번호 찾기 정보 전송을 통해 받아온 비밀번호 문자열
                        String result = connector.findPWInformation(typeId.getText(), typeName.getText(), typeNum.getText());
                        // 받은 문자열이 NF(비밀번호 찾기 실패)가 아닐 경우
                        if (!result.equals("NF")) {
                            // 로그인 프레임 활성화
                            mainOperator.lf.setVisible(true);
                            // 입력 칸 초기화
                            typeName.setText("");
                            typeNum.setText("");
                            // 비밀번호 찾기 프레임 닫기
                            dispose();
                            // 비밀번호 앞 4자리만 보여주고 나머지는 *** 마스킹 처리
                            result = result.substring(0, 4) + "***";
                            // 비밀번호 찾기 결과 팝업창 안내
                            JOptionPane.showMessageDialog(null, result, "비밀번호 찾기 - 결과", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            // 비밀번호 찾기 실패의 경우 경고창 생성
                            JOptionPane.showMessageDialog(null, "비밀번호 찾기 실패", "비밀번호 찾기", JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // 연락처 정규식 불일치
                        typeNum.setText("");
                        JOptionPane.showMessageDialog(null, "[전화번호] 11자리 숫자만 입력", "비밀번호 찾기", JOptionPane.ERROR_MESSAGE);
                    }
                    // 모든 항목을 입력하지 않고 정보를 넘길 경우, 클라이언트 에러가 생김
                } else { JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.", "비밀번호 찾기", JOptionPane.ERROR_MESSAGE); }

                // 취소 버튼을 눌렀을 경우
            } else if (b.getText().equals("취소")) {
                // 로그인 프레임 활성화
                mainOperator.lf.setVisible(true);
                // 입력 칸 초기화
                typeName.setText("");
                typeNum.setText("");
                // 아이디 찾기 프레임
                dispose();
            }
        }
    }
}