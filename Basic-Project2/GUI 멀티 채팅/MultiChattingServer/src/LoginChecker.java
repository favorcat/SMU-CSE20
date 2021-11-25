import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

// 로그인 체크하는 클래스
public class LoginChecker {
    // 유저 정보를 배열로 받음
    ArrayList<User> userInfor = new ArrayList<User>();
    // 유저 정보가 저장되어 있는 DB 파일 경로 지정해서 가져옴
    File dataFile = new File("/Users/favorcat/Github/SMU-CSE20/Basic-Project2/GUI 멀티 채팅/MultiChattingServer/users.txt");
    // 유저 정보를 읽어 내면서 가져올 String
    String readData = null;
    // String을 "//"로 쪼개줘야 하니 StringTokenizer 사용
    StringTokenizer st;

    LoginChecker(){
        DBchecker();
    }

    // 회원가입으로 정보 추가할때, 유저 정보 업데이트를 위해 메소드로 구현
    public void DBchecker(){
        try {
            // 파일을 읽기 위해 BufferReader 선언
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            // 파일에 있는 유저 정보를 전부 가져옴
            while ((readData = br.readLine()) != null) {
                // 끝까지 읽는다.
                st = new StringTokenizer(readData, "//"); // 구분자는 // 로 지정
                String userId= st.nextToken(); // 첫번째 받아주기 (유저 ID)
                String userPassword = st.nextToken(); //  구분자 다음 문자열 (유저 비밀번호)
                String userName = st.nextToken(); // 구분자 다음 문자열 (유저 이름)
                String userNum = st.nextToken(); // 구분자 다음 문자열 (유저 연락처)
                // 유저 객체 생성
                User user = new User(userId, userPassword, userName, userNum);
                // 유저 정보를 담을 배열에 유저 추가
                userInfor.add(user);
            }
            // BufferReader 사용 끝났으니 닫아줌
            br.close();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }
    // 입력받은 ID와 PW가 DB와 일치하는지
    boolean check(String _id, String _pass) {
        // 로그인 성공 flag 설정
        boolean flag = false;
        // 리스트에 저장된 유저 정보(DB)를 입력받은 ID와 일치하는지 반복문을 돌면서 검사
        for (User user : userInfor) {
            if (user.id.equals(_id) && user.pass.equals(_pass)) {
                // DB와 일치하면 flag를 true로 바꾸고 종료함
                flag = true;
                break;
            }
        }
        return flag;
    }

    // 유저의 이름(닉네임)을 가져와 채팅의 이름을 설정
    String getName(String _id) {
        String name = null;
        // 유저 DB에서 ID로 검사해서 해당 유저의 이름 가져옴
        for (User user : userInfor) {
            if (user.id.equals(_id)) {
                name = user.name;
                break;
            }
        }
        return name;
    }
}
