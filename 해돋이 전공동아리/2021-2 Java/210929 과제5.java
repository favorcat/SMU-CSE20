public class Main {
    public static void main(String[]args){
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                if (j==i) System.out.print("*");
                else if (j==(6-i)) System.out.print("*");
                else System.out.print(" ");
            }
            System.out.println();
        }
    }
}