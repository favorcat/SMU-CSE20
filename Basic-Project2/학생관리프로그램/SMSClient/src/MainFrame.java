import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.*;
import javax.swing.tree.*;


public class MainFrame extends JFrame{
    // 학과 정보를 담고 있는 배열
    ArrayList<String> departments = new ArrayList<>();
    int selected = 0;           // 학부생, 대학원생 체크 구별 -> 0: 둘 다 선택함 / 1: 학부생 / 2: 대학원생 / 3: 둘 다 선택안함
    int selectedYear = 0;       // 선택한 학년 -> 0: 모든 학년
    String selectedDep = "ALL"; // 선택한 학과 -> ALL: 모든 학과

    // 공간 제어용
    JPanel basePanel = new JPanel(new BorderLayout());
    JPanel westPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    // 메뉴
    JMenuBar mb = new JMenuBar();
    JMenu homeMenu = new JMenu("HOME");
    JMenuItem addMI = new JMenuItem("학생등록");
    JMenuItem delMI = new JMenuItem("학생삭제");
    JMenuItem exitMI = new JMenuItem("EXIT");

    // westPannel 컴포넌트 선언
    JLabel titleLabel = new JLabel("  Select Stuedent Type");
    JCheckBox usCheck = new JCheckBox("학부생", true);
    JCheckBox gsCheck = new JCheckBox("대학원생", true);
    JComboBox comboBox;
    JTree tree;
    DefaultMutableTreeNode root;
    DefaultTreeModel treeModel;

    // 센터 패널용 컴포턴트 선언
    JTable table;
    DefaultTableModel tableModel;
    String[] columNames = { "학과", "학년", "성명", "구분", "학번" };

    MyConnector connector;
    Operator mainOperator;
    MessageListener ml;

    MainFrame(Operator _o){
        mainOperator = _o;
        connector = _o.connector;
        ml = new MessageListener(connector.dataInStream, this);

        // 각 리스너
        ActionListener al = new MyActionListener();
        ItemListener il = new MyItemListener();
        TreeSelectionListener tl = new MyTreeListener();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("학생정보관리시스템_고은영");

        //homeMenu 추가
        homeMenu.add(addMI);
        homeMenu.addSeparator(); // 구분선
        homeMenu.add(delMI);
        homeMenu.addSeparator();  // 구분선
        homeMenu.add(exitMI);
        exitMI.addActionListener(e -> {
            JMenuItem m = (JMenuItem)e.getSource();
            // EXIT 눌렀을 때, 프로그램 종료
            if(m.getText().equals("EXIT")){
                System.exit(0);
            }
        });

        mb.add(homeMenu);
        setJMenuBar(mb);

        // 패널 추가 작업
        westPanel.setPreferredSize(new Dimension(160, basePanel.getHeight()));
        setContentPane(basePanel);
        basePanel.add(westPanel, BorderLayout.WEST);
        basePanel.add(centerPanel, BorderLayout.CENTER);
        westPanel.setLayout(new FlowLayout());

        // westPannel 컴포넌트 작업
        comboBox = new JComboBox(); // 문자열 배열의 각 요소가 하나의 아이템으로 들어감 -> 드롭다운박스처럼
        comboBox.addActionListener(al);
        root = new DefaultMutableTreeNode("학과"); // 트리 객체의 부모 노드
        tree = new JTree(root); // 트리 객체
        tree.addTreeSelectionListener(tl); // 트리에 선택 리스너 추가
        JScrollPane treeSP = new JScrollPane(tree); // scrollPane에 트리 추가

        // 크기 조절
        comboBox.setPreferredSize(new Dimension(160, 20));
        titleLabel.setPreferredSize(new Dimension(160, 20)); // select student type 아래 부분
        tree.setPreferredSize(new Dimension(160, 140));

        // westPanel.add
        westPanel.add(titleLabel);
        westPanel.add(usCheck);
        usCheck.addItemListener(il); // 체크박스에 리스너 추가
        westPanel.add(gsCheck);
        gsCheck.addItemListener(il); // 체크박스에 리스너 추가
        westPanel.add(comboBox);
        westPanel.add(treeSP);

        // 센터 패널 컴포넌트 작업
        tableModel = new DefaultTableModel(columNames, 0); // column -> 타이틀
        table = new JTable(tableModel);  // 테이블이 생성되때 테이블모델이 있어야 함. 모델 -> 내용을 가지고 있는 것
        JScrollPane sp = new JScrollPane(table); // 스크롤이 달려있는 Pane 생성 for table, scrollPane 위에 table이 존재

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(sp, BorderLayout.CENTER);

        setSize(600, 400); // 메인 프레임의 크기를 설정
    }

