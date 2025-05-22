package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Lecture {
    private int id;
    private String name;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String professorName;
    private List<Student> students;

    public Lecture(int id, String name, LocalDate date, LocalTime timeStart, 
                   LocalTime timeEnd, String professorName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.professorName = professorName;
        this.students = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public LocalTime getTimeStart() { return timeStart; }
    public LocalTime getTimeEnd() { return timeEnd; }
    public String getProfessorName() { return professorName; }
    public List<Student> getStudents() { return students; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTimeStart(LocalTime timeStart) { this.timeStart = timeStart; }
    public void setTimeEnd(LocalTime timeEnd) { this.timeEnd = timeEnd; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }
    public void setStudents(List<Student> students) { this.students = students; }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", professorName='" + professorName + '\'' +
                '}';
    }
}
