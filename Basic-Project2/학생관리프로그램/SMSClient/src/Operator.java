public class Operator {
    MyConnector connector = null;
    LoginFrame lf =null;
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