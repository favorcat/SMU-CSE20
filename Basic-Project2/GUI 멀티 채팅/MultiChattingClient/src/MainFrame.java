import javax.swing.*;
import javax.swing.border.Border;
import java.net.Socket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    // 베이스 판넬
    JPanel basePanel = new JPanel(new BorderLayout());
    // 채팅, 텍스트 입력 판넬
    JPanel centerPanel = new JPanel(new BorderLayout());
    // 접속자 목록 보여줄 판넬
    JPanel eastPanel = new JPanel();
    // centerPanel
    JTextArea textArea = new JTextArea();
    JScrollPane sp = new JScrollPane(textArea);

    JPanel sendPanel = new JPanel(new BorderLayout());
    JTextField textField = new JTextField();
    JButton sendBtn = new JButton();
    // eastPanel 컴포넌트
    //JList onlineMember = new JList();

    boolean isFirst = true;
    Socket socket;

    public MainFrame() {
        //this.socket = socket;

        basePanel.add(centerPanel, BorderLayout.CENTER);
        basePanel.add(eastPanel, BorderLayout.EAST);

        centerPanel.add(sp, BorderLayout.NORTH);
        textArea.setEditable(false);

        sendPanel.add(textField, BorderLayout.WEST);
        sendPanel.add(sendBtn, BorderLayout.EAST);
        centerPanel.add(sendPanel, BorderLayout.SOUTH);

        basePanel.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sendBtn){
            if(textField.getText().equals("")) return;
            //textArea.append("["+ name + "] : " + textField.getText() + "\n");
            textField.setText("");
        } else {
            this.dispose();
        }
    }
}
