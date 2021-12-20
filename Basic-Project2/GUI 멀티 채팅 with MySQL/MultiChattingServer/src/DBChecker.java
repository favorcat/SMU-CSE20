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


    // 채팅방 목록 테이블 초기화
    void delChatRoomAll() {
        try {
            stmt.executeUpdate("delete from chatroom");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // 접속자 목록 테이블 초기화
    void delConnectedUserAll() {
        try {
            stmt.executeUpdate("delete from connected");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // 접속자 목록 테이블에서 유저 삭제
    void delConnectedUser(String _nickname) {
        try {
            stmt.executeUpdate("delete from connected where nickname like '" + _nickname + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // 로그인 처리 (아이디, 비밀번호)
    String loginCheck(String _id, String _pw) {
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
            // 로그인 성공 했을 경우, 접속자 목록 테이블에 추가
            if (!_nickname.equals("")) {
                // 현재 접속자 목록 질의
                ResultSet res = stmt.executeQuery("SELECT * FROM connected order by cnum");
                res.last();
                int cnt = res.getRow(); // 접속자 목록 테이블에서 접속자 수를 받아옴
                if (cnt == 0) cnt = 1; // 첫 번째 접속이라면 1
                else { cnt = res.getInt("cnum") + 1; } // 아니라면 가장 마지막 접속자의 번호 + 1
                // 접속자 목록 테이블에 추가
                instmt.executeUpdate("INSERT INTO connected VALUES ('"+ cnt +"', '"+ _nickname +"', '0')");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 닉네임 반환, 공백으로 반환한다면 ID와 PW가 불일치하다는 것
        return _nickname;
    }


    // 회원가입 처리 (아이디, 비밀번호, 이름, 연락처, 닉네임)
    boolean signupCheck(String _id, String _pw, String _name, String _num, String _nickname){
        boolean flag = true;
        try {
            // 유저 테이블에 추가
            stmt.executeUpdate("INSERT INTO user VALUES ('" + _id + "', '"+ _pw + "', '"+ _name + "', '"+ _num + "', '"+ _nickname + "')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // ID나 닉네임이 unique에 해당하지 않는다면 false
            flag = false;
        }
        return flag; // flag 반환
    }

    // 아이디 찾기 처리 (이름, 연락처)
    String findUserID(String _name, String _num){
        String _id = "";
        try {
            // 유저 테이블에서 입력 받은 이름과 연락처 모두 만족하는 것이 있는지 질의
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
        return _id; // 아이디 반환
    }

    // 비밀번호 찾기 처리 (아이디, 이름, 연락처)
    String findUserPW(String _id, String _name, String _num){
        String _pw = "";
        try {
            // 유저 테이블에서 입력 받은 이름과 연락처 모두 만족하는 것이 있는지 질의
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
        return _pw; // 비밀번호 반환
    }

    // 채팅방 생성 처리 (채팅방 제목, 채팅방 비밀번호, 채팅방 최대인원, 유저 닉네임)
    int makeChatRoom(String _title, String _pw, String _num, String _nickname){
        try {
            // 현재 존재하는 채팅방 목록 질의
            ResultSet res = stmt.executeQuery("SELECT * FROM chatroom order by roomid");
            res.last();
            int cnt = res.getRow(); // 채팅방 수 받아옵
            if (cnt == 0) cnt = 1;  // 첫번째 채팅방이라면 1
            else { cnt = res.getInt("roomid") + 1; } // 아니라면 가장 마지막 채팅방 번호 + 1
            // 채팅방 목록 테이블에 추가
            instmt.executeUpdate("INSERT INTO chatroom VALUES ('" + cnt + "', '"+ _title + "', '"+ _pw + "', '"+ _num + "')");
            // 채팅방 생성을 요청한 유저의 현재 경로 변경
            moveUserPath(_nickname, cnt);
            // 채팅방 번호 반환
            return cnt;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    // 채팅방의 접속 인원이 최대인원인지 검사
    boolean getChatRoomUserCNT(int _path){
        boolean flag = true;
        try {
            // 채팅방 목록 테이블에서 해당 채팅방의 최대인원 질의
            ResultSet result = instmt.executeQuery("SELECT * FROM chatroom where roomid = "+ _path + " order by roomid");
            int cnt = 0;
            while(result.next()){
                cnt = result.getInt("maxnum");
            }
            // 해당 채팅방의 접속자 목록 반환을 통해 접속자 수 파악
            String [] userNum = getOnlineUserList(_path).split(", ");
            // 만약 최대인원이라면 false 반환
            if (userNum.length == cnt) flag = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    // 해당 경로의 접속자 목록 반환
    String getOnlineUserList(int _path){
        String userList = "";
        try {
            // 해당 경로로 되어있는 접속 유저 테이블 질의, 접속 순서 오름차순으로 반환
            ResultSet result = stmt.executeQuery("SELECT * FROM connected where path = "+ _path + " order by cnum");
            while (result.next()){
                // 유저 목록 문자열에 닉네임 추가
                userList += result.getString("nickname") + ", ";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList; // 접속자 목록 반환
    }

    // 채팅방 목록 반환
    String getChatRoomList(){
        String res = "";
        try {
            // 채팅방 목록 테이블에서 채팅방 번호 오름차순으로 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM chatroom order by roomid");
            while (result.next()){
                // 해당 경로로 되어있는 접속 유저 테이블 질의, 접속 순서 내림차순으로 반환
                ResultSet usrList = instmt.executeQuery("SELECT * FROM connected where path = " + result.getString("roomid") + " order by cnum desc");
                int cnt = 0;        // 접속자 수
                String leader = ""; // 방장 닉네임
                while (usrList.next()){
                    cnt++;
                    // 가장 마지막에 담긴 닉네임이 방장 (내림차순 반환이므로 가장 먼저 접속한 사람이 들어옴)
                    leader = usrList.getString("nickname");
                }
                if (cnt != 0) { // 접속자 수가 0명이 아닐 경우
                    // 반환할 결과 문자열에 채팅방 번호, 채팅방 제목, 방장 닉네임, 접속인원 추가
                    res += "//" + result.getString("roomid") + ", " +
                            result.getString("title") + ", " +
                            leader + ", " + cnt;
                } else { // 접속자 수가 0명일 경우, 채팅방 삭제
                    delChatRoom(result.getInt("roomid"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    // 채팅방 삭제 (채팅방 번호(경로))
    void delChatRoom(int _path){
        try {
            // 채팅방 목록에서 해당 경로를 가진 채팅방 row 삭제
            stmt.executeUpdate("delete from chatroom where roomid = " + _path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // 채팅방 비밀번호 반환 (채팅방 번호(경로))
    String getChatRoomPW(int _path){
        String pw = "";
        try {
            // 채팅방 목록 테이블에서 해당 경로의 채팅방 정보 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM chatroom where roomid = "+ _path);
            while (result.next()){
                // 채팅방 비밀번호 설정
                pw = result.getString("pw");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pw; // 채팅방 비밀번호 반환
    }

    // 유저의 경로 변경 (유저 닉네임, 경로)
    void moveUserPath(String _nickname, int _path){
        try {
            // 접속자 목록 테이블에서 유저 닉네임과 일치하는 데이터의 경로 변경
            instmt.executeUpdate("update connected set path = " + _path + " where nickname like '" + _nickname + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
