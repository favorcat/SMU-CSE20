public class Class2 extends Class1{
    String department;

    Class2(String name, int salary, String department) {
        super(name, salary);
        this.department = department;
    }

    @Override
    public void getInformation() {
        System.out.printf("이름:%s 연봉:%d 부서:%s\n", getName(), getSalary(), department);
    }

    @Override
    public void prn() {
        System.out.println("서브클래스");
    }

    public void callSuperThis() {
        super.prn();
        this.prn();
    }
}