public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StudentExam se[]=new StudentExam[3];
        se[0]=new StudentExam("홍길동",15,171, 81, "201101");
        se[1]=new StudentExam("정길동",13,183, 72, "201102");
        se[2]=new StudentExam("박길동",16,175, 65, "201103");

        System.out.printf("%4s %5s %10s %11s %10s\n","name","나이","신장","몸무게","학번");
        for(StudentExam sm : se) {
            System.out.println(sm.toString());
        }
    }
}
