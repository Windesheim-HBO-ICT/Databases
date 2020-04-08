import java.util.List;

public class Startup {

    public static void main(String[] args) {
        List<Student> students = StudentRepository.getAllStudents();

        for (Student student : students) {
            System.out.println(student);
        }
    }
}