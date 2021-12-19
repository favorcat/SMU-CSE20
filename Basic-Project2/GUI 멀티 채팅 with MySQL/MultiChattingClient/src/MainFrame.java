import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MainFrame extends JFrame{
    Vector<String> vec = new Vector<String>();
    String myName = "";

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


    JPanel southPanel = new JPanel(new FlowLayout());
    JTextArea nameArea = new JTextArea("",1,20);
    JButton makeBtn = new JButton("채팅방 생성");
    JButton outBtn = new JButton("로그아웃");

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
        setTitle("멀티 채팅프로그램 - 대기실");

        centerPanel.add(sp, BorderLayout.NORTH);
        textArea.setEditable(false);
        basePanel.add(centerPanel, BorderLayout.CENTER);

        onlineSP.setPreferredSize(new Dimension(100,367));
        eastPanel.add(onlineSP);
        basePanel.add(eastPanel, BorderLayout.EAST);

        southPanel.add(nameArea);
        nameArea.setEditable(false);
        southPanel.add(makeBtn);
        southPanel.add(outBtn);
        basePanel.add(southPanel, BorderLayout.SOUTH);

        // 전송 버튼에 액션리스너 추가
        makeBtn.addActionListener(al);
        outBtn.addActionListener(al);

        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(550,440);

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
            if (b.getText().equals("채팅방 생성")) {
                mainOperator.mrf.setVisible(true);
            } else if (b.getText().equals("로그아웃")){
                dispose();
                connector.logOut(myName);
            }
        }
    }
}
