public class Operator {
    // Operator로 커넥터, 로그인프레임, 회원가입프레임, 메인프레임 선언
    MyConnector connector = null;
    LoginFrame lf = null;
    SignUpFrame sf = null;
    MainFrame mf = null;

    public static void main(String[] args) {
        Operator operator = new Operator();
        operator.connector = new MyConnector();
        operator.lf = new LoginFrame(operator);
        operator.sf = new SignUpFrame(operator);
        operator.mf = new MainFrame(operator);

    }
}
