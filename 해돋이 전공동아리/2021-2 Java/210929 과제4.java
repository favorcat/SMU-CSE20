public class Main {
    public static void main(String[]args){
        int[] coin = { 500, 100, 50, 10 };
        int money = 2680;
        int fivehund = 0, onehund = 0, fifty = 0, ten = 0;
        
        fivehund = money / 500;
        money = money - 500 * fivehund;
        
        onehund = money / 100;
        money = money - 100 * onehund;

        fifty = money / 50;
        money = money - 50 * fifty;

        ten = money / 10;

        System.out.println("money = " + money);
        System.out.println("500원 : " + fivehund);
        System.out.println("100원 : " + onehund);
        System.out.println("50원 : " + fifty);
        System.out.println("10원 : " + ten);
    }
}