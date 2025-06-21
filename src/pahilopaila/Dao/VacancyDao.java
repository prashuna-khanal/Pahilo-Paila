package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection;
import pahilopaila.model.Vacancy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacancyDao {
    private final Connection connection;

    public VacancyDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("VacancyDao initialized");
    }

    public boolean saveVacancy(int recruiterId, String jobTitle, String jobType, String experienceLevel, int daysLeft, String description) {
        String sql = "INSERT INTO vacancies (recruiter_id, job_title, job_type, experience_level, days_left, description) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("Saving vacancy for recruiter_id: " + recruiterId);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, recruiterId);
            pstmt.setString(2, jobTitle);
            pstmt.setString(3, jobType);
            pstmt.setString(4, experienceLevel);
            pstmt.setInt(5, daysLeft);
            pstmt.setString(6, description);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Vacancy saved successfully, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving vacancy: " + e.getMessage());
            return false;
        }
    }

    public boolean saveVacancy(Vacancy vacancy) {
        String sql = "INSERT INTO vacancies (recruiter_id, job_title, job_type, experience_level, days_left, description) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("Saving vacancy for recruiter_id: " + vacancy.getRecruiterId());
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, vacancy.getRecruiterId());
            pstmt.setString(2, vacancy.getJobTitle());
            pstmt.setString(3, vacancy.getJobType());
            pstmt.setString(4, vacancy.getExperienceLevel());
            pstmt.setInt(5, vacancy.getDaysLeft());
            pstmt.setString(6, vacancy.getDescription());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Vacancy saved successfully, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving vacancy: " + e.getMessage());
            return false;
        }
    }

    public List<Vacancy> getVacanciesByRecruiterId(int recruiterId) {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT * FROM vacancies WHERE recruiter_id = ?";
        System.out.println("Retrieving vacancies for recruiter_id: " + recruiterId);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, recruiterId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vacancy vacancy = new Vacancy(
                    rs.getInt("id"),
                    rs.getInt("recruiter_id"),
                    rs.getString("job_title"),
                    rs.getString("job_type"),
                    rs.getString("experience_level"),
                    rs.getInt("days_left"),
                    rs.getString("description")
                );
                vacancies.add(vacancy);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving vacancies: " + e.getMessage());
        }
        return vacancies;
    }

    public List<Vacancy> getAllVacancies() {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT * FROM vacancies";
        System.out.println("Retrieving all vacancies");
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vacancy vacancy = new Vacancy(
                    rs.getInt("id"),
                    rs.getInt("recruiter_id"),
                    rs.getString("job_title"),
                    rs.getString("job_type"),
                    rs.getString("experience_level"),
                    rs.getInt("days_left"),
                    rs.getString("description")
                );
                vacancies.add(vacancy);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all vacancies: " + e.getMessage());
        }
        return vacancies;
    }

    public Vacancy getVacancyById(int vacancyId) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";
        System.out.println("Retrieving vacancy for id: " + vacancyId);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, vacancyId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Vacancy vacancy = new Vacancy(
                    rs.getInt("id"),
                    rs.getInt("recruiter_id"),
                    rs.getString("job_title"),
                    rs.getString("job_type"),
                    rs.getString("experience_level"),
                    rs.getInt("days_left"),
                    rs.getString("description")
                );
                rs.close();
                return vacancy;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving vacancy: " + e.getMessage());
        }
        return null;
    }
    public List<Vacancy> getFilteredVacancies(String jobType, String experienceLevel, java.util.Date startDate, java.util.Date endDate) {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT * FROM vacancies WHERE 1=1";
        if (jobType != null && !jobType.isEmpty()) sql += " AND job_type = ?";
        if (experienceLevel != null && !experienceLevel.isEmpty()) sql += " AND experience_level = ?";
        if (startDate != null || endDate != null) {
            long now = System.currentTimeMillis();
            if (startDate != null) {
                long startDays = (startDate.getTime() - now) / (1000 * 60 * 60 * 24);
                sql += " AND days_left >= ?";
            }
            if (endDate != null) {
                long endDays = (endDate.getTime() - now) / (1000 * 60 * 60 * 24);
                sql += " AND days_left <= ?";
            }
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int index = 1;
            if (jobType != null && !jobType.isEmpty()) pstmt.setString(index++, jobType);
            if (experienceLevel != null && !experienceLevel.isEmpty()) pstmt.setString(index++, experienceLevel);
            if (startDate != null) {
                long startDays = (startDate.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                pstmt.setInt(index++, (int) startDays);
            }
            if (endDate != null) {
                long endDays = (endDate.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                pstmt.setInt(index++, (int) endDays);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vacancy vacancy = new Vacancy();
                vacancy.setId(rs.getInt("id"));
                vacancy.setJobTitle(rs.getString("job_title"));
                vacancy.setJobType(rs.getString("job_type"));
                vacancy.setExperienceLevel(rs.getString("experience_level"));
                vacancy.setDaysLeft(rs.getInt("days_left"));
                vacancies.add(vacancy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error in getFilteredVacancies: " + e.getMessage());
        }
        return vacancies;
    }
    }