    // 학과 선택 시, 부모 노드는 학과명, 자식 노드는 1 ~ 4학년으로 설정하는 메소드
    public void setRootNode() {
        // 부모 노드를 학과명으로 설정
        root.setUserObject(selectedDep);
        // 기존에 있던 자식 노드 삭제
        root.removeAllChildren();
        String[] year = {"1학년", "2학년", "3학년", "4학년"};
        for(int i=0; i<4; i++){ // 학년별로 자식 노드 추가
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(year[i]);
            root.add(child);
        }
    }

    // 콤보 박스 리스너
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            // 콤보 박스의 값을 선택된 학과로 설정
            selectedDep = (String) cb.getSelectedItem();
            // 선택된 학년 초기화
            selectedYear = 0;
            // 만약 모든 학과를 선택했다면
            if (selectedDep.equals("ALL")){
                // 부모 노드를 "학과"로 설정
                root.setUserObject("학과");
                // 기존에 있던 자식 노드 삭제
                root.removeAllChildren();
                // 학과 정보를 담고 있는 배열을 통해 자식 노드 재설정
                for(int i=0; i<departments.size(); i++){
                    String data = departments.get(i);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
                    root.add(node);
                }
            } else { // 특정 학과를 선택했다면
                setRootNode();
            }
            // 트리 모델 재설정
            treeModel = (DefaultTreeModel) tree.getModel();
            treeModel.setRoot(root);
            // 서버에 학생 정보 요청하여 업데이트
            connector.search(selectedDep, selectedYear, selected);
        }
    }

    // 체크 박스 리스너
    class MyItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            // 학부생 체크박스가 작동했을 때
            if(e.getSource() == usCheck){
                // 선택했을 때
                if(e.getStateChange() == ItemEvent.SELECTED){
                    // 학부생 O, 대학원생 O
                    if(gsCheck.isSelected()) selected = 0;
                    // 학부생 O, 대학원생 X
                    else selected = 1;
                } else { // 선택 해제했을 때
                    // 학부생 X, 대학원생 O
                    if(gsCheck.isSelected()) selected = 2;
                    // 학부생 X, 대학원생 X
                    else selected = 3;
                }
            }
            // 대학원생 체크박스가 작동했을 때
            if(e.getSource() == gsCheck){
                // 선택했을 때
                if(e.getStateChange() == ItemEvent.SELECTED){
                    // 학부생 O, 대학원생 O
                    if(usCheck.isSelected()) selected = 0;
                    // 학부생 X, 대학원생 O
                    else selected = 2;
                } else { // 선택 해제했을 때
                    // 학부생 O, 대학원생 X
                    if(usCheck.isSelected()) selected = 1;
                    // 학부생 X, 대학원생 X
                    else selected = 3;
                }
            }
            // 서버에 학생 정보 요청하여 업데이트
            connector.search(selectedDep, selectedYear, selected);
        }
    }
    // 트리 선택 리스너
    class MyTreeListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); // 선택된 노드
            // null이라면 return
            if(node == null) return;

            // 선택된 노드값을 문자열로 캐스팅
            String selec = (String) node.getUserObject();
            if(selec.endsWith("학과") || selec.endsWith("학부")){ // 특정 학과 혹은 학부 노드를 선택했다면
                // 해당 학과로 선택된 학과 변경
                selectedDep = selec;
                // 선택된 학년 초기화
                selectedYear = 0;
                // 콤보박스의 선택된 학과 해당 학과로 변경
                comboBox.setSelectedItem(selectedDep);
                // 노드 재설정
                setRootNode();
            } else { // 특정 학년 노드를 선택했다면
                // 선택된 학년을 선택한 노드로 설정
                selectedYear = Integer.parseInt(selec.substring(0,1));
            }
            // 트리 모델 재설정
            treeModel = (DefaultTreeModel) tree.getModel();
            treeModel.setRoot(root);
            // 서버에 학생 정보 요청하여 업데이트
            connector.search(selectedDep, selectedYear, selected);
        }
    }
}