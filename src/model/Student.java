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
