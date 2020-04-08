public class Student {

    private int student_id;
    private String nummer;
    private String voornaam;
    private String tussenvoegsel;
    private String achternaam;

    public Student(String nummer, String voornaam, String achternaam) {
        this.nummer = nummer;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.tussenvoegsel = "";
    }

    public Student(String nummer, String voornaam, String tussenvoegsel, String achternaam) {
        this(nummer, voornaam, achternaam);
        this.tussenvoegsel = tussenvoegsel;
    }

    public Student(int student_id, String nummer, String voornaam, String tussenvoegsel, String achternaam) {
        this(nummer, voornaam, tussenvoegsel, achternaam);
        this.student_id = student_id;
    }

    public int getStudentId() {
        return this.student_id;
    }

    public String getNummer() {
        return this.nummer;
    }

    public String getVoornaam() {
        return this.voornaam;
    }

    public String getTussenvoegsel() {
        return this.tussenvoegsel;
    }

    public String getAchternaam() {
        return this.achternaam;
    }

    @Override
    public String toString() {
        return ((this.voornaam + " " + this.tussenvoegsel).trim() + " " + this.achternaam).trim() + " (" + this.nummer + ")";
    }
}
