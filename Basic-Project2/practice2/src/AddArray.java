public class AddArray {
    public static void main(String[] args) {
        InputManager im = new InputManager();
        int[] arr;
        arr = new int[5];
        int sum = 0;

        for(int i=0; i<5; i++){
            int n = im.input();
            arr[i] = n;
        }

        for(int i=0; i<5; i++)  sum += arr[i];
        System.out.println(sum/5);
    }
}
