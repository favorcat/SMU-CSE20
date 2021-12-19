public class Operator {
    // Operator로 커넥터, 로그인프레임, 회원가입프레임, 메인프레임 선언
    MyConnector connector = null;

    LoginFrame lf = null;
    SignUpFrame sf = null;

    FindIDFrame fidf = null;
    FindPWFrame fpwf = null;

    MainFrame mf = null;

    MakingRoomFrame mrf = null;
    ChatFrame cf = null;
    RoomPWFrame rpwf = null;

    public static void main(String[] args) {
        Operator operator = new Operator();
        operator.connector = new MyConnector();

        operator.lf = new LoginFrame(operator);
        operator.sf = new SignUpFrame(operator);

        operator.fidf = new FindIDFrame(operator);
        operator.fpwf = new FindPWFrame(operator);

        operator.mf = new MainFrame(operator);

        operator.mrf = new MakingRoomFrame(operator);
        operator.cf = new ChatFrame(operator);
//        operator.rpwf = new RoomPWFrame(operator);
    }
}
