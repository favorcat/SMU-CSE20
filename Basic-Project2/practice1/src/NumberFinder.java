public class NumberFinder {
    public static void main(String[] args) {
        InputManager im = new InputManager();
        int n = im.input();
        int check = 0;

        for(int i=n; i<=500; i++){
            if (i%n == 0){
                System.out.print(i+" ");
                check++;
                if (check%5 == 0)   System.out.print("\n");
            }
        }
    }
}
