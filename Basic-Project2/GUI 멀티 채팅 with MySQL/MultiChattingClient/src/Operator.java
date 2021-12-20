public class Operator { // Operator로 선언
    // 커넥터
    MyConnector connector = null;

    // 로그인, 회원가입 프레임
    LoginFrame lf = null;
    SignUpFrame sf = null;

    // 아이디, PW 찾기 프레임
    FindIDFrame fidf = null;
    FindPWFrame fpwf = null;

    // 대기실 프레임
    MainFrame mf = null;

    // 채팅방 생성, 채팅방, 채팅방 비밀번호 입력 프레임
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
        operator.rpwf = new RoomPWFrame(operator);
    }
}
