import java.sql.*;

public class DBChecker {
    // DB 서버와 연동할 객체
    Connection con = null;
    // DB와 서버가 메세지를 주고 받을 객체
    Statement stmt = null;
    Statement instmt = null;
    // DB의 스키마 주소
    String url = "jdbc:mysql://127.0.0.1:3306/chatting?serverTimezone=Asia/Seoul";
    // DB ID 및 PW
    String DBuser = "root";
    String passwd = "1234";

    DBChecker(){
        try {
            // mySql 연동을 책임질 클래스를 가지고 옴, 필수 작성 코드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(url, DBuser, passwd);
            stmt = con.createStatement();
            instmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 입력 받은 ID와 PW가 DB와 일치하는지 검사하여 닉네임 반환
    String check(String _id, String _pw) {
        String _nickname = "";
        try {
            // user 테이블에서 입력 받은 ID와 PW를 모두 만족하는 것이 있는지 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM user where id like '"+ _id +"' and pw like '" + _pw +"'");
            while (result.next()) {
                // DB로부터 받아온 결과 중 아이디와 비밀번호가 일치하는지 검사
                if (result.getString("id").equals(_id) && result.getString("pw").equals(_pw)) {
                    // 일치하면 닉네임을 가져옴
                    _nickname = result.getString("nickname");
                    break;
                }
            }
            if (!_nickname.equals("")) {
                ResultSet res = stmt.executeQuery("SELECT * FROM connected order by cnum");
                res.last();
                int cnt = res.getRow();
                if (cnt == 0) cnt = 1;
                else { cnt = res.getInt("cnum") + 1; }
                instmt.executeUpdate("INSERT INTO connected VALUES ('"+ cnt +"', '"+ _nickname +"', '0')");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 닉네임 반환, 공백으로 반환한다면 ID와 PW가 불일치하다는 것
        return _nickname;
    }

    void delConnectedAll() {
        try {
            stmt.executeUpdate("delete from connected");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void delConnected(String _nickname) {
        try {
            stmt.executeUpdate("delete from connected where nickname like '" + _nickname + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // 회원가입 처리
    boolean signupCheck(String _id, String _pw, String _name, String _num, String _nickname){
        boolean flag = true;
        try {
            stmt.executeUpdate("INSERT INTO user VALUES ('" + _id + "', '"+ _pw + "', '"+ _name + "', '"+ _num + "', '"+ _nickname + "')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // ID나 닉네임이 unique에 해당하지 않는다면 false
            flag = false;
        }
        return flag; // flag 반환
    }

    String findID(String _name, String _num){
        String _id = "";
        try {
            // user 테이블에서 입력 받은 이름과 연락처 모두 만족하는 것이 있는지 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM user where name like '"+ _name +"' and num like'" + _num +"'");
            while (result.next()) {
                // DB로부터 받아온 결과 중 이름과 연락처가 일치하는지 검사
                if (result.getString("name").equals(_name) && result.getString("num").equals(_num)) {
                    // 일치하면 닉네임 반환
                    _id = result.getString("id");
                    break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return _id;
    }

    String findPW(String _id, String _name, String _num){
        String _pw = "";
        try {
            // user 테이블에서 입력 받은 이름과 연락처 모두 만족하는 것이 있는지 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM user where id like '"+ _id +"' and name like '"+ _name +"' and num like '" + _num +"'");
            while (result.next()) {
                // DB로부터 받아온 결과 중 이름과 연락처가 일치하는지 검사
                if (result.getString("id").equals(_id) && result.getString("name").equals(_name) && result.getString("num").equals(_num)) {
                    // 일치하면 비밀번호 반환
                    _pw = result.getString("pw");
                    break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return _pw;
    }

    // 해당 경로의 접속자 목록
    String getOnlineUser(int _roomnum){
        String userList = "";
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM connected where path = "+ _roomnum + " order by cnum");
            while (result.next()){
                userList += result.getString("nickname") + ", ";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    String getRoomList(){
        String res = "";
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM chatroom order by roomid");
            while (result.next()){
                ResultSet usrList = instmt.executeQuery("SELECT * FROM connected where path = " + result.getString("roomid") + " order by cnum desc");
                int cnt = 0;
                String leader = "";
                while (usrList.next()){
                    cnt++;
                    leader = usrList.getString("nickname");
                }
                if (cnt != 0) {
                    res += "//" + result.getString("roomid") + ", " +
                            result.getString("title") + ", " +
                            leader + ", " + cnt;
                } else {
                    delRoom(result.getInt("roomid"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    boolean getRoomUsercnt(int _path){
        boolean flag = true;
        try {
            ResultSet result = instmt.executeQuery("SELECT * FROM chatroom where roomid = "+ _path + " order by roomid");
            int cnt = 0;
            while(result.next()){
                cnt = result.getInt("maxnum");
            }
            String [] userNum = getOnlineUser(_path).split(", ");
            if (userNum.length == cnt) flag = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    String getRoomPW(int _path){
        String pw = "";
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM chatroom where roomid = "+ _path + " order by roomid");
            while (result.next()){
                pw = result.getString("pw");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pw;
    }

    // 채팅방 생성
    int makeRoom(String _title, String _pw, String _num, String _nickname){
        try {
            ResultSet res = stmt.executeQuery("SELECT * FROM chatroom order by roomid");
            res.last();
            int cnt = res.getRow();
            if (cnt == 0) cnt = 1;
            else { cnt = res.getInt("roomid") + 1; }
            instmt.executeUpdate("INSERT INTO chatroom VALUES ('" + cnt + "', '"+ _title + "', '"+ _pw + "', '"+ _num + "')");
            moveUserPath(_nickname, cnt);
            return cnt;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    void moveUserPath(String _nickname, int _path){
        try {
            stmt.executeUpdate("update connected set path = " + _path + " where nickname like '" + _nickname + "'");
            String check = getRoomList();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void delRoom(int _path){
        try {
            stmt.executeUpdate("delete from chatroom where roomid = " + _path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    void delRoomAll() {
        try {
            stmt.executeUpdate("delete from chatroom");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
