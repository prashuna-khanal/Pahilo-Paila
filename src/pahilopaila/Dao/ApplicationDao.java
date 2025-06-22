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
 * Data Access Object (DAO) class for managing Application entities in the database.
 * Handles CRUD operations and queries related to job applications.
 *
 * @author LENOVO
 */
public class ApplicationDao {
    // Database connection instance
    private final Connection connection;

    /**
     * Constructor that initializes the database connection using MySqlConnection singleton.
     */
    public ApplicationDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("ApplicationDao initialized");
    }

    /**
     * Saves a new application to the database.
     *
     * @param jobSeekerId ID of the job seeker
     * @param recruiterId ID of the recruiter
     * @param vacancyId   ID of the job vacancy
     * @param cvId        ID of the CV associated with the application
     * @return true if the application is saved successfully, false otherwise
     */
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

    /**
     * Retrieves a list of applications for a specific recruiter.
     *
     * @param recruiterId ID of the recruiter
     * @return List of Application objects with associated job seeker and CV details
     */
    public List<Application> getApplicationsByRecruiterId(int recruiterId) {
        List<Application> applications = new ArrayList<>();
        // SQL query to fetch applications with related job seeker, CV, and vacancy details
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
                // Create a Cv object from the result set
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
                // Create an Application object with associated CV
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

    /**
     * Retrieves a single application by its ID.
     *
     * @param applicationId ID of the application
     * @return Application object with associated job seeker and CV details, or null if not found
     */
    public Application getApplicationById(int applicationId) {
        // SQL query to fetch a specific application with related details
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
                // Create a Cv object from the result set
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
                // Create an Application object with associated CV
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

    /**
     * Checks if a job seeker has already applied for a specific vacancy.
     *
     * @param jobSeekerId ID of the job seeker
     * @param vacancyId   ID of the vacancy
     * @return true if the job seeker has applied, false otherwise
     */
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

    /**
     * Updates the status of an application.
     *
     * @param applicationId ID of the application
     * @param status       New status to set
     * @return true if the status is updated successfully, false otherwise
     */
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