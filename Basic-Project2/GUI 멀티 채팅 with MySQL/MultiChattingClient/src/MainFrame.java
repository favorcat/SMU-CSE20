import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MainFrame extends JFrame{
    Vector<String> vec = new Vector<String>();
    String myName = "";
    String selectedRoomNum = "";
    String selectedRoomTitle = "";

    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());

    // 채팅, 텍스트 입력 판넬
    JTable table;
    DefaultTableModel tableModel;
    String[] columNames = { "번호", "방 제목", "방장", "인원"};

    // 접속자 목록 보여줄 판넬
    JPanel eastPanel = new JPanel();
    JList<String> onlineUser = new JList<>();
    JScrollPane onlineSP = new JScrollPane(onlineUser);

    JPanel southPanel = new JPanel(new FlowLayout());
    JTextArea nameArea = new JTextArea("",1,20);
    JButton makeBtn = new JButton("채팅방 생성");
    JButton enterBtn = new JButton("채팅방 입장");
    JButton outBtn = new JButton("로그아웃");

    MyConnector connector;
    Operator mainOperator;
    MessageListener ml;

    boolean flag = false;

    public MainFrame(Operator _o) {
        mainOperator = _o;
        connector = _o.connector;

        ActionListener al = new MyActionListener();
        MouseListener tl = new MyMouseListener();
        ml = new MessageListener(connector.dataInStream, this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("멀티 채팅프로그램 - 대기실");

        tableModel = new DefaultTableModel(columNames, 0); // column -> 타이틀
        table = new JTable(tableModel);  // 테이블이 생성되때 테이블모델이 있어야 함. 모델 -> 내용을 가지고 있는 것
        JScrollPane sp = new JScrollPane(table); // 스크롤이 달려있는 Pane 생성 for table, scrollPane 위에 table이 존재
        basePanel.add(sp, BorderLayout.CENTER);

        onlineSP.setPreferredSize(new Dimension(100,367));
        eastPanel.add(onlineSP);
        basePanel.add(eastPanel, BorderLayout.EAST);

        southPanel.add(nameArea);
        nameArea.setEditable(false);
        southPanel.add(makeBtn);
        southPanel.add(enterBtn);
        southPanel.add(outBtn);
        basePanel.add(southPanel, BorderLayout.SOUTH);

        // 전송 버튼에 액션리스너 추가
        makeBtn.addActionListener(al);
        enterBtn.addActionListener(al);
        outBtn.addActionListener(al);
        table.addMouseListener(tl);

        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(600,440);

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
            } else if (b.getText().equals("채팅방 입장")) {
                if(!selectedRoomNum.equals("") && !selectedRoomTitle.equals("")){
                    String result = connector.searchRoom(selectedRoomNum);
                    String enterResult = "";
                    if(result.equals("ROOM_MAX")) {
                        JOptionPane.showMessageDialog(null, "최대 인원으로 입장 불가", selectedRoomTitle, JOptionPane.ERROR_MESSAGE);
                    } else if(result.equals("ROOM_PW_NULL")){
                        System.out.println("비번 없음");
                        enterResult = connector.enterRoom(selectedRoomNum, "NULL");
                    } else if(result.equals("ROOM_INSERT_PW")){
                        System.out.println("비번입력필요");
                    }
                    if (!enterResult.equals("ROOM_FAIL")){
                        mainOperator.mf.flag = false;
                        if (!mainOperator.cf.startFlag) mainOperator.cf.cml.start();
                        mainOperator.cf.flag = true;
                        System.out.println("플래그 전환");

                        mainOperator.mf.setVisible(false);
                        mainOperator.cf.chatPath = selectedRoomNum;
                        mainOperator.cf.setVisible(true);
                        mainOperator.cf.setTitle(selectedRoomNum + " - " + selectedRoomTitle);
                        dispose();
                    }
                }
            } else if (b.getText().equals("로그아웃")){
                dispose();
                connector.logOut(myName);
            }
        }
    }

    class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            selectedRoomNum = (String) table.getModel().getValueAt(row, 0);
            selectedRoomTitle = (String) table.getModel().getValueAt(row, 1);
            System.out.println("selectedRoomNUM >> " + selectedRoomNum);
            System.out.println("selectedTitle >> " + selectedRoomTitle);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
