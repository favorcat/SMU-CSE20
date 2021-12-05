import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;

public class InsertIntoCustomer {
    public static void main(String args[]) {

        File dataFile = new File("/Users/favorcat/Github/SMU-CSE20/Basic-Project2/MySQL 활용 샘플/students.txt");
        String readData = null;
        StringTokenizer st;

        Connection con = null;
        Statement stmt = null;
        String url = "jdbc:mysql://127.0.0.1:3306/university?serverTimezone=Asia/Seoul";
        String user = "root";
        String passwd = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
            return;
        }

        String insertString = "INSERT INTO students VALUES ";
        // 삽입할 행의 정보를 배열에 저장한 후 이를 이용해 삽입
//        String recordString[] =
//                {insertString + "('C-1001', '가나다', '010-1111-2222', '서울')",
//                        insertString + "('C-1002', '라마바', '010-1111-3333', '부산')",
//                        insertString + "('C-1003', '사아자', '010-1111-4444', '대구')",
//                        insertString + "('C-1004', '가나다', '010-1111-5555', '광주')",
//                        insertString + "('C-1005', '나다라', '010-1111-6666', '대전')",
//                        insertString + "('C-1006', '다라마', '010-1111-7777', '강원')"};

        try {

            con = DriverManager.getConnection(url, user, passwd);
            stmt = con.createStatement();
            BufferedReader br = new BufferedReader(new FileReader(dataFile)); // 파일 리더 선언
            int count = 0;
            while ((readData = br.readLine()) != null) {
                // 끝까지 읽는다.
                st = new StringTokenizer(readData, " "); // 구분자는 "//"
                stmt.executeUpdate(insertString + "('" + st.nextToken() + "', '" + st.nextToken() + "', '" + st.nextToken() + "', '" + st.nextToken() + "', '" + st.nextToken() + "')");
                count++;
            }
            br.close();
            System.out.println(count + "개의 레코드가 테이블에 삽입되었습니다! ");

        } catch(SQLException | IOException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (Exception ignored) {}
        }
    }
}