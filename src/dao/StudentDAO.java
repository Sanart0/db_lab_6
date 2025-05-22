package dao;

import model.Student;
import model.Lecture;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, student_group, course) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGroup());
            stmt.setInt(3, student.getCourse());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, student_group = ?, course = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGroup());
            stmt.setInt(3, student.getCourse());
            stmt.setInt(4, student.getId());
            stmt.executeUpdate();
        }
    }

    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("student_group"),
                    rs.getInt("course")
                ));
            }
        }
        return students;
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("student_group"),
                        rs.getInt("course")
                    );
                }
            }
        }
        return null;
    }

    public void enrollToLecture(int studentId, int lectureId) throws SQLException {
        String sql = "INSERT INTO student_lectures (student_id, lecture_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, lectureId);
            stmt.executeUpdate();
        }
    }

    public List<Lecture> findLecturesByStudentId(int studentId) throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT l.* FROM lectures l JOIN student_lectures sl ON l.id = sl.lecture_id WHERE sl.student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lectures.add(new Lecture(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time_start").toLocalTime(),
                        rs.getTime("time_end").toLocalTime(),
                        rs.getString("professor_name")
                    ));
                }
            }
        }
        return lectures;
    }
}
