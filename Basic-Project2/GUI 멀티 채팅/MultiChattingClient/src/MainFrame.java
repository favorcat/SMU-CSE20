import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame{
    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());
    // 채팅, 텍스트 입력 판넬
    JPanel centerPanel = new JPanel(new BorderLayout());
    // 접속자 목록 보여줄 판넬
    JPanel eastPanel = new JPanel();
    JTextArea onlineUser = new JTextArea("",24,10);
    JScrollPane onlineSP = new JScrollPane(onlineUser);

    // centerPanel
    JTextArea textArea = new JTextArea("",23,20);
    JScrollPane sp = new JScrollPane(textArea);

    JPanel sendPanel = new JPanel(new BorderLayout());
    JTextField textField = new JTextField(28);
    JButton sendBtn = new JButton("전송");
    // eastPanel 컴포넌트
    //JList onlineMember = new JList();

    MyConnector connector;
    Operator mainOperator;
    MessageListener ml;

    boolean flag = false;

    public MainFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;
        ActionListener al = new MyActionListener();
        ml = new MessageListener(connector.dataInStream, this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("멀티 채팅프로그램");

        basePanel.add(centerPanel, BorderLayout.CENTER);
        basePanel.add(eastPanel, BorderLayout.EAST);

        centerPanel.add(sp, BorderLayout.NORTH);
        textArea.setEditable(false);

        sendPanel.add(textField, BorderLayout.WEST);
        sendPanel.add(sendBtn, BorderLayout.EAST);
        centerPanel.add(sendPanel, BorderLayout.SOUTH);

        eastPanel.add(onlineSP);
        onlineUser.setEditable(false);

        // 전송 버튼에 액션리스너 추가
        sendBtn.addActionListener(al);
        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(550,430);
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
                   connector.sendChat(textField.getText());
                   // 내가 작성한 메세지는 "[나]"로 보여짐
                   textArea.append("[나]: "+ textField.getText()+ simple.format(now) + "\n");
                }
                // 메세지 입력창을 공백으로 초기화
                textField.setText("");
            } else {
                dispose();
            }
        }
    }
}
