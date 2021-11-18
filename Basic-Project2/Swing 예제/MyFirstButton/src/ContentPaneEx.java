import javax.swing.*;
import java.awt.*;

public class ContentPaneEx extends JFrame {
    public ContentPaneEx(){
        setTitle("ContentPane과 JFrame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cp = getContentPane();
        cp.setBackground(Color.ORANGE);
        cp.setLayout((new FlowLayout()));

        cp.add(new JButton("OK"));
        /*
        JButton bCancel = new JButton("Cancel");
        cp.add(bCancel);
        JButton bIgnore = new JButton("Ignore");
        cp.add(bIgnore);
        */
        setSize(350,400);
        setVisible(true);
    }

    public static void main(String[] args) {
        ContentPaneEx cp = new ContentPaneEx();
    }
}
