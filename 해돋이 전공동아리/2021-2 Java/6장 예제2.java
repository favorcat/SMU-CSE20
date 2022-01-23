import java.util.Scanner;
public class Main {
    public static void main(String[]args){
        Scanner scan = new Scanner(System.in);

        int[] arr = new int[5];
        float res = 0;
        for(int i=0; i<5; i++){
            System.out.print(i+1 +"번째의 정수를 입력하시오: ");
            int n = scan.nextInt();
            arr[i] = n;
            res += arr[i];
        }
        System.out.println("5개의 정수 평균은 : " + res/5);
    }
}