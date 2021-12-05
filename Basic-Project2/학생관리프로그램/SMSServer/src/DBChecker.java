import java.sql.*;

public class DBChecker {
    // DB 서버와 연동할 객체
    Connection con = null;
    // DB와 서버가 메세지를 주고 받을 객체
    Statement stmt = null;
    // DB의 스키마 주소
    String url = "jdbc:mysql://127.0.0.1:3306/university?serverTimezone=Asia/Seoul";
    // DB ID 및 PW
    String DBuser = "root";
    String passwd = "1234";

    // 입력 받은 ID와 PW가 DB와 일치하는지 검사
    boolean check(String _id, String _pass) {
        // 로그인 성공 flag 설정
        boolean flag = false;

        try {
            // mySql 연동을 책임질 클래스를 가지고 옴, 필수 작성 코드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            con = DriverManager.getConnection(url, DBuser, passwd);
            stmt = con.createStatement();
            // user 테이블에서 입력 받은 ID와 PW를 모두 만족하는 것이 있는지 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM user where id like '"+ _id +"' and password like '" + _pass +"'");
            while (result.next()) {
                // DB로부터 받아온 결과 중 아이디와 비밀번호가 일치하는지 검사
                if (result.getString("id").equals(_id) && result.getString("password").equals(_pass)) {
                    // 일치하면 flag를 true로 바꾸고 종료함
                    flag = true;
                    break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // flag 반환
        return flag;
    }

    // 회원가입 처리
    boolean signupCheck(String _id, String _pass){
        boolean flag = true;
        try {
            // mySql 연동을 책임질 클래스를 가지고 옴, 필수 작성 코드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            con= DriverManager.getConnection(url, DBuser, passwd);
            stmt = con.createStatement();

            // 이미 존재하는 아이디인지 중복 검사를 위해 입력 받은 ID와 일치하는 데이터가 있는지 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM user where id like '"+ _id +"'");
            while (result.next()) {
                if (result.getString("id").equals(_id)) {
                    // DB에 이미 아이디가 존재하면 회원가입 안함
                    flag = false;
                    break;
                }
            }
            if (flag){ // 앞에서 한 중복 검사에서 false로 바뀌지 않았다면, 테이블에 데이터 추가
                stmt.executeUpdate("INSERT INTO user VALUES ('" + _id + "', '"+ _pass + "')");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag; // flag 반환
    }

    // department 테이블 데이터 확인
    String departmentCheck(){
        String res = "";
        try {
            // mySql 연동을 책임질 클래스를 가지고 옴, 필수 작성 코드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            con= DriverManager.getConnection(url, DBuser, passwd);
            stmt = con.createStatement();

            // department 테이블의 모든 데이터 질의
            ResultSet result = stmt.executeQuery("SELECT * FROM departments");
            while (result.next()) {
                // 각 데이터는 "//"로 구분하여 문자열에 추가
                res += "//" + result.getString("dname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 만든 문자열 반환
        return res;
    }

    // 학생 데이터 검색
    String searchStudent(String _dep, int _year, int _grade){
        // 학부생, 대학원생 모두 체크가 되어있지 않다면 "" 반환
        if (_grade == 3) return "";

        // 질의문에 where이 사용되었는지에 대한 flag
        boolean flag = false;
        // 질의 결과를 담을 문자열
        String res = "";
        try {
            // mySql 연동을 책임질 클래스를 가지고 옴, 필수 작성 코드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            con= DriverManager.getConnection(url, DBuser, passwd);
            stmt = con.createStatement();
            // students 테이블에서 검색
            String query = "SELECT * FROM students";

            // 학부생, 대학원생 모두 선택된 경우가 아니라면 질의문에 where 추가
            if (_grade != 0) { query += " where"; flag = true; }
            // 질의문에 학부생/대학원생 조건 추가
            if (_grade == 1){
                query += " grade like '학부생'";
            } else if (_grade == 2){
                query += " grade like '대학원생'";
            }

            // 특정 학과가 선택된 경우라면
            if (!(_dep.equals("ALL"))){
                // 질의문에 where이 사용되지 않았다면 where 추가
                if (!flag) { query += " where"; flag = true; }
                // 이미 where이 있다면 조건을 추가하기 위해 and 추가
                else query += " and";
                // 선택된 학과에 대한 조건 추가
                query += " department like \"" + _dep + "\"";
            }

            // 특정 학년이 선택된 경우라면
            if (_year != 0){
                // 질의문에 where이 사용되지 않았다면 where 추가
                if (!flag) { query += "where"; flag = true; }
                // 이미 where이 있다면 조건을 추가하기 위해 and 추가
                else query += " and";
                // 선택된 학년에 대한 조건 추가
                query += " year =" + _year;
            }
            // 학과 이름 > 대학원생/학부생 > 학년 > 학생 이름 오름차순으로 정렬
            query += " order by department, grade, year, sname asc";

            // 요청한 질의문 콘솔에 확인
            System.out.println("QUERY >> " + query);

            // 질의를 통해 얻어온 결과
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                // 각 학생간의 구분은 "//"로 함
                // 각 결과는 한 학생에 대한 데이터이므로 ", "로 구분하여 반환할 문자열에 추가
                res += "//" + result.getString("department") + ", " +   // 학과
                                result.getString("year") + ", " +       // 학년
                                result.getString("sname") + ", " +      // 이름
                                result.getString("grade") + ", " +      // 학부생/대학원생
                                result.getString("snum") + "";          // 학번
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 질의를 통해 얻은 결과로 만든 문자열 반환
        return res;
    }
}
