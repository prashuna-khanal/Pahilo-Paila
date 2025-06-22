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

 


    public List<Vacancy> getFilteredVacancies(String jobType, String experienceLevel, String endDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/pahilopaila";
        private static final String USER = "root";
        private static final String PASSWORD = "Pahilopaila@123";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
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
            System.out.println("Vacancy returned successfully, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving vacancy: " + e.getMessage());
            return false;
        }
    }
 
    public List<Vacancy> getVacanciesByRecruiterId(int applicantId) {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT * FROM vacancies WHERE recruiter_id = ?";
        System.out.println("Retrieving vacancies for recruiter_id: " + applicantId);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, applicantId);
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

 
    public List<Vacancy> getFilteredVacancies(String jobType, String experienceLevel, Date startDate, Date endDate) {
    List<Vacancy> vacancies = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM vacancies WHERE 1=1");
    List<Object> params = new ArrayList<>();
 
    if (jobType != null && !jobType.isEmpty()) {
        jobType = jobType.equals("Full-time") ? "Full time" : jobType.equals("Part-time") ? "Part time" : jobType;
        sql.append(" AND job_type = ?");
        params.add(jobType);
    }
    if (experienceLevel != null && !experienceLevel.isEmpty()) {
        experienceLevel = experienceLevel.equals("Mid") ? "Mid-Level" :
                         experienceLevel.equals("Junior") ? "Junior-Level" :
                         experienceLevel.equals("Senior") ? "Senior-Level" : experienceLevel;
        sql.append(" AND experience_level = ?");
        params.add(experienceLevel);
    }
    // Date filtering will be fixed in the next section
    if (startDate != null) {
        sql.append(" AND days_left >= ?");
        params.add(startDate.getTime());
    }
    if (endDate != null) {
        sql.append(" AND days_left <= ?");
        params.add(endDate.getTime());
    }
 
    System.out.println("Executing filtered vacancies query: " + sql.toString() + " with params: " + params);
 
    try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }
        try (ResultSet rs = pstmt.executeQuery()) {
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
        }
        System.out.println("Retrieved " + vacancies.size() + " filtered vacancies");
    } catch (SQLException e) {
        System.err.println("SQL Error in getFilteredVacancies: " + e.getMessage());
        e.printStackTrace();
    }
    return vacancies;
}
}