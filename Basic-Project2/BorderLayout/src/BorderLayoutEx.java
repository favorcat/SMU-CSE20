import javax.swing.*; import java.awt.*;

public class BorderLayoutEx extends JFrame { BorderLayoutEx() {
    setTitle("BorderLayout Sample");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel baseContainer = new JPanel();
    JPanel upperContainer = new JPanel();

    setContentPane(baseContainer);
    baseContainer.setLayout(new BorderLayout());
    baseContainer.add(new JButton("1"), BorderLayout.NORTH);
    baseContainer.add(new JButton("2"), BorderLayout.SOUTH);
    baseContainer.add(new JButton("3"), BorderLayout.EAST);
    baseContainer.add(new JButton("4"), BorderLayout.WEST);
    baseContainer.add(upperContainer, BorderLayout.CENTER);

    upperContainer.setLayout(new BorderLayout());
    upperContainer.add(new JButton("5"), BorderLayout.NORTH);
    upperContainer.add(new JButton("6"), BorderLayout.SOUTH);
    upperContainer.add(new JButton("7"), BorderLayout.EAST);
    upperContainer.add(new JButton("8"), BorderLayout.WEST);
    upperContainer.add(new JButton("9"), BorderLayout.CENTER);

    setSize(600, 400);
    setVisible(true);
}

    public static void main(String[] args) {
        new BorderLayoutEx();
    }
}