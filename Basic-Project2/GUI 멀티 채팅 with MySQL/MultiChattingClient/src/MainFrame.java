import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class MainFrame extends JFrame {
    Vector<String> vec = new Vector<String>();   // 접속자 목록 받을 벡터
    String myName = "";                     // 유저의 닉네임
    String selectedRoomNum = "";            // 선택한 채팅방 번호(경로)
    String selectedRoomTitle = "";          // 선택한 채팅방 제목

    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());

    // 채팅방 목록
    JTable table;
    DefaultTableModel tableModel;
    String[] columNames = { "채팅방 번호", "채팅방 제목", "방장", "접속인원"};

    // 채팅, 텍스트 입력 판넬
    JPanel centerPanel = new JPanel(new BorderLayout());
    JTextArea textArea = new JTextArea("",23,20);
    JScrollPane sp = new JScrollPane(textArea);

    // 접속자 목록 보여줄 판넬
    JList<String> onlineUser = new JList<>();
    JScrollPane onlineSP = new JScrollPane(onlineUser);

    // 하단 버튼 및 유저 닉네임 보여줄 판넬
    JPanel southPanel = new JPanel(new FlowLayout());
    JTextArea nameArea = new JTextArea("",1,20); // 로그인한 유저 닉네임
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

        MouseListener msl = new MyMouseListener();
        ActionListener al = new MyActionListener();
        ml = new MessageListener(connector.dataInStream, this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("멀티 채팅프로그램 - 대기실");

        // 채팅방 목록 더블 클릭 시, 변경 불가능하게 설정
        tableModel = new DefaultTableModel(columNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        }; // column -> 타이틀
        table = new JTable(tableModel);  // 테이블이 생성되때 테이블 모델이 있어야 함. 모델 -> 내용을 가지고 있는 것
        JScrollPane sp = new JScrollPane(table); // 스크롤이 달려있는 Pane 생성 for table, scrollPane 위에 table이 존재
        basePanel.add(sp, BorderLayout.CENTER);

        onlineSP.setPreferredSize(new Dimension(100,367));
        basePanel.add(onlineSP, BorderLayout.EAST);

        southPanel.add(nameArea);
        nameArea.setEditable(false); // 유저 닉네임 변경 불가능
        southPanel.add(makeBtn);
        southPanel.add(enterBtn);
        southPanel.add(outBtn);
        basePanel.add(southPanel, BorderLayout.SOUTH);

        // 버튼에 액션리스너 추가
        makeBtn.addActionListener(al);
        enterBtn.addActionListener(al);
        outBtn.addActionListener(al);
        table.addMouseListener(msl);

        this.add(basePanel, BorderLayout.CENTER);
        this.setSize(600,440);
        this.setResizable(false);

        // 프레임을 화면 중앙에 배치
        Dimension frameSize = this.getSize();   // 프레임 사이즈를 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    class MyActionListener implements ActionListener {
        // 이벤트를 발생시킨 컴포넌트(소스)
        public void actionPerformed(ActionEvent e) {
            JButton b =  (JButton)e.getSource();

            // 채팅방 생성 버튼을 눌렀을 경우
            if (b.getText().equals("채팅방 생성")) {
                mainOperator.mrf.setVisible(true);
                // 채팅방 입장 버튼을 눌렀을 경우
            } else if (b.getText().equals("채팅방 입장")) {
                // 채팅방을 먼저 선택했는지 확인
                if(!selectedRoomNum.equals("") && !selectedRoomTitle.equals("")){
                    // 채팅방 입장을 위해 채팅방 정보 요청을 통해 얻어온 문자열
                    String result = connector.searchRoom(selectedRoomNum);
                    System.out.println(">>> " + result);
                    switch (result) {
                        // 최대 인원일 경우
                        case "ROOM_MAX" -> JOptionPane.showMessageDialog(null, "최대 인원으로 입장 불가", selectedRoomTitle, JOptionPane.ERROR_MESSAGE);

                        // 채팅방 비밀번호가 없을 경우
                        case "ROOM_PW_NULL" -> {
                            // 채팅방 입장 요청, 비밀번호는 NULL로 요청
                            String enterResult = connector.enterRoom(selectedRoomNum, "NULL");
                            // 입장 실패가 아닐 경우,
                            if (!enterResult.equals("ROOM_FAIL")) {
                                // 대기실 프레임 비활성화
                                mainOperator.mf.setVisible(false);
                                // 채팅방 경로 선택된 채팅방 번호로 설정
                                mainOperator.cf.chatPath = selectedRoomNum;
                                // 채팅방 프레임 활성화
                                mainOperator.cf.setVisible(true);
                                // 채팅방 프레임 타이틀 -> [방 반호] 채팅방 제목
                                mainOperator.cf.setTitle("[" + selectedRoomNum + "] " + selectedRoomTitle);
                            }
                        }

                        // 채팅방 비밀번호가 있을 경우
                        case "ROOM_INSERT_PW" -> {
                            // 채팅방 비밀번호 입력 프레임의 경로와 타이틀 설정
                            mainOperator.rpwf.path = selectedRoomNum;
                            mainOperator.rpwf.title = selectedRoomTitle;
                            // 채팅방 비밀번호 입력 프레임 활성화
                            mainOperator.rpwf.setVisible(true);
                            // 채팅방 비밀번호 입력 프레임 타이틀 -> 채팅방 제목 - 비밀번호 입력
                            mainOperator.rpwf.setTitle(mainOperator.rpwf.title + " - 비밀번호 입력");
                        }
                    }
                } else { // 채팅방 선택을 안하고 눌렀을 경우
                    JOptionPane.showMessageDialog(null, "먼저 채팅방을 선택해 주세요.", "대기실", JOptionPane.ERROR_MESSAGE);
                    }

                // 로그아웃 버튼을 눌렀을 경우
            } else if (b.getText().equals("로그아웃")){
                // 로그아웃을 위한 정보 전송
                connector.logOut(myName);
                // 대기실 닫을
                dispose();
            }
        }
    }

    // 채팅방 목록을 클릭했는지 보는 마우스 리스너
    class MyMouseListener implements MouseListener {
        @Override
        // 클릭 했을 때
        public void mouseClicked(MouseEvent e) {
            // 선택한 행 저장
            int row = table.getSelectedRow();
            // 해당 행의 채팅방 번호, 채팅방 제목 설정
            selectedRoomNum = (String) table.getModel().getValueAt(row, 0);
            selectedRoomTitle = (String) table.getModel().getValueAt(row, 1);
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
