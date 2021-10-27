import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LoginChecker {
    ArrayList<User> userInfor = new ArrayList<User>();
    File dataFile = new File("../users.txt");
    String readData = null;
    StringTokenizer st;

    LoginChecker(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            // 한 줄씩 읽는다
            while((readData=br.readLine()) != null){
                st = new StringTokenizer(readData, "//");
                String userID = st.nextToken();
                String userPassword = st.nextToken();
                User user = new User(userID, userPassword);
                userInfor.add(user);
            }
            br.close();
        } catch(Exception e){
            System.out.println("Server> LoginChecker 에러 발생");
        }
    }

    boolean check(String _id, String _pass){
        boolean flag = false;
        for(int i=0; i<userInfor.size(); i++){
            if(userInfor.get(i).id.equals(_id) && userInfor.get(i).pass.equals(_pass))
                flag = true;
        }
        return flag;
    }
}
