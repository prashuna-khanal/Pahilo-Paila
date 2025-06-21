package pahilopaila.model;

import java.sql.Timestamp;

public class Cv {
    private int id;
    private String userId;
    private String firstName;
    private String lastName;
    private String dob;
    private String contact;
    private String education;
    private String skills;
    private String experience;


    public Cv(int id, String userId, String firstName, String lastName, String dob, String contact,
              String education, String skills, String experience) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.contact = contact;
        this.education = education;
        this.skills = skills;
        this.experience = experience;
    }

    public int getId() { return id; }
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDob() { return dob; }
    public String getContact() { return contact; }
    public String getEducation() { return education; }
    public String getSkills() { return skills; }
    public String getExperience() { return experience;}
}