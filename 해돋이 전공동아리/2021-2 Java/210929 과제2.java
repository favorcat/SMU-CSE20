import java.util.Scanner;
public class Main {
    public static void main(String[]args){
        Scanner scan = new Scanner(System.in);
        int rand = (int)(Math.random()*100+1);
        int cnt = 1;
        
        while(true){
            int n = scan.nextInt();
            if (n==rand) break;
            else {
                if (n>rand) System.out.println("더 작은 수를 입력하세요");
                if (n<rand) System.out.println("더 큰 수를 입력하세요");
                cnt++;
            }
        }
        System.out.println("맞췄습니다");
        System.out.println("시도횟수는 "+ cnt +"번 입니다.");
    }
}