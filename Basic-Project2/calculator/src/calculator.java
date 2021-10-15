import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class calculator extends JFrame {
    // x, y 그리고 연산자
    String x, y, operator;
    // 결과값을 보여줄 JTextField
    JTextField result;

    public calculator() {
        // 부모 프레임 생성
        JFrame frame = new JFrame();
        frame.setTitle("GUI 계산기 - 202023041 고은영");
        // 계산기 사이즈 설정
        frame.setSize(520, 405);
        // 프레임을 화면 가운데에 배치
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // x, y, 연산자 초기화
        x = y = operator = "";

        // 연산을 담는 JTextField 생성
        result = new JTextField(40);
        // 결과값 TextField border 설정
        Border outsideBorder = result.getBorder();
        // 위 아래 border 설정
        Border insideBorder = BorderFactory.createEmptyBorder(10, 0, 10, 0);
        result.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));

        // JTextField 를 담을 JPanel 생성
        JPanel numberContainer = new JPanel();
        // numberContainer 위치 설정
        numberContainer.setBounds(5,5,500,50);
        numberContainer.add(result);

        // (clear ~ enter) container, 숫자 및 연산 버튼 container
        JPanel baseContainer = new JPanel();
        JPanel nbuttonContainer = new JPanel();

        // 버튼 container (clear ~ enter) BorderLayout 설정 및 위치 설정
        setContentPane(baseContainer);
        baseContainer.setLayout(new BorderLayout(10,10)); // 간격 조정
        baseContainer.setBounds(5,65,500,300);

        // Clear, Enter 버튼 생성 및 액션리스너 추가
        JButton btc = new JButton("Clear");
        JButton bte = new JButton("Enter");
        btc.addActionListener(new MyActionListener());
        bte.addActionListener(new MyActionListener());
        // 버튼 폰트 설정
        btc.setFont(new Font("맑은 고딕",0,20));
        bte.setFont(new Font("맑은 고딕",0,20));
        // 버튼 글씨 색 설정
        btc.setForeground(Color.GRAY);
        bte.setForeground(Color.GRAY);

        // 버튼 container 의 북쪽과 남쪽에 Clear, Enter 버튼 추가
        baseContainer.add(btc, BorderLayout.NORTH);
        baseContainer.add(bte, BorderLayout.SOUTH);
        // 숫자 버튼 판넬 추가 및 GridLayout 으로 설정
        baseContainer.add(nbuttonContainer, BorderLayout.CENTER);
        nbuttonContainer.setLayout(new GridLayout(4,4,10,10));

        // 숫자 버튼 배열로 생성
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

        // 각 버튼에게 액션리스너 추가 및 GridLayout 에 버튼 추가
        for (int i=0; i<16; i++){
            bt[i].addActionListener(new MyActionListener());
            nbuttonContainer.add(bt[i]);
            // 폰트 설정
            bt[i].setFont(new Font("맑은 고딕",0,26));
        }

        // 컴포넌트 추가
        frame.getContentPane().add(numberContainer);
        frame.getContentPane().add(baseContainer);
        frame.setVisible(true);
    }

    class MyActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // 각 버튼을 String으로 가져오기
            String s = e.getActionCommand();

            // 숫자 및 . 일 경우에
            if(('0' <= s.charAt(0) && s.charAt(0) <= '9') || s.charAt(0) == '.') {
                // 연산자가 입력되지 않았다면 x에 숫자 추가
                if (operator.equals("")) x += s;
                // 연산자가 있다면 y에 숫자 추가
                else y += s;
                // 연산 결과에 출력
                result.setText(x + operator + y);
            } else if (s.charAt(0) == '√'){ // 루트 계산
                if (operator.equals("")) x = Double.toString(Math.sqrt(Double.parseDouble(x)));
                else y = Double.toString(Math.sqrt(Double.parseDouble(y)));
                result.setText(x + operator + y);
            } else if (s.charAt(0) == 'C'){ // Clear 버튼 클릭시 계산 결과 모두 초기화
                x = operator = y = "";
                result.setText(x + operator + y);
            } else if (s.charAt(0) == 'E'){ // Enter 버튼 클릭시 연산자에 따라서 계산 결과 반환
                double d = switch (operator) {
                    case "+" -> (Double.parseDouble(x) + Double.parseDouble(y));
                    case "-" -> (Double.parseDouble(x) - Double.parseDouble(y));
                    case "/" -> (Double.parseDouble(x) / Double.parseDouble(y));
                    default -> (Double.parseDouble(x) * Double.parseDouble(y));
                };
                x = Double.toString(d);
                result.setText(x);
                x = operator = y = ""; // 초기화
            } else {
                // 연산자나 y가 입력되지 않았을 때에는 연산자에 대입
                if (operator.equals("") || y.equals("")) operator = s;
                else {
                    // y가 입력되어 있을때에는 x, y를 연산해 주고, 그 값을 x에 넣는다 (연속 계산)
                    double d = switch (operator) {
                        case "+" -> (Double.parseDouble(x) + Double.parseDouble(y));
                        case "-" -> (Double.parseDouble(x) - Double.parseDouble(y));
                        case "/" -> (Double.parseDouble(x) / Double.parseDouble(y));
                        default -> (Double.parseDouble(x) * Double.parseDouble(y));
                    };
                    x = Double.toString(d);
                    // 입력했던 연산자에 대입
                    operator = s;
                    // y 초기화
                    y = "";
                }
                result.setText(x + operator + y);
            }
        }
    }

    public static void main(String[] args) {
        // 계산기 실행
        new calculator();
    }
}
