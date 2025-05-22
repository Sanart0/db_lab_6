package test;

import dao.LectureDAO;
import dao.StudentDAO;
import model.Lecture;
import model.Student;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/schedule_db";
        String user = "user";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Initialize DAOs
            StudentDAO studentDAO = new StudentDAO(conn);
            LectureDAO lectureDAO = new LectureDAO(conn);

            // Create some students
            Student student1 = new Student(0, "Artur Sanytskyi One", "IO-36", 2);
            Student student2 = new Student(1, "Artur Sanytskyi Two", "IO-36", 2);
            studentDAO.insert(student1);
            studentDAO.insert(student2);

            // Create some lectures
            Lecture lecture1 = new Lecture(0, "Організація баз даних", 
                LocalDate.of(2025, 5, 30), 
                LocalTime.of(12, 20), 
                LocalTime.of(13, 55), 
                "Болдак Андрій Олександрович");
                
            Lecture lecture2 = new Lecture(1, "Алгоритми та методи обчислень", 
                LocalDate.of(2025, 5, 26), 
                LocalTime.of(12, 20), 
                LocalTime.of(13, 55), 
                "Новотарський Михайло Анатолійович");
                
            lectureDAO.insert(lecture1);
            lectureDAO.insert(lecture2);

            // Enroll students to lectures
            studentDAO.enrollToLecture(student1.getId(), lecture1.getId());
            studentDAO.enrollToLecture(student1.getId(), lecture2.getId());
            studentDAO.enrollToLecture(student2.getId(), lecture1.getId());

            // Test retrieving data
            System.out.println("All Students:");
            List<Student> students = studentDAO.findAll();
            students.forEach(System.out::println);

            System.out.println("\nAll Lectures:");
            List<Lecture> lectures = lectureDAO.findAll();
            lectures.forEach(System.out::println);

            // Test finding lectures for a student
            System.out.println("\nLectures for student John Doe:");
            List<Lecture> johnsLectures = studentDAO.findLecturesByStudentId(student1.getId());
            johnsLectures.forEach(System.out::println);

            // Test finding students for a lecture
            System.out.println("\nStudents in Database Systems lecture:");
            List<Student> dbStudents = lectureDAO.findStudentsByLectureId(lecture1.getId());
            dbStudents.forEach(System.out::println);

            // Update a student
            student1.setCourse(3);
            studentDAO.update(student1);
            System.out.println("\nUpdated student:");
            System.out.println(studentDAO.findById(student1.getId()));

            // Update a lecture
            lecture2.setProfessorName("Порєв Віктор Миколайович");
            lectureDAO.update(lecture2);
            System.out.println("\nUpdated lecture:");
            System.out.println(lectureDAO.findById(lecture2.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
