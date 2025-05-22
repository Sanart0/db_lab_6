package dao;

import model.Lecture;
import model.Student;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO {
    private Connection conn;

    public LectureDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Lecture lecture) throws SQLException {
        String sql = "INSERT INTO lectures (name, date, time_start, time_end, professor_name) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, lecture.getName());
            stmt.setDate(2, Date.valueOf(lecture.getDate()));
            stmt.setTime(3, Time.valueOf(lecture.getTimeStart()));
            stmt.setTime(4, Time.valueOf(lecture.getTimeEnd()));
            stmt.setString(5, lecture.getProfessorName());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lecture.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Lecture lecture) throws SQLException {
        String sql = "UPDATE lectures SET name = ?, date = ?, time_start = ?, time_end = ?, professor_name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecture.getName());
            stmt.setDate(2, Date.valueOf(lecture.getDate()));
            stmt.setTime(3, Time.valueOf(lecture.getTimeStart()));
            stmt.setTime(4, Time.valueOf(lecture.getTimeEnd()));
            stmt.setString(5, lecture.getProfessorName());
            stmt.setInt(6, lecture.getId());
            stmt.executeUpdate();
        }
    }

    public List<Lecture> findAll() throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM lectures";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
        return lectures;
    }

    public Lecture findById(int id) throws SQLException {
        String sql = "SELECT * FROM lectures WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lecture(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time_start").toLocalTime(),
                        rs.getTime("time_end").toLocalTime(),
                        rs.getString("professor_name")
                    );
                }
            }
        }
        return null;
    }

    public List<Student> findStudentsByLectureId(int lectureId) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM students s JOIN student_lectures sl ON s.id = sl.student_id WHERE sl.lecture_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lectureId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("student_group"),
                        rs.getInt("course")
                    ));
                }
            }
        }
        return students;
    }
}
