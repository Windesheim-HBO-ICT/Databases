import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
CREATE TABLE IF NOT EXISTS student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    nummer VARCHAR(10) NOT NULL,
    voornaam VARCHAR(50) NOT NULL,
    tussenvoegsel VARCHAR(10) NULL,
    achternaam VARCHAR(50) NOT NULL
)  ENGINE=INNODB;

INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES ('s12345678', 'Jorick', NULL, 'Veenstra');
INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES ('s27648263', 'Barry', NULL, 'Daalman');
INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES ('s17466283', 'Stephan', 'de', 'Zwart');
INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES ('s47561872', 'Liesbeth', NULL, 'Pas');
INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES ('s75529834', 'Sania', NULL, 'Aaftink');
*/

public class StudentRepository {

    public static Student getStudentById(int student_id) {
        String query = "SELECT * FROM student WHERE student_id = " + student_id;
        List<Map<String, Object>> results = Database.executeQuery(query);

        if (results.size() == 1) {
            Map<String, Object> result = results.get(0);
            return createStudentFromMap(result);
        }

        return null;
    }

    public static List<Student> getAllStudents() {
        String query = String.format("SELECT * FROM student");
        List<Map<String, Object>> results = Database.executeQuery(query);

        ArrayList<Student> students = new ArrayList<>();

        for (Map<String, Object> result : results) {
            students.add(createStudentFromMap(result));
        }

        return students;
    }

    public static List<Student> findStudentsByNaam(String naam) {
        String query = String.format("SELECT * FROM student WHERE voornaam LIKE '%%%s%%' OR achternaam LIKE '%%%s%%'", naam, naam);
        List<Map<String, Object>> results = Database.executeQuery(query);

        ArrayList<Student> students = new ArrayList<>();

        for (Map<String, Object> result : results) {
            students.add(createStudentFromMap(result));
        }

        return students;
    }

    public static void saveStudent(Student student) {
        if (student.getStudentId() > 0) {
            // Update            
            String query = "UPDATE student SET nummer=?, voornaam=?, tussenvoegsel=?, achternaam=? WHERE student_id=?";
            Database.executePrepared(query, student.getNummer(), student.getVoornaam(), student.getTussenvoegsel(), student.getAchternaam(), student.getStudentId());
        } else {
            // Insert
            String query = "INSERT INTO student (nummer, voornaam, tussenvoegsel, achternaam) VALUES(?, ?, ?, ?)";
            Database.executePrepared(query, student.getNummer(), student.getVoornaam(), student.getTussenvoegsel(), student.getAchternaam());
        }
    }

    public static void deleteStudent(Student student) {
        if (student.getStudentId() > 0) {
            String query = String.format("DELETE FROM student WHERE student_id = %d", student.getStudentId());
            Database.executeUpdate(query);
        }
    }

    private static Student createStudentFromMap(Map<String, Object> map) {
        return new Student(
                getInt(map, "student_id"),
                getString(map, "nummer"),
                getString(map, "voornaam"),
                getString(map, "tussenvoegsel"),
                getString(map, "achternaam"));
    }

    private static String getString(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object obj = map.get(key);

            if (obj instanceof String) {
                return (String) obj;
            }
        }

        return "";
    }

    private static int getInt(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object obj = map.get(key);

            if (obj instanceof Integer) {
                return (Integer) obj;
            }
        }

        return 0;
    }
}