import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;


public class MainFrame extends JFrame{
    String[] departments = {"컴퓨터공학부", "전자공학과","기계공학과", "건축공학과"};
    Object[][] students = {
            {"컴퓨터공학부", "1학년", "홍길동", "학부생", "111111"},
            {"컴퓨터공학부", "4학년", "김철수", "학부생", "123467"},
            {"컴퓨터공학부", "4학년", "이병헌", "학부생", "100011"},
            {"컴퓨터공학부", "2학년", "푸하하", "학부생", "145361"},
            {"컴퓨터공학부", "1학년", "하하하", "학부생", "111551"},
            {"컴퓨터공학부", "1학년", "강호동", "학부생", "123111"},
            {"컴퓨터공학부", "3학년", "이수근", "학부생", "165101"},
            {"컴퓨터공학부", "1학년", "서장훈", "학부생", "133411"},
            {"전자공학과", "1학년", "홍길동", "학부생", "111111"},
            {"전자공학과", "1학년", "김철수", "학부생", "123467"},
            {"전자공학과", "4학년", "이병헌", "학부생", "100011"},
            {"전자공학과", "4학년", "푸하하", "학부생", "145361"},
            {"전자공학과", "1학년", "하하하", "학부생", "111551"},
            {"전자공학과", "2학년", "강호동", "학부생", "123111"},
            {"전자공학과", "1학년", "이수근", "대학원생", "165101"},
            {"전자공학과", "3학년", "서장훈", "대학원생", "133411"}
    };

    // 공간 제어용
    JPanel basePanel = new JPanel(new BorderLayout());
    JPanel westPanel = new JPanel();
    JPanel centerPanel = new JPanel();


    //메뉴 제작용
    JMenuBar mb = new JMenuBar();
    JMenu homeMenu = new JMenu("HOME");
    // JMenu manageMenu = new JMenu("Manage"); // 객체 생성
    JMenuItem openMI = new JMenuItem("OPEN");
    JMenuItem newMI = new JMenuItem("NEW");
    JMenuItem exitMI = new JMenuItem("EXIT");
    // westPannel 컴포넌트 선언
    JLabel titleLabel = new JLabel("Select Stuedent Type");
    JCheckBox usCheck = new JCheckBox("학부생");
    JCheckBox gsCheck = new JCheckBox("대학원생");
    JComboBox comboBox;
    JTree tree;
    //JButton exitBtn = new JButton("EXIT");
    DefaultMutableTreeNode root;
    DefaultTreeModel treeModel;

    // 센터 패널용 컴포턴트 선언
    JTable table;
    DefaultTableModel tableModel;
    String columNames[] = { "학과", "학년", "성명", "구분", "학번" };

    MainFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("학생정보관리시스템 V 0.1");

//homeMenu 추가
        homeMenu.add(newMI);
        homeMenu.addSeparator(); // 구분선
        homeMenu.add(openMI);
        homeMenu.addSeparator();
        homeMenu.add(exitMI);

        mb.add(homeMenu);
        // mb.add(new JMenu("Manage")); // 익명 객체
        setJMenuBar(mb);

// 패널 추가 작업
        westPanel.setPreferredSize(new Dimension(160, basePanel.getHeight()));
        setContentPane(basePanel);
        basePanel.add(westPanel, BorderLayout.WEST);
        basePanel.add(centerPanel, BorderLayout.CENTER);
        westPanel.setLayout(new FlowLayout());



// westPannel 컴포넌트 작업
        comboBox = new JComboBox(departments); // 문자열 배열의 각 요소가 하나의 아이템으로 들어감 -> 드롭다운박스처럼
        root = new DefaultMutableTreeNode("학과"); // 트리 객체 생성을 위해 필요한 것
        tree = new JTree(root); // 트리 객체
        for(int i=0; i<departments.length; i++ ) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(departments[i]); // 반복문으로 노드 생성
            root.add(node); // 루트에 추가
            treeModel = (DefaultTreeModel) tree.getModel(); // 필드로 선언했던 자신이 가지고 있는 treeModel을 넘겨줌
            treeModel.setRoot(root); // 루트를 새롭게 갱신한다.
        }

        // 크기 조절
        comboBox.setPreferredSize(new Dimension(160, 20));
        titleLabel.setPreferredSize(new Dimension(160, 20)); // select student type 아래 부분
        tree.setPreferredSize(new Dimension(160, 140));

// westPanel.add
        westPanel.add(titleLabel);
        westPanel.add(usCheck);
        westPanel.add(gsCheck);
        westPanel.add(comboBox);
        westPanel.add(tree);
//westPanel.add(exitBtn);

// 센터 패널 컴포넌트 작업
        tableModel = new DefaultTableModel(students, columNames); // column -> 타이틀
        table = new JTable(tableModel);  // 테이블이 생성되때 테이블모델이 있어야 함. 모델 -> 내용을 가지고 있는 것
        JScrollPane sp = new JScrollPane(table); // 스크롤이 달려있는 Pane 생성 for table, scrollPane 위에 table이 존재

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(sp, BorderLayout.CENTER);

        setSize(900, 300); // 메인 프레임의 크기를 설정
    }

    // 여기서 각 요소들에 이벤트 트리거를 생성해 주어야 함. 과제에서는 DB를 연동시켜야 함.
}