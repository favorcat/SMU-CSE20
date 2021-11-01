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
        homeMenu.addSeparator();
        homeMenu.add(openMI);
        homeMenu.addSeparator();
        homeMenu.add(exitMI);
        mb.add(homeMenu);
        setJMenuBar(mb);

// 패널 추가 작업
        westPanel.setPreferredSize(new Dimension(160, basePanel.getHeight()));
        setContentPane(basePanel);
        basePanel.add(westPanel, BorderLayout.WEST);
        basePanel.add(centerPanel, BorderLayout.CENTER);
        westPanel.setLayout(new FlowLayout());



// westPannel 컴포넌트 작업
        comboBox = new JComboBox(departments);
        root = new DefaultMutableTreeNode("학과");
        tree = new JTree(root);
        for(int i=0; i<departments.length; i++ ) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(departments[i]);
            root.add(node);
            treeModel = (DefaultTreeModel) tree.getModel();
            treeModel.setRoot(root);
        }


        comboBox.setPreferredSize(new Dimension(160, 20));
        titleLabel.setPreferredSize(new Dimension(160, 20));
        tree.setPreferredSize(new Dimension(160, 140));

// westPanel.add
        westPanel.add(titleLabel);
        westPanel.add(usCheck);
        westPanel.add(gsCheck);
        westPanel.add(comboBox);
        westPanel.add(tree);
//westPanel.add(exitBtn);

// 센터 패널 컴포넌트 작업
        tableModel = new DefaultTableModel(students, columNames);
        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(sp, BorderLayout.CENTER);

        setSize(900, 300);
    }


}