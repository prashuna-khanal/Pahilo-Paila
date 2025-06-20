/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

import java.security.Timestamp;

/**
 *
 * @author LENOVO
 */
public class Application {
    private int id;
    private int jobSeekerId;
    private int recruiterId;
    private int vacancyId;
    private int cvId;
    private String status;
    private java.sql.Timestamp appliedAt;
    private String jobSeekerName;
    private String jobSeekerEmail;
    private Cv cv;

    // Constructor matching ApplicationDao usage
    public Application(int id, int jobSeekerId, int recruiterId, int vacancyId, int cvId, String status,
                       java.sql.Timestamp appliedAt, String jobSeekerName, String jobSeekerEmail, Cv cv) {
        this.id = id;
        this.jobSeekerId = jobSeekerId;
        this.recruiterId = recruiterId;
        this.vacancyId = vacancyId;
        this.cvId = cvId;
        this.status = status;
        this.appliedAt = appliedAt;
        this.jobSeekerName = jobSeekerName;
        this.jobSeekerEmail = jobSeekerEmail;
        this.cv = cv;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getJobSeekerId() {
        return jobSeekerId;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public int getVacancyId() {
        return vacancyId;
    }

    public int getCvId() {
        return cvId;
    }

    public String getStatus() {
        return status;
    }

    public java.sql.Timestamp getAppliedAt() {
        return appliedAt;
    }

    public String getJobSeekerName() {
        return jobSeekerName;
    }

    public String getJobSeekerEmail() {
        return jobSeekerEmail;
    }

    public Cv getCv() {
        return cv;
    }

    public String getVacancyTitle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVacancyTitle'");
    }
}
