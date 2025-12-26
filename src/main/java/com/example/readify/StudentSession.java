package com.example.readify;

public class StudentSession {

    private static StudentSession instance;

    private int studentId;
    private String studentName;
    private String studentEmail;

    private StudentSession() { }

    public static StudentSession getInstance() {
        if (instance == null) {
            instance = new StudentSession();
        }
        return instance;
    }

    // Setters
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    // Getters
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }

    // Clear session
    public void clearSession() {
        this.studentId = 0;
        this.studentName = null;
        this.studentEmail = null;
    }
}
