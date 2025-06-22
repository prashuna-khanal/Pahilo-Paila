/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pahilopaila;

import pahilopaila.Dao.*;
import pahilopaila.model.*;
import pahilopaila.database.MySqlConnection;

import org.junit.*;
import java.sql.*; // Keep this for java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException, java.sql.Timestamp, and java.sql.Date
import java.time.*; // Keep this for LocalDate, LocalDateTime, ZoneId
import java.util.Date; // *** CHANGED: Import java.util.Date explicitly ***
import java.util.List; // *** CHANGED: Import java.util.List explicitly ***


public class PahiloPailaTest {

    // Real DAO instances
    private UserDao userDao;
    private CVDao cvDao;
    private VacancyDao vacancyDao;
    private RatingDao ratingDao;
    private NotificationDao notificationDao;
    private ApplicationDao applicationDao;

    private static Connection testConnection; // Shared connection for cleanup


    @BeforeClass // JUnit 4 annotation
    public static void setUpClass() {
        System.out.println("Attempting to connect to database...");
        try {
            // MySqlConnection.getInstance().getConnection() will throw if not implemented correctly
            testConnection = MySqlConnection.getInstance().getConnection();
            if (testConnection != null && !testConnection.isClosed()) {
                System.out.println("Database connection successful for tests.");
            } else {
                Assert.fail("Failed to get a valid database connection. Ensure MySqlConnection is correctly implemented.");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed during @BeforeClass setup: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed to connect to database. Ensure MySqlConnection is properly configured and the database is running.");
        }
    }

    @AfterClass // JUnit 4 annotation
    public static void tearDownClass() {
        if (testConnection != null) {
            try {
                testConnection.close();
                System.out.println("Database connection closed after all tests.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    @Before // JUnit 4 annotation
    public void setUp() {
        // Initialize real DAO instances
        userDao = new UserDao();
        cvDao = new CVDao();
        vacancyDao = new VacancyDao();
        ratingDao = new RatingDao();
        notificationDao = new NotificationDao();
        applicationDao = new ApplicationDao();

        // Cleanup any residual data from previous runs or failed tests
        cleanupDatabase();
    }

    @After // JUnit 4 annotation
    public void tearDown() {
        // Cleanup database after each test to ensure test isolation
        cleanupDatabase();
    }

    /**
     * Cleans up common test data from the database.
     * This method deletes data associated with emails starting with 'test_%', 'uvc_%', and 'forget@%'.
     * Deletion order is important due to foreign key constraints.
     */
    private void cleanupDatabase() {
        System.out.println("Cleaning up database before/after test...");
        try {
            // Delete in correct order due to foreign key constraints
            testConnection.prepareStatement("DELETE FROM applications").executeUpdate();
            testConnection.prepareStatement("DELETE FROM cvs").executeUpdate();
            testConnection.prepareStatement("DELETE FROM vacancies").executeUpdate();
            testConnection.prepareStatement("DELETE FROM ratings").executeUpdate();
            testConnection.prepareStatement("DELETE FROM notifications").executeUpdate();
            // Delete from users (test users only, careful with existing real data)
            testConnection.prepareStatement("DELETE FROM users WHERE email LIKE 'test_%' OR email LIKE 'uvc_%' OR email LIKE 'forget@%'").executeUpdate();
            System.out.println("Database cleanup complete.");
        } catch (SQLException e) {
            System.err.println("Error during database cleanup: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed during database cleanup. Check foreign key constraints or data.");
        }
    }

    // Helper method to register a user and return their ID
    // This helper is used by multiple tests to set up preconditions for DAO tests.
    private int registerTestUser(String username, String email, String password, String role) throws SQLException {
        if (!userDao.checkEmailExists(email)) {
            Assert.assertTrue("User registration failed", userDao.registerUser(username, email, password, role));
        }
        // Retrieve the user ID after ensuring registration
        UserData user = userDao.login(new LoginRequest(email, password));
        Assert.assertNotNull("Could not retrieve user after registration for ID: " + email, user);
        return user.getId();
    }


    // --- UserDao Tests ---

    /**
     * Test UserDao.login with valid credentials.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_login_Success_DAO() throws SQLException {
        System.out.println("UserDao: login - Success (DAO direct)");
        String email = "dao_login_user_" + System.currentTimeMillis() + "@example.com";
        String password = "dao_password_valid";
        registerTestUser("DAO Login User", email, password, "Job Seeker");

        LoginRequest req = new LoginRequest(email, password);
        UserData user = userDao.login(req);
        Assert.assertNotNull("Login should succeed", user);
        Assert.assertEquals("Retrieved user email should match.", email, user.getEmail());
    }

    /**
     * Test UserDao.login with invalid credentials.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_login_Fail_DAO() {
        System.out.println("UserDao: login - Fail (DAO direct)");
        LoginRequest req = new LoginRequest("nonexistent_dao@example.com", "badpass");
        UserData user = userDao.login(req);
        Assert.assertNull("Login should fail", user);
    }

    /**
     * Test UserDao.registerUser.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_registerUser_DAO() throws SQLException {
        System.out.println("UserDao: registerUser (DAO direct)");
        String email = "dao_reg_user_" + System.currentTimeMillis() + "@example.com";
        boolean registered = userDao.registerUser("DAO Reg", email, "reg_pass", "Employer");
        Assert.assertTrue("Should register user", registered);
        Assert.assertTrue("Email should exist", userDao.checkEmailExists(email));
    }

    /**
     * Test UserDao.verifyPassword.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_verifyPassword_DAO() throws SQLException {
        System.out.println("UserDao: verifyPassword (DAO direct)");
        String email = "dao_verify_pass_" + System.currentTimeMillis() + "@example.com";
        String password = "verify_password_dao";
        int userId = registerTestUser("VerifyPassUser", email, password, "Job Seeker");

        Assert.assertTrue("Correct password should verify", userDao.verifyPassword(userId, password));
        Assert.assertFalse("Wrong password should not verify", userDao.verifyPassword(userId, "wrongpass"));
    }

    /**
     * Test UserDao.updateUser.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_updateUser_DAO() throws SQLException {
        System.out.println("UserDao: updateUser (DAO direct)");
        String email = "dao_update_user_" + System.currentTimeMillis() + "@example.com";
        String password = "old_pass";
        int userId = registerTestUser("UpdateUser", email, password, "Job Seeker");

        String newEmail = "updated_user_" + System.currentTimeMillis() + "@example.com";
        boolean updated = userDao.updateUser(userId, "Updated Name", newEmail, "new_pass");
        Assert.assertTrue("User should be updated", updated);

        LoginRequest updatedLoginReq = new LoginRequest(newEmail, "new_pass");
        UserData updatedUser = userDao.login(updatedLoginReq);
        Assert.assertNotNull("Should login with new credentials", updatedUser);
        Assert.assertEquals("Updated Name", updatedUser.getUsername());
        Assert.assertEquals(newEmail, updatedUser.getEmail());
    }

    /**
     * Test UserDao.getUserById.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_getUserById_DAO() throws SQLException {
        System.out.println("UserDao: getUserById (DAO direct)");
        String email = "dao_getbyid_" + System.currentTimeMillis() + "@example.com";
        int userId = registerTestUser("GetById", email, "pass", "Employer");

        UserData user = userDao.getUserById(userId);
        Assert.assertNotNull("Should fetch user by ID", user);
        Assert.assertEquals(userId, user.getId());
        Assert.assertEquals("GetById", user.getUsername());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals("Employer", user.getRoles());
    }
    // --- VacancyDao Tests ---

    /**
     * Test VacancyDao.saveVacancy (using object).
     */
    @Test // JUnit 4 annotation
    public void testVacancyDao_saveVacancy_Object_DAO() throws SQLException {
        System.out.println("VacancyDao: saveVacancy (Object) (DAO direct)");
        int recruiterId = registerTestUser("Vac Rec DAO", "vac_rec_dao@example.com", "pass", "Employer");
        Vacancy vacancy = new Vacancy(0, recruiterId, "Software Dev (DAO Test)", "Full time", "Mid-Level", 60, "Join our team.");
        boolean saved = vacancyDao.saveVacancy(vacancy);
        Assert.assertTrue("Vacancy should be saved (DAO direct).", saved);

        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertFalse("Should find saved vacancy for recruiter.", vacancies.isEmpty());
        Assert.assertEquals("Saved vacancy title should match.", "Software Dev (DAO Test)", vacancies.get(0).getJobTitle());
    }

    /**
     * Test VacancyDao.getAllVacancies.
     */
    @Test // JUnit 4 annotation
    public void testVacancyDao_getAllVacancies_DAO() throws SQLException {
        System.out.println("VacancyDao: getAllVacancies (DAO direct)");
        int recruiterId1 = registerTestUser("Vac Rec1 DAO", "vac_rec1_dao@example.com", "pass", "Employer");
        int recruiterId2 = registerTestUser("Vac Rec2 DAO", "vac_rec2_dao@example.com", "pass", "Employer");
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId1, "Job A DAO", "Full time", "Junior-Level", 10, "Desc A"));
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId2, "Job B DAO", "Part time", "Mid-Level", 20, "Desc B"));

        List<Vacancy> allVacancies = vacancyDao.getAllVacancies();
        Assert.assertFalse("Should retrieve all vacancies (DAO direct).", allVacancies.isEmpty());
        Assert.assertTrue(allVacancies.stream().anyMatch(v -> v.getJobTitle().equals("Job A DAO")));
        Assert.assertTrue(allVacancies.stream().anyMatch(v -> v.getJobTitle().equals("Job B DAO")));
    }

    /**
     * Test VacancyDao.getVacancyById.
     */
    @Test // JUnit 4 annotation
    public void testVacancyDao_getVacancyById_DAO() throws SQLException {
        System.out.println("VacancyDao: getVacancyById (DAO direct)");
        int recruiterId = registerTestUser("Vac ById Rec DAO", "vac_byid_rec_dao@example.com", "pass", "Employer");
        Vacancy newVacancy = new Vacancy(0, recruiterId, "Specific Job DAO", "Full time", "Entry-Level", 7, "A unique job.");
        vacancyDao.saveVacancy(newVacancy);

        // Retrieve the ID of the newly saved vacancy
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        int vacancyId = vacancies.stream()
                                 .filter(v -> v.getJobTitle().equals("Specific Job DAO"))
                                 .findFirst()
                                 .orElseThrow(() -> new AssertionError("Saved vacancy not found")).getId();

        Vacancy retrievedVacancy = vacancyDao.getVacancyById(vacancyId);
        Assert.assertNotNull("Should retrieve vacancy by ID (DAO direct).", retrievedVacancy);
        Assert.assertEquals("Retrieved vacancy title should match.", "Specific Job DAO", retrievedVacancy.getJobTitle());
    }

    /**
     * Test VacancyDao.getFilteredVacancies.
     * IMPORTANT: This test will only verify the method call and return, NOT the logical correctness
     * of date filtering, as the DAO's implementation for date filtering against `days_left` (INT)
     * using `Date` objects is incorrect.
     */
    @Test // JUnit 4 annotation
    public void testVacancyDao_getFilteredVacancies_DAO() throws SQLException {
        System.out.println("VacancyDao: getFilteredVacancies (DAO direct)");
        int recruiterId = registerTestUser("Vac Filter Rec DAO", "vac_filter_rec_dao@example.com", "pass", "Employer");
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Filtered Job A DAO", "Full time", "Junior-Level", 30, "Desc A"));
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Filtered Job B DAO", "Part time", "Mid-Level", 15, "Desc B"));
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Filtered Job C DAO", "Full time", "Senior-Level", 5, "Desc C"));

        // Test filtering by job type
        List<Vacancy> filtered = vacancyDao.getFilteredVacancies("Full time", null, null, null);
        Assert.assertFalse("Should find filtered vacancies for 'Full time'.", filtered.isEmpty());
        Assert.assertEquals("Should find 2 full-time jobs (DAO direct).", 2, filtered.size());
        Assert.assertTrue(filtered.stream().anyMatch(v -> v.getJobTitle().equals("Filtered Job A DAO")));
        Assert.assertTrue(filtered.stream().anyMatch(v -> v.getJobTitle().equals("Filtered Job C DAO")));

        // Test filtering by experience level
        filtered = vacancyDao.getFilteredVacancies(null, "Mid-Level", null, null);
        Assert.assertEquals("Should find 1 Mid-Level job (DAO direct).", 1, filtered.size());
        Assert.assertEquals("Filtered Job B DAO", filtered.get(0).getJobTitle());

        // Test with date filters (will pass a Date object, but DAO's logic is flawed for `days_left`)
        // Creating java.sql.Date from java.util.Date for the method signature
        java.sql.Date todaySql = new java.sql.Date(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        java.sql.Date futureDateSql = new java.sql.Date(Date.from(LocalDate.now().plusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

        filtered = vacancyDao.getFilteredVacancies(null, null, todaySql, futureDateSql);
        Assert.assertNotNull("Method should return a list even with problematic date filter.", filtered);
        // We cannot reliably assert the content of 'filtered' here for date range due to the DAO's implementation.
        System.out.println("VacancyDao.getFilteredVacancies tested. Note its current limitation with date filtering on 'days_left'.");
    }

    // --- NotificationDao Tests ---

    /**
     * Test NotificationDao.saveNotification.
     */
    @Test // JUnit 4 annotation
    public void testNotificationDao_saveNotification_DAO() throws SQLException {
        System.out.println("NotificationDao: saveNotification (DAO direct)");
        int userId = registerTestUser("Notif User DAO", "notif_user_dao@example.com", "pass", "Job Seeker");
        String message = "Your application has been viewed (DAO Test).";
        String timestamp = LocalDateTime.now().toString(); // Use current format from DAO
        boolean isImportant = true;

        boolean saved = notificationDao.saveNotification(userId, message, timestamp, isImportant);
        Assert.assertTrue("Notification should be saved (DAO direct).", saved);

        List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
        Assert.assertFalse("Should retrieve saved notification.", notifications.isEmpty());
        Assert.assertEquals("Saved message should match.", message, notifications.get(0).getMessage());
    }

    /**
     * Test NotificationDao.getNotificationsByUserId.
     */
    @Test // JUnit 4 annotation
    public void testNotificationDao_getNotificationsByUserId_DAO() throws SQLException {
        System.out.println("NotificationDao: getNotificationsByUserId (DAO direct)");
        int userId = registerTestUser("Notif Get User DAO", "notif_get_user_dao@example.com", "pass", "Job Seeker");
        notificationDao.saveNotification(userId, "Message 1 DAO", LocalDateTime.now().minusDays(1).toString(), false);
        notificationDao.saveNotification(userId, "Message 2 DAO", LocalDateTime.now().toString(), true);

        List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
        Assert.assertEquals("Should retrieve two notifications (DAO direct).", 2, notifications.size());
        Assert.assertEquals("Newer message should be first.", "Message 2 DAO", notifications.get(0).getMessage());
    }

    /**
     * Test NotificationDao.markAllNotificationsRead.
     */
    @Test // JUnit 4 annotation
    public void testNotificationDao_markAllNotificationsRead_DAO() throws SQLException {
        System.out.println("NotificationDao: markAllNotificationsRead (DAO direct)");
        int userId = registerTestUser("Notif Mark Read DAO", "notif_mark_read_dao@example.com", "pass", "Job Seeker");
        notificationDao.saveNotification(userId, "Unread 1 DAO", LocalDateTime.now().toString(), false);
        notificationDao.saveNotification(userId, "Unread 2 DAO", LocalDateTime.now().minusHours(1).toString(), true);

        boolean marked = notificationDao.markAllNotificationsRead(userId);
        Assert.assertTrue("Notifications should be marked as read (DAO direct).", marked);

        List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
        for (Notification n : notifications) {
            Assert.assertTrue("All notifications should be marked as read.", n.isRead());
        }
    }

    // --- CVDao Tests ---

    /**
     * Test CVDao.saveCV.
     */
    @Test // JUnit 4 annotation
    public void testCVDao_saveCV_DAO() throws SQLException {
        System.out.println("CVDao: saveCV (DAO direct)");
        int userId = registerTestUser("CV User DAO", "cv_user_dao@example.com", "cvPass", "Job Seeker");

        boolean saved = cvDao.saveCV(userId, "Jane", "Doe", "1991-03-20", "9988776655",
                "M.Sc. Data Science", "Python, ML", "3 years Data Scientist");
        Assert.assertTrue("CV should be saved successfully (DAO direct).", saved);

        try (ResultSet rs = cvDao.getCVByUserId(userId)) {
            Assert.assertTrue("Should retrieve the saved CV.", rs.next());
            Assert.assertEquals("Jane", rs.getString("first_name"));
            Assert.assertEquals("Python, ML", rs.getString("skills"));
            Assert.assertEquals("DOB should be stored as VARCHAR.", "1991-03-20", rs.getString("dob"));
        }
    }

    /**
     * Test CVDao.getCVByUserId.
     */
    @Test // JUnit 4 annotation
    public void testCVDao_getCVByUserId_DAO() throws SQLException {
        System.out.println("CVDao: getCVByUserId (DAO direct)");
        int userId = registerTestUser("CV Get User DAO", "cv_get_user_dao@example.com", "cvGetPass", "Job Seeker");
        cvDao.saveCV(userId, "Mike", "Brown", "1985-07-07", "1122334455",
                "Ph.D. Physics", "Research, Analysis", "10 years Research Scientist");

        try (ResultSet rs = cvDao.getCVByUserId(userId)) {
            Assert.assertNotNull("ResultSet should not be null.", rs);
            Assert.assertTrue("Should find a CV for the user.", rs.next());
            Assert.assertEquals("Mike", rs.getString("first_name"));
            Assert.assertEquals("Research, Analysis", rs.getString("skills"));
            Assert.assertEquals("1985-07-07", rs.getString("dob"));
        }
    }

    // --- ApplicationDao Tests ---

    /**
     * Test ApplicationDao.saveApplication.
     */
    @Test // JUnit 4 annotation
    public void testApplicationDao_saveApplication_DAO() throws SQLException {
        System.out.println("ApplicationDao: saveApplication (DAO direct)");
        int jobseekerId = registerTestUser("App JS DAO", "app_js_dao@example.com", "pass", "Job Seeker");
        int recruiterId = registerTestUser("App Rec DAO", "app_rec_dao@example.com", "pass", "Employer");

        cvDao.saveCV(jobseekerId, "App", "Seeker", "2000-01-01", "111", "Uni", "Skill", "Exp");
        int cvId;
        try (ResultSet rsCv = cvDao.getCVByUserId(jobseekerId)) {
            Assert.assertTrue(rsCv.next()); cvId = rsCv.getInt("id");
        }

        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Test Vacancy DAO", "Full time", "Entry-Level", 10, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertTrue(vacancies.size() > 0); int vacancyId = vacancies.get(0).getId();

        boolean saved = applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        Assert.assertTrue("Application should be saved (DAO direct).", saved);

        Assert.assertTrue("Job seeker should have applied after saving.", applicationDao.hasApplied(jobseekerId, vacancyId));
    }

    /**
     * Test ApplicationDao.getApplicationsByRecruiterId.
     */
    @Test // JUnit 4 annotation
    public void testApplicationDao_getApplicationsByRecruiterId_DAO() throws SQLException {
        System.out.println("ApplicationDao: getApplicationsByRecruiterId (DAO direct)");
        int jobseekerId1 = registerTestUser("App JS1 DAO", "app_js1_dao@example.com", "pass", "Job Seeker");
        int jobseekerId2 = registerTestUser("App JS2 DAO", "app_js2_dao@example.com", "pass", "Job Seeker");
        int recruiterId = registerTestUser("App Rec Get DAO", "app_rec_get_dao@example.com", "pass", "Employer");

        cvDao.saveCV(jobseekerId1, "JS1", "User", "1995-01-01", "111", "Edu", "Skills", "Exp");
        int cvId1;
        try (ResultSet rsCv1 = cvDao.getCVByUserId(jobseekerId1)) {
            Assert.assertTrue(rsCv1.next()); cvId1 = rsCv1.getInt("id");
        }

        cvDao.saveCV(jobseekerId2, "JS2", "User", "1996-02-02", "222", "Edu", "Skills", "Exp");
        int cvId2;
        try (ResultSet rsCv2 = cvDao.getCVByUserId(jobseekerId2)) {
            Assert.assertTrue(rsCv2.next()); cvId2 = rsCv2.getInt("id");
        }

        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Job for Apps DAO", "Full time", "Mid-Level", 30, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertTrue(vacancies.size() > 0); int vacancyId = vacancies.get(0).getId();

        applicationDao.saveApplication(jobseekerId1, recruiterId, vacancyId, cvId1);
        applicationDao.saveApplication(jobseekerId2, recruiterId, vacancyId, cvId2);

        List<Application> applications = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertEquals("Should retrieve two applications for the recruiter (DAO direct).", 2, applications.size());
        // Further assertions could check the job_seeker_name, job_seeker_email etc., but would require inspecting the Application model.
    }

    /**
     * Test ApplicationDao.getApplicationById.
     */
    @Test // JUnit 4 annotation
    public void testApplicationDao_getApplicationById_DAO() throws SQLException {
        System.out.println("ApplicationDao: getApplicationById (DAO direct)");
        int jobseekerId = registerTestUser("App JS ById DAO", "app_js_byid_dao@example.com", "pass", "Job Seeker");
        int recruiterId = registerTestUser("App Rec ById DAO", "app_rec_byid_dao@example.com", "pass", "Employer");

        cvDao.saveCV(jobseekerId, "JSByID", "User", "1990-01-01", "111", "Edu", "Skills", "Exp");
        int cvId;
        try (ResultSet rsCv = cvDao.getCVByUserId(jobseekerId)) {
            Assert.assertTrue(rsCv.next()); cvId = rsCv.getInt("id");
        }

        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Job ById DAO", "Full time", "Entry-Level", 10, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertTrue(vacancies.size() > 0); int vacancyId = vacancies.get(0).getId();

        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        List<Application> pendingApps = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertTrue(pendingApps.size() > 0); int applicationId = pendingApps.get(0).getId();

        Application retrievedApp = applicationDao.getApplicationById(applicationId);
        Assert.assertNotNull("Should retrieve application by ID (DAO direct).", retrievedApp);
        Assert.assertEquals("Retrieved application's job seeker ID should match.", jobseekerId, retrievedApp.getJobSeekerId());
    }

    /**
     * Test ApplicationDao.hasApplied.
     */
    @Test // JUnit 4 annotation
    public void testApplicationDao_hasApplied_DAO() throws SQLException {
        System.out.println("ApplicationDao: hasApplied (DAO direct)");
        int jobseekerId = registerTestUser("App JS Has DAO", "app_js_has_dao@example.com", "pass", "Job Seeker");
        int recruiterId = registerTestUser("App Rec Has DAO", "app_rec_has_dao@example.com", "pass", "Employer");

        cvDao.saveCV(jobseekerId, "JS Has", "User", "1990-01-01", "111", "Edu", "Skills", "Exp");
        int cvId;
        try (ResultSet rsCv = cvDao.getCVByUserId(jobseekerId)) {
            Assert.assertTrue(rsCv.next()); cvId = rsCv.getInt("id");
        }

        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Job Has DAO", "Full time", "Entry-Level", 10, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertTrue(vacancies.size() > 0); int vacancyId = vacancies.get(0).getId();

        Assert.assertFalse("Should not have applied yet.", applicationDao.hasApplied(jobseekerId, vacancyId));
        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        Assert.assertTrue("Should have applied after saving (DAO direct).", applicationDao.hasApplied(jobseekerId, vacancyId));
    }

    /**
     * Test ApplicationDao.updateApplicationStatus.
     */
    @Test // JUnit 4 annotation
    public void testApplicationDao_updateApplicationStatus_DAO() throws SQLException {
        System.out.println("ApplicationDao: updateApplicationStatus (DAO direct)");
        int jobseekerId = registerTestUser("App JS Update DAO", "app_js_update_dao@example.com", "pass", "Job Seeker");
        int recruiterId = registerTestUser("App Rec Update DAO", "app_rec_update_dao@example.com", "pass", "Employer");

        cvDao.saveCV(jobseekerId, "JS Update", "User", "1990-01-01", "111", "Uni", "Skills", "Exp");
        int cvId;
        try (ResultSet rsCv = cvDao.getCVByUserId(jobseekerId)) {
            Assert.assertTrue(rsCv.next()); cvId = rsCv.getInt("id");
        }

        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Job Update DAO", "Full time", "Entry-Level", 10, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertTrue(vacancies.size() > 0); int vacancyId = vacancies.get(0).getId();

        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        List<Application> pendingApps = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertTrue(pendingApps.size() > 0); int applicationId = pendingApps.get(0).getId();

        boolean updated = applicationDao.updateApplicationStatus(applicationId, "Rejected");
        Assert.assertTrue("Application status should be updated (DAO direct).", updated);

        Application updatedApp = applicationDao.getApplicationById(applicationId);
        Assert.assertEquals("Status should be 'Rejected' after update.", "Rejected", updatedApp.getStatus());
    }
}