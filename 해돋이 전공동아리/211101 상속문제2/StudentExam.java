class StudentExam extends Human{
    String hak;
    StudentExam(String name, int age, int height, int weight, String hak){
        super(name, age, height, weight);
        this.hak=hak;
    }

    @Override
    public String toString() {
        String data = name + "\t" + age + "\t\t\t" + height + "\t\t\t\t" + weight+"\t\t\t"+ hak;
        return data;
    }
}