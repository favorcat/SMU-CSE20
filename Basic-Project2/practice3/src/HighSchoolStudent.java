public class HighSchoolStudent extends Student{
    String studentID;
    int garde;

    public static void main(String[] args) {
        Student student = new Student();
        Person person = new Person();
        HighSchoolStudent HSstudent = new HighSchoolStudent();

        student.study();
        person.eating();
        HSstudent.study();
    }

    public void study(){
        System.out.println("HighSchoolStudent study");
    }
}