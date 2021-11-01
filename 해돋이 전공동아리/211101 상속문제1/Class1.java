public class Class1 {
    private String name;
    private int salary;
    public Class1() {
    }

    Class1(String n, int s) {
        name = n; salary = s;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public void getInformation() {
        System.out.println("이름:" + name + " 연봉:" + salary);
    }
    
    public void prn() {
        System.out.println("수퍼클래스");
    }
}