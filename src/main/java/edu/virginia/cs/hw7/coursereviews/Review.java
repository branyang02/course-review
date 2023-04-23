package edu.virginia.cs.hw7.coursereviews;

public class Review {
    private Student student;
    private Course course;
    private String comment;
    private int rating;

    public Review(Student student, Course course, String comment, int rating) {
        this.student = student;
        this.course = course;
        this.comment = comment;
        this.rating = rating;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String toString() {
        return "Review: " + student.getId() + " " + course.getDepartment() + " " + course.getCatalogNumber() + " " + comment + " " + rating;
    }

}
