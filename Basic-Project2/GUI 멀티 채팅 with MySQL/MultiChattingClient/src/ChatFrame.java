import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ChatFrame extends JFrame {
    Vector<String> vec = new Vector<String>();  // 접속자 목록 받을 벡터
    String myName = "";                    // 유저의 닉네임
    String chatPath = "0";                 // 현재 채팅방 경로

    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());

    // 채팅 메세지 판넬
    JTextArea textArea = new JTextArea("",23,20);
    JScrollPane sp = new JScrollPane(textArea);

    // 접속자 목록 보여줄 판넬
    JList<String> onlineUser = new JList<>();
    JScrollPane onlineSP = new JScrollPane(onlineUser);

    // 메세지 입력, 전송, 퇴장 판넬
    JPanel sendPanel = new JPanel(new FlowLayout());
    JTextField textField = new JTextField(28);
    JButton sendBtn = new JButton("전송");
    JButton outBtn = new JButton("퇴장");

    MyConnector connector;
    Operator mainOperator;

    public ChatFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        ActionListener al = new MyActionListener();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea.setEditable(false);    // 채팅 메세지 로그 변경 불가능
        basePanel.add(sp, BorderLayout.CENTER);

        onlineSP.setPreferredSize(new Dimension(100,380));
        basePanel.add(onlineSP, BorderLayout.EAST);

        sendPanel.add(textField);
        sendPanel.add(sendBtn);
        sendPanel.add(outBtn);
        basePanel.add(sendPanel, BorderLayout.SOUTH);

        // 전송 버튼에 액션리스너 추가
        sendBtn.addActionListener(al);
        outBtn.addActionListener(al);

        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(550,430);
        this.setResizable(false);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   // 프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            // 메세지 뒤에 시간을 보여주기 위해 포맷 설정
            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat simple = new SimpleDateFormat(" (a hh:mm)");

            JButton b =  (JButton)e.getSource();
            // 전송 버튼을 눌렀을 때
            if (b.getText().equals("전송")) {
                if (textField.getText().equals("")) return;
                else { // 커넥터를 통해 서버로 전송
                    connector.sendChat(textField.getText(), chatPath);
                }
                // 메세지 입력 칸 초기화
                textField.setText("");

                // 퇴장 버튼을 눌렀을 때
            } else if (b.getText().equals("퇴장")) {
                // 채팅방 퇴장 정보 전송
                connector.exitRoom(myName);
                // 대기실의 채팅방 선택 필드 초기화
                mainOperator.mf.selectedRoomNum = "";
                mainOperator.mf.selectedRoomTitle = "";
                // 접속자 목록 초기화
                vec = new Vector<String>();
                onlineUser.setListData(vec);
                // 채팅방 경로 초기화
                chatPath = "0";
                // 채팅 메세지 입력 칸 초기화
                textArea.setText("");
                setTitle("채팅방");
                // 대기실 프레임 활성화
                mainOperator.mf.setVisible(true);
                // 채팅 프레임 비활성화
                setVisible(false);
            }
        }
    }
}
