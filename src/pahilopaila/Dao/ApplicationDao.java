/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package pahilopaila.Dao;
import pahilopaila.database.MySqlConnection;
import pahilopaila.model.Application;
import pahilopaila.model.Cv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
*
* @author LENOVO
*/
public class ApplicationDao {
    private final Connection connection;

    public ApplicationDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("ApplicationDao initialized");
    }

    public boolean saveApplication(int jobSeekerId, int recruiterId, int vacancyId, int cvId) {
        String sql = "INSERT INTO applications (job_seeker_id, recruiter_id, vacancy_id, cv_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobSeekerId);
            stmt.setInt(2, recruiterId);
            stmt.setInt(3, vacancyId);
            stmt.setInt(4, cvId);
            int rows = stmt.executeUpdate();
            System.out.println("Application saved, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error saving application: " + e.getMessage());
            return false;
        }
    }

    public List<Application> getApplicationsByRecruiterId(int recruiterId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS job_seeker_name, u.email AS job_seeker_email, " +
                     "c.first_name, c.last_name, c.dob, c.contact, c.education, c.skills, c.experience, v.job_title " +
                     "FROM applications a " +
                     "JOIN users u ON a.job_seeker_id = u.id " +
                     "JOIN cvs c ON a.cv_id = c.id " +
                     "JOIN vacancies v ON a.vacancy_id = v.id " +
                     "WHERE a.recruiter_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recruiterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cv cv = new Cv(
                    rs.getInt("cv_id"),
                    String.valueOf(rs.getInt("job_seeker_id")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("contact"),
                    rs.getString("education"),
                    rs.getString("skills"),
                    rs.getString("experience")
                );
                Application app = new Application(
                    rs.getInt("id"),
                    rs.getInt("job_seeker_id"),
                    rs.getInt("recruiter_id"),
                    rs.getInt("vacancy_id"),
                    rs.getInt("cv_id"),
                    rs.getString("status"),
                    rs.getTimestamp("applied_at"),
                    rs.getString("job_seeker_name"),
                    rs.getString("job_seeker_email"),
                    cv
                );
                applications.add(app);
            }
            System.out.println("Retrieved " + applications.size() + " applications for recruiter_id: " + recruiterId);
            return applications;
        } catch (SQLException e) {
            System.err.println("Error retrieving applications: " + e.getMessage());
            return applications;
        }
    }

    public Application getApplicationById(int applicationId) {
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS job_seeker_name, u.email AS job_seeker_email, " +
                     "c.first_name, c.last_name, c.dob, c.contact, c.education, c.skills, c.experience, v.job_title " +
                     "FROM applications a " +
                     "JOIN users u ON a.job_seeker_id = u.id " +
                     "JOIN cvs c ON a.cv_id = c.id " +
                     "JOIN vacancies v ON a.vacancy_id = v.id " +
                     "WHERE a.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cv cv = new Cv(
                    rs.getInt("cv_id"),
                    String.valueOf(rs.getInt("job_seeker_id")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("contact"),
                    rs.getString("education"),
                    rs.getString("skills"),
                    rs.getString("experience")
                );
                Application app = new Application(
                    rs.getInt("id"),
                    rs.getInt("job_seeker_id"),
                    rs.getInt("recruiter_id"),
                    rs.getInt("vacancy_id"),
                    rs.getInt("cv_id"),
                    rs.getString("status"),
                    rs.getTimestamp("applied_at"),
                    rs.getString("job_seeker_name"),
                    rs.getString("job_seeker_email"),
                    cv
                );
                rs.close();
                return app;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving application: " + e.getMessage());
        }
        return null;
    }

    public boolean hasApplied(int jobSeekerId, int vacancyId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE job_seeker_id = ? AND vacancy_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jobSeekerId);
            stmt.setInt(2, vacancyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean hasApplied = rs.getInt(1) > 0;
                rs.close();
                return hasApplied;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error checking application: " + e.getMessage());
        }
        return false;
    }

    public boolean updateApplicationStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, applicationId);
            int rows = stmt.executeUpdate();
            System.out.println("Application status updated, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return false;
        }
    }
}