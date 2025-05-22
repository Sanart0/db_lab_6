# Реалізація об’єктно-реляційного відображення

## 1. Створення бази даних MySQL

Було створено базу даних `schedule_db` у MySQL для зберігання даних.

```
CREATE DATABASE schedule_db;
USE schedule_db;

CREATE TABLE students (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  student_group VARCHAR(50) NOT NULL,
  course INT NOT NULL
);

CREATE TABLE lectures (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  date DATE NOT NULL,
  time_start TIME NOT NULL,
  time_end TIME NOT NULL,
  professor_name VARCHAR(100) NOT NULL
);

CREATE TABLE student_lectures (
  student_id INT,
  lecture_id INT,
  PRIMARY KEY (student_id, lecture_id),
  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  FOREIGN KEY (lecture_id) REFERENCES lectures(id) ON DELETE CASCADE
);
```

## 2. Створення Java-проєкту

Для реалізації завдання створено консольний Java-проєкт без використання IDE. Усі дії виконувались через термінал Linux.

```
mkdir -p src/model src/dao src/test
```

**Структура проєкту:**

```
src/
├── model/      — bean-класи (модель таблиць)
├── dao/        — реалізація DAO
└── test/       — тестовий клас
````

## 3. Створення таблиці `students`

У базі даних `schedule_db` створено таблицю `students` зі структурою:

- `id` — унікальний ідентифікатор
- `name` — ім’я студента
- `student_group` — група студента
- `course` — курс студента

```
CREATE TABLE students (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  student_group VARCHAR(50) NOT NULL,
  course INT NOT NULL
);
```

## 4. Створення таблиці `lectures`

У базі даних `schedule_db` створено таблицю `lectures` зі структурою:

- `id` — унікальний ідентифікатор
- `name` — ім’я лекції
- `date` — дата проведення лекції
- `time_start` — час початку лекції
- `time_end` — час закінчення лекції
- `professor_name` — ім’я викладача

```
CREATE TABLE lectures (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  date DATE NOT NULL,
  time_start TIME NOT NULL,
  time_end TIME NOT NULL,
  professor_name VARCHAR(100) NOT NULL
);
```

## 5. Створення таблиці `student_lectures`

У базі даних `schedule_db` створено таблицю `student_lectures` зі структурою:

- `student_id` — ідентифікатор студента
- `lecture_id` — ідентифікатор лекції

```
CREATE TABLE student_lectures (
  student_id INT,
  lecture_id INT,
  PRIMARY KEY (student_id, lecture_id),
  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  FOREIGN KEY (lecture_id) REFERENCES lectures(id) ON DELETE CASCADE
);
```

## 6. Створення bean-класу

Створено клас `Student` (у `src/model/Student.java`), який описує один запис таблиці `students`.

```java
package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private String group;
    private int course;
    private List<Lecture> lectures;

    public Student(int id, String name, String group, int course) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.course = course;
        this.lectures = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGroup() { return group; }
    public int getCourse() { return course; }
    public List<Lecture> getLectures() { return lectures; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGroup(String group) { this.group = group; }
    public void setCourse(int course) { this.course = course; }
    public void setLectures(List<Lecture> lectures) { this.lectures = lectures; }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", course=" + course +
                '}';
    }
}
````

## 7. Створення bean-класу

Створено клас `Lecture` (у `src/model/Lecture.java`), який описує один запис таблиці `lectures`.

```java
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

    // Getters and Setters
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
````

## 8. Розробка DAO-інфраструктури

У пакеті `src/dao/` реалізовано клас `StudentDAO.java`, який містить методи для:

* вставки нового студента
* оновлення студента
* пошука студента
* запису студента на лекцію
* пошука лекцій студента

```java
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
```

## 9. Розробка DAO-інфраструктури

У пакеті `src/dao/` реалізовано клас `LectureDAO.java`, який містить методи для:

* вставки нової лекції
* оновлення лекції
* пошука лекції
* пошука студентів лекції

```java
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
```

## 10. Тестування DAO

У `src/test/TestDAO.java` створено тестовий клас `TestDAO`, який:

* підключається до бази `schedule_db`
* використовує `StudentDAO` та `LectureDAO` для роботи з базою

```java
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
        String password = "user";

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
            System.out.println("\nLectures for student Artur Sanytskyi One:");
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
```

## 11. Запуск програми

**Компіляція:**

```bash
javac -cp "lib/mysql-connector-j-<version>.jar" src/**/*.java
```

**Запуск:**

```bash
java -cp "lib/mysql-connector-j-<version>.jar:src" test.TestDAO
```

**Перевірка результатів:**

```
All Students:
Student{id=1, name='Artur Sanytskyi One', group='IO-36', course=2}
Student{id=2, name='Artur Sanytskyi Two', group='IO-36', course=2}

All Lectures:
Lecture{id=1, name='Організація баз даних', date=2025-05-30, timeStart=12:20, timeEnd=13:55, professorName='Болдак Андрій Олександрович'}
Lecture{id=2, name='Алгоритми та методи обчислень', date=2025-05-26, timeStart=12:20, timeEnd=13:55, professorName='Новотарський Михайло Анатолійович'}

Lectures for student Artur Sanytskyi One:
Lecture{id=1, name='Організація баз даних', date=2025-05-30, timeStart=12:20, timeEnd=13:55, professorName='Болдак Андрій Олександрович'}
Lecture{id=2, name='Алгоритми та методи обчислень', date=2025-05-26, timeStart=12:20, timeEnd=13:55, professorName='Новотарський Михайло Анатолійович'}

Students in Database Systems lecture:
Student{id=1, name='Artur Sanytskyi One', group='IO-36', course=2}
Student{id=2, name='Artur Sanytskyi Two', group='IO-36', course=2}

Updated student:
Student{id=1, name='Artur Sanytskyi One', group='IO-36', course=3}

Updated lecture:
Lecture{id=2, name='Алгоритми та методи обчислень', date=2025-05-26, timeStart=12:20, timeEnd=13:55, professorName='Порєв Віктор Миколайович'}
```

## 12. Висновок

Успішно реалізовано DAO шаблон для системи управління навчальним процесом з використанням Java та MySQL.
 
Створено базу даних schedule_db з трьома взаємопов'язаними таблицями:
* students - для зберігання інформації про студентів
* lectures - для зберігання розкладу лекцій
* student_lectures - для реалізації зв'язків між студентами та лекціями

Розроблено об'єктну модель:
* Клас Student з усіма необхідними полями та методами
* Клас Lecture з підтримкою дати та часу проведення

Реалізовано повноцінні DAO класи:
* StudentDAO з методами CRUD та управління лекціями
* LectureDAO з методами CRUD та управління студентами

Забезпечено двосторонній зв'язок між об'єктами:
* Студент знає свої лекції
* Лекція знає своїх студентів

Протестовано всі основні операції:
* Додавання/редагування студентів та лекцій
* Запис студентів на лекції
* Отримання пов'язаних даних

Підтверджено коректну роботу програми з реальною базою даних MySQL.
Дана реалізація демонструє ефективне використання патерну DAO для створення масштабованої системи управління навчальним процесом з чітким розділенням логіки доступу до даних та бізнес-логіки.
