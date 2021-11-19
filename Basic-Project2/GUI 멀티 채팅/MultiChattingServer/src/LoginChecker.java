import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LoginChecker {
    ArrayList<User> userInfor = new ArrayList<User>();
    File dataFile = new File("/Users/favorcat/Github/SMU-CSE20/Basic-Project2/GUI 멀티 채팅/MultiChattingServer/users.txt");
    String readData = null;
    StringTokenizer st;

    LoginChecker(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile)); // 파일 리더 선언
            while ((readData = br.readLine()) != null) {
                // 끝까지 읽는다.
                st = new StringTokenizer(readData, "//"); // 구분자는
                String userId= st.nextToken(); // 첫번째 받아주기
                String userPassword = st.nextToken(); //  구분자 후 두번째
                String userName = st.nextToken();
                String userNum = st.nextToken();
                User user = new User(userId, userPassword, userName, userNum);
                userInfor.add(user);
            }
            br.close();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    boolean check(String _id, String _pass) {
        boolean flag = false;
        for (User user : userInfor) {
            if (user.id.equals(_id) && user.pass.equals(_pass)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    String getName(String _id) {
        String name = null;
        for (User user : userInfor) {
            if (user.id.equals(_id)) {
                name = user.name;
                break;
            }
        }
        return name;
    }
}
