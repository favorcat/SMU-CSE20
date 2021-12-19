import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ChatFrame extends JFrame{
    Vector<String> vec = new Vector<String>();
    String myName = "";
    String chatPath = "";

    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());

    // 채팅, 텍스트 입력 판넬
    JPanel centerPanel = new JPanel(new BorderLayout());
    JTextArea textArea = new JTextArea("",23,20);
    JScrollPane sp = new JScrollPane(textArea);

    // 접속자 목록 보여줄 판넬
    JPanel eastPanel = new JPanel();
    JList<String> onlineUser = new JList<>();
    JScrollPane onlineSP = new JScrollPane(onlineUser);

    JPanel sendPanel = new JPanel(new FlowLayout());
    JTextField textField = new JTextField(28);
    JButton sendBtn = new JButton("전송");
    JButton outBtn = new JButton("퇴장");

    MyConnector connector;
    Operator mainOperator;
    ChatMessageListener cml;

    boolean flag = false;
    boolean startFlag = false;

    public ChatFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        ActionListener al = new MyActionListener();
        cml = new ChatMessageListener(connector.dataInStream,this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        centerPanel.add(sp, BorderLayout.NORTH);
        textArea.setEditable(false);
        basePanel.add(centerPanel, BorderLayout.CENTER);

        onlineSP.setPreferredSize(new Dimension(100,380));
        eastPanel.add(onlineSP);
        basePanel.add(eastPanel, BorderLayout.EAST);

        sendPanel.add(textField);
        sendPanel.add(sendBtn);
        sendPanel.add(outBtn);
        basePanel.add(sendPanel, BorderLayout.SOUTH);

        // 전송 버튼에 액션리스너 추가
        sendBtn.addActionListener(al);
        outBtn.addActionListener(al);

        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(550,430);

        //생성 창을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   //프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 메세지 뒤에 시간을 보여주기 위해 포맷 설정
            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat simple = new SimpleDateFormat(" (a hh:mm)");

            // 전송 버튼을 눌렀을 때, 채팅메세지를 서버로 전송
            JButton b =  (JButton)e.getSource();
            if (b.getText().equals("전송")) {
                if (textField.getText().equals("")) return;
                else {
                    // 커넥터를 통해 서버로 전송
                    connector.sendChat(textField.getText(), chatPath);
                }
                // 메세지 입력창을 공백으로 초기화
                textField.setText("");
            } else if (b.getText().equals("퇴장")){
                flag = false;
                mainOperator.mf.flag = true;
                mainOperator.mf.selectedRoomNum = "";
                mainOperator.mf.selectedRoomTitle = "";

                vec = new Vector<String>();
                onlineUser.setListData(vec);
                chatPath = "";
                setTitle("채팅방");

                connector.exitRoom(myName);
                mainOperator.mf.setVisible(true);
                dispose();
            }
        }
    }
}
