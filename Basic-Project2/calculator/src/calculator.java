import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class calculator extends JFrame {
    String x, operator, y;
    JTextField res;

    public calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame frame = new JFrame();                 // 부모 프레임 생성
        frame.setSize(500, 270);    // 계산기 사이즈 설정
        //frame.setLocationRelativeTo(null);          // 프레임을 화면 가운데에 배치
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(null);

        x = operator = y = "";
        res = new JTextField(40);
        JPanel numberContainer = new JPanel();
        numberContainer.setBounds(0,0,500,30); // 가로 세로위치, 가로 세로 길이
        numberContainer.add(res);

        // 계산기, 버튼 container
        JPanel baseContainer = new JPanel();
        JPanel nbuttonContainer = new JPanel();

        // 버튼 container (clear - enter)
        setContentPane(baseContainer);
        baseContainer.setLayout(new BorderLayout());
        baseContainer.setBounds(0,30,500,200);

        JButton btc = new JButton("Clear");
        JButton bte = new JButton("Enter");
        btc.addActionListener(new MyActionListener());
        bte.addActionListener(new MyActionListener());

        baseContainer.add(btc, BorderLayout.NORTH);
        baseContainer.add(bte, BorderLayout.SOUTH);
        // 숫자 버튼 판넬
        baseContainer.add(nbuttonContainer, BorderLayout.CENTER);
        nbuttonContainer.setLayout(new GridLayout(4,4,5,5));

        JButton[] bt = new JButton[16];

        bt[0] = new JButton("1");
        bt[1] = new JButton("2");
        bt[2] = new JButton("3");
        bt[3] = new JButton("+");

        bt[4] = new JButton("4");
        bt[5] = new JButton("5");
        bt[6] = new JButton("6");
        bt[7] = new JButton("-");

        bt[8] = new JButton("7");
        bt[9] = new JButton("8");
        bt[10] = new JButton("9");
        bt[11] = new JButton("*");

        bt[12] = new JButton("0");
        bt[13] = new JButton(".");
        bt[14] = new JButton("√");
        bt[15] = new JButton("/");

        for (int i=0; i<16; i++){
            bt[i].addActionListener(new MyActionListener());
            nbuttonContainer.add(bt[i]);
        }

        // 컴포넌트 추가
        frame.getContentPane().add(numberContainer);
        frame.getContentPane().add(baseContainer);

        frame.setVisible(true);
    }

    class MyActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();

            if(('0' <= s.charAt(0) && s.charAt(0) <= '9') || s.charAt(0) == '.') {
                if (!operator.equals("")) y += s;
                else x += s;
                res.setText(x + operator + y);
            } else if (s.charAt(0) == '√'){
                if (!operator.equals("")) y = Double.toString(Math.sqrt(Double.parseDouble(y)));
                else x = Double.toString(Math.sqrt(Double.parseDouble(x)));
                res.setText(x + operator + y);
            } else if (s.charAt(0) == 'C'){
                x = operator = y = "";
                res.setText(x + operator + y);
            } else if (s.charAt(0) == 'E'){
                double d = switch (operator) {
                    case "+" -> (Double.parseDouble(x) + Double.parseDouble(y));
                    case "-" -> (Double.parseDouble(x) - Double.parseDouble(y));
                    case "/" -> (Double.parseDouble(x) / Double.parseDouble(y));
                    default -> (Double.parseDouble(x) * Double.parseDouble(y));
                };
                x = Double.toString(d);
                res.setText(x);
                x = operator = y = "";
            } else {
                if (operator.equals("") || y.equals("")) operator = s;
                else {
                    double d = switch (operator) {
                        case "+" -> (Double.parseDouble(x) + Double.parseDouble(y));
                        case "-" -> (Double.parseDouble(x) - Double.parseDouble(y));
                        case "/" -> (Double.parseDouble(x) / Double.parseDouble(y));
                        default -> (Double.parseDouble(x) * Double.parseDouble(y));
                    };
                    x = Double.toString(d);
                    operator = s;
                    y = "";
                }
                res.setText(x + operator + y);
            }
        }
    }

    public static void main(String[] args) {
        // 계산기 실행
        new calculator();
    }
}