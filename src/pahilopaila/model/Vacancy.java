package pahilopaila.model;

import java.sql.Date;

public class Vacancy {
    private int id;
    private int recruiterId;
    private String jobTitle;
    private String jobType;
    private String experienceLevel;
    private int daysLeft;
    private String description;

    // Default constructor
    public Vacancy() {
    }

    // Parameterized constructor
    public Vacancy(int id, int recruiterId, String jobTitle, String jobType, String experienceLevel, int daysLeft, String description) {
        this.id = id;
        this.recruiterId = recruiterId;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.daysLeft = daysLeft;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Optional: toString for debugging
    @Override
    public String toString() {
        return "Vacancy{" +
               "id=" + id +
               ", recruiterId=" + recruiterId +
               ", jobTitle='" + jobTitle + '\'' +
               ", jobType='" + jobType + '\'' +
               ", experienceLevel='" + experienceLevel + '\'' +
               ", daysLeft=" + daysLeft +
               ", description='" + description + '\'' +
               '}';
    }

    public void setDeadline(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public void setEndDate(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}