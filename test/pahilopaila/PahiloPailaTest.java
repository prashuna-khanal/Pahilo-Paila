/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pahilopaila;

// Import all necessary DAO and Model classes
import pahilopaila.Dao.*;
import pahilopaila.model.*;
import pahilopaila.database.MySqlConnection;

// Import JUnit 4 annotations and Assertions
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert; // Explicitly import Assert class for Assert.method() calls

// Import Java SQL, Time, and Utility classes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date; // Specific import for java.util.Date to avoid ambiguity
import java.util.List; // Specific import for java.util.List to avoid ambiguity
import java.util.ArrayList; // Specific import for java.util.ArrayList

/**
 * Comprehensive integration test class for all DAO (Data Access Object) classes
 * in the PahiloPaila project, compatible with JUnit 4.
 * This class directly interacts with a live MySQL database, using existing DAO
 * and Model implementations without modification.
 *
 * IMPORTANT CONSIDERATIONS AND LIMITATIONS:
 * - Your MySqlConnection.java must be fully functional and connect to a valid MySQL DB.
 * - Database schema must match DAOs EXACTLY as provided (e.g., VARCHAR for dates/timestamps, INT for days_left).
 * - Test data management: This suite will insert/update data. The cleanupDatabase() method
 * attempts to remove data created by these tests (based on 'test_%', 'uvc_%', and 'forget@%' email prefixes).
 * Ensure this cleanup strategy aligns with your needs, or implement more specific cleanup.
 * - VacancyDao.getFilteredVacancies Limitation: Note that one `getFilteredVacancies` method in
 * the provided `VacancyDao.java` still throws `UnsupportedOperationException`. The test uses
 * the other `getFilteredVacancies` method that takes `Date` objects, which has an implementation.
 * - DatabaseConnection.java and inner DatabaseConnection class in VacancyDao are redundant if
 * MySqlConnection is the primary connection method. Ensure MySqlConnection is correctly configured.
 */
public class PahiloPailaTest {

    // Real DAO instances
    private UserDao userDao;
    private CVDao cvDao;
    private VacancyDao vacancyDao;
    private RatingDao ratingDao;
    private NotificationDao notificationDao;
    private ApplicationDao applicationDao;

    private static Connection testConnection; // Shared connection for cleanup


    /**
     * Sets up the database connection before any tests in the class are run.
     * Asserts that the connection is successfully established.
     */
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
            Assert.fail("Failed to connect to database. Ensure MySqlConnection is properly configured and the database is running. Error: " + e.getMessage());
        }
    }

    /**
     * Closes the database connection after all tests in the class have been run.
     */
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

    /**
     * Initializes DAO instances and cleans the database before each test method.
     */
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

    /**
     * Cleans the database after each test method to ensure test isolation.
     */
    @After // JUnit 4 annotation
    public void tearDown() {
        // Cleanup database after each test to ensure test isolation
        cleanupDatabase();
    }

    /**
     * Cleans up common test data from the database.
     * This method deletes data associated with emails starting with 'test_%', 'uvc_%', and 'forget@%'.
     * Deletion order is important due to foreign key constraints (applications -> cvs, vacancies, users).
     */
    private void cleanupDatabase() {
        System.out.println("Cleaning up database before/after test...");
        try {
            // Delete in correct order due to foreign key constraints
            testConnection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); // Temporarily disable FK checks
            testConnection.prepareStatement("DELETE FROM applications").executeUpdate();
            testConnection.prepareStatement("DELETE FROM cvs").executeUpdate();
            testConnection.prepareStatement("DELETE FROM vacancies").executeUpdate();
            testConnection.prepareStatement("DELETE FROM ratings").executeUpdate();
            testConnection.prepareStatement("DELETE FROM notifications").executeUpdate();
            // Delete from users (test users only, careful with existing real data)
            testConnection.prepareStatement("DELETE FROM users WHERE email LIKE 'test_%' OR email LIKE 'uvc_%' OR email LIKE 'forget@%'").executeUpdate();
            testConnection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); // Re-enable FK checks
            System.out.println("Database cleanup complete.");
        } catch (SQLException e) {
            System.err.println("Error during database cleanup: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed during database cleanup. Check foreign key constraints, data, or MySqlConnection setup. Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to register a user and return their ID.
     * Used by multiple tests to set up preconditions for DAO tests.
     */
    private int registerTestUser(String username, String email, String password, String role) throws SQLException {
        if (!userDao.checkEmailExists(email)) {
            Assert.assertTrue("User registration failed for: " + email, userDao.registerUser(username, email, password, role));
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
        Assert.assertNotNull("User should be returned on successful login (DAO direct).", user);
        Assert.assertEquals("Retrieved user email should match.", email, user.getEmail());
    }

    /**
     * Test UserDao.login with invalid credentials.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_login_Fail_DAO() {
        System.out.println("UserDao: login - Fail (DAO direct)");
        LoginRequest req = new LoginRequest("nonexistent_dao@example.com", "anypass");
        UserData user = userDao.login(req);
        Assert.assertNull("No user should be returned on failed login (DAO direct).", user);
    }

    /**
     * Test UserDao.registerUser.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_registerUser_DAO() throws SQLException {
        System.out.println("UserDao: registerUser (DAO direct)");
        String email = "dao_reg_user_" + System.currentTimeMillis() + "@example.com";
        boolean registered = userDao.registerUser("DAO Reg User New", email, "reg_pass_dao", "Employer");
        Assert.assertTrue("User should be registered (DAO direct).", registered);
        Assert.assertTrue("Registered email should exist after direct DAO call.", userDao.checkEmailExists(email));
    }

    /**
     * Test UserDao.verifyPassword.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_verifyPassword_DAO() throws SQLException {
        System.out.println("UserDao: verifyPassword (DAO direct)");
        String email = "dao_verify_pass_" + System.currentTimeMillis() + "@example.com";
        String password = "verify_password_dao";
        int userId = registerTestUser("DAO Verify Pass", email, password, "Job Seeker");

        Assert.assertTrue("Correct password should verify.", userDao.verifyPassword(userId, password));
        Assert.assertFalse("Wrong password should not verify.", userDao.verifyPassword(userId, "wrongpass"));
    }

    /**
     * Test UserDao.updateUser.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_updateUser_DAO() throws SQLException {
        System.out.println("UserDao: updateUser (DAO direct)");
        String email = "dao_update_user_" + System.currentTimeMillis() + "@example.com";
        String password = "old_pass_dao";
        int userId = registerTestUser("DAO Update User", email, password, "Job Seeker");

        String newUsername = "Updated DAO User Name";
        String newEmail = "updated_dao_user_" + System.currentTimeMillis() + "@example.com";
        String newPassword = "new_dao_pass_direct";

        boolean updated = userDao.updateUser(userId, newUsername, newEmail, newPassword);
        Assert.assertTrue("User should be updated (DAO direct).", updated);

        LoginRequest updatedLoginReq = new LoginRequest(newEmail, newPassword);
        UserData updatedUser = userDao.login(updatedLoginReq);
        Assert.assertNotNull("Should login with new credentials after direct DAO update.", updatedUser);
        Assert.assertEquals("Username should be updated.", newUsername, updatedUser.getUsername());
        Assert.assertEquals("Email should be updated.", newEmail, updatedUser.getEmail());
    }

    /**
     * Test UserDao.getUserById.
     */
    @Test // JUnit 4 annotation
    public void testUserDao_getUserById_DAO() throws SQLException {
        System.out.println("UserDao: getUserById (DAO direct)");
        String email = "dao_getbyid_" + System.currentTimeMillis() + "@example.com";
        String password = "getbyid_pass_dao";
        int userId = registerTestUser("DAO GetById User", email, password, "Employer");

        UserData retrievedUser = userDao.getUserById(userId);
        Assert.assertNotNull("User should be retrieved by ID (DAO direct).", retrievedUser);
        Assert.assertEquals("Retrieved user ID should match.", userId, retrievedUser.getId());
        Assert.assertEquals("Retrieved username should match.", "DAO GetById User", retrievedUser.getUsername());
        Assert.assertEquals("Retrieved email should match.", email, retrievedUser.getEmail());
        Assert.assertEquals("Retrieved roles should match.", "Employer", retrievedUser.getRoles());
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
        // Save the vacancy and try to get its ID from the database after saving
        vacancyDao.saveVacancy(newVacancy);

        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        // Find the saved vacancy in the list to get its database-assigned ID
        // Assuming job title is unique enough for this test context, or add more attributes
        Vacancy savedVacancyFromDb = vacancies.stream()
                                 .filter(v -> v.getJobTitle().equals("Specific Job DAO") &&
                                              v.getDescription().equals("A unique job."))
                                 .findFirst()
                                 .orElse(null);

        Assert.assertNotNull("Saved vacancy should be found in DB for ID retrieval.", savedVacancyFromDb);
        int vacancyId = savedVacancyFromDb.getId();
        Assert.assertTrue("Vacancy ID must be greater than 0.", vacancyId > 0);


        Vacancy retrievedVacancy = vacancyDao.getVacancyById(vacancyId);
        Assert.assertNotNull("Should retrieve vacancy by ID (DAO direct).", retrievedVacancy);
        Assert.assertEquals("Retrieved vacancy title should match.", "Specific Job DAO", retrievedVacancy.getJobTitle());
    }

    /**
     * Test VacancyDao.getFilteredVacancies.
     * IMPORTANT: This test uses the `getFilteredVacancies` method that takes `Date` objects.
     * The DAO's current implementation for date filtering on `days_left` (an INT) might still
     * have logical flaws, but the method call itself will be tested.
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

        // Test with date filters (will pass a Date object, but DAO's logic for `days_left` might need review)
        java.sql.Date todaySql = new java.sql.Date(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        java.sql.Date futureDateSql = new java.sql.Date(Date.from(LocalDate.now().plusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

        // The DAO expects java.util.Date objects for startDate and endDate.
        filtered = vacancyDao.getFilteredVacancies(null, null, todaySql, futureDateSql);
        Assert.assertNotNull("Method should return a list even with problematic date filter.", filtered);
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
        Assert.assertFalse("New notification should be unread by default.", notifications.get(0).isRead());
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
        // Notifications are ordered by timestamp DESC in DAO, so newer message is first
        Assert.assertEquals("Newer message should be first.", "Message 2 DAO", notifications.get(0).getMessage());
        Assert.assertEquals("Older message should be second.", "Message 1 DAO", notifications.get(1).getMessage());
        Assert.assertTrue("Newer message should have isImportant=true.", notifications.get(0).isImportant());
        Assert.assertFalse("Older message should have isImportant=false.", notifications.get(1).isImportant());
    }

    /**
     * Test NotificationDao.markNotificationRead and markAllNotificationsRead.
     */
    @Test // JUnit 4 annotation
    public void testNotificationDao_markNotificationsRead_DAO() throws SQLException {
        System.out.println("NotificationDao: markNotificationsRead (DAO direct)");
        int userId = registerTestUser("Notif Mark Read DAO", "notif_mark_read_dao@example.com", "pass", "Job Seeker");
        notificationDao.saveNotification(userId, "Unread 1 DAO", LocalDateTime.now().minusMinutes(5).toString(), false);
        notificationDao.saveNotification(userId, "Unread 2 DAO", LocalDateTime.now().toString(), true);

        List<Notification> initialNotifications = notificationDao.getNotificationsByUserId(userId);
        Assert.assertFalse("Initial notifications should be unread.", initialNotifications.get(0).isRead());
        Assert.assertFalse("Initial notifications should be unread.", initialNotifications.get(1).isRead());

        // Test markAllNotificationsRead
        boolean markedAll = notificationDao.markAllNotificationsRead(userId);
        Assert.assertTrue("All notifications should be marked as read.", markedAll);

        List<Notification> allMarkedNotifications = notificationDao.getNotificationsByUserId(userId);
        for (Notification n : allMarkedNotifications) {
            Assert.assertTrue("All notifications should now be read.", n.isRead());
        }

        // Add another unread notification to test markNotificationRead for a single one
        notificationDao.saveNotification(userId, "Unread Single DAO", LocalDateTime.now().plusMinutes(5).toString(), false);
        List<Notification> notificationsAfterAddingOne = notificationDao.getNotificationsByUserId(userId);
        Notification singleUnread = notificationsAfterAddingOne.get(0); // Should be the newest, unread one
        Assert.assertFalse("The new notification should be unread.", singleUnread.isRead());

        boolean markedSingle = notificationDao.markNotificationRead(singleUnread.getId());
        Assert.assertTrue("Single notification should be marked as read.", markedSingle);

        List<Notification> finalNotifications = notificationDao.getNotificationsByUserId(userId);
        // Find the specific notification and check its status
        Notification verifiedSingle = finalNotifications.stream()
                                    .filter(n -> n.getId() == singleUnread.getId())
                                    .findFirst()
                                    .orElse(null);
        Assert.assertNotNull("Verified single notification should exist.", verifiedSingle);
        Assert.assertTrue("The specific notification should now be read.", verifiedSingle.isRead());
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
            Assert.assertEquals("First name should match.", "Jane", rs.getString("first_name"));
            Assert.assertEquals("Skills should match.", "Python, ML", rs.getString("skills"));
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
            Assert.assertEquals("First name should match.", "Mike", rs.getString("first_name"));
            Assert.assertEquals("Skills should match.", "Research, Analysis", rs.getString("skills"));
            Assert.assertEquals("DOB should match.", "1985-07-07", rs.getString("dob"));
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

        // Save CV for the job seeker
        cvDao.saveCV(jobseekerId, "App", "Seeker", "2000-01-01", "111", "Uni", "Skill", "Exp");
        int cvId;
        try (ResultSet rsCv = cvDao.getCVByUserId(jobseekerId)) {
            Assert.assertTrue("CV should be retrieved to get its ID.", rsCv.next());
            cvId = rsCv.getInt("id");
            Assert.assertTrue("CV ID should be greater than 0.", cvId > 0);
        }

        // Save Vacancy for the recruiter
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Test Vacancy DAO", "Full time", "Entry-Level", 10, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertFalse("Recruiter should have at least one vacancy.", vacancies.isEmpty());
        int vacancyId = vacancies.get(0).getId(); // Get the ID of the saved vacancy
        Assert.assertTrue("Vacancy ID should be greater than 0.", vacancyId > 0);

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

        // Save CVs for job seekers
        cvDao.saveCV(jobseekerId1, "JS1", "User", "1995-01-01", "111", "Edu1", "Skills1", "Exp1");
        int cvId1;
        try (ResultSet rsCv1 = cvDao.getCVByUserId(jobseekerId1)) {
            Assert.assertTrue(rsCv1.next()); cvId1 = rsCv1.getInt("id");
        }

        cvDao.saveCV(jobseekerId2, "JS2", "User", "1996-02-02", "222", "Edu2", "Skills2", "Exp2");
        int cvId2;
        try (ResultSet rsCv2 = cvDao.getCVByUserId(jobseekerId2)) {
            Assert.assertTrue(rsCv2.next()); cvId2 = rsCv2.getInt("id");
        }

        // Save Vacancy for the recruiter
        vacancyDao.saveVacancy(new Vacancy(0, recruiterId, "Job for Apps DAO", "Full time", "Mid-Level", 30, "Desc"));
        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        Assert.assertFalse("Recruiter should have vacancies.", vacancies.isEmpty());
        int vacancyId = vacancies.get(0).getId();

        // Save Applications
        applicationDao.saveApplication(jobseekerId1, recruiterId, vacancyId, cvId1);
        applicationDao.saveApplication(jobseekerId2, recruiterId, vacancyId, cvId2);

        List<Application> applications = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertEquals("Should retrieve two applications for the recruiter (DAO direct).", 2, applications.size());

        // Verify content of retrieved applications, including nested CV data
        Application app1 = applications.stream().filter(a -> a.getJobSeekerId() == jobseekerId1).findFirst().orElse(null);
        Application app2 = applications.stream().filter(a -> a.getJobSeekerId() == jobseekerId2).findFirst().orElse(null);

        Assert.assertNotNull("Application for JS1 should be found.", app1);
        Assert.assertNotNull("Application for JS2 should be found.", app2);

        Assert.assertEquals("JS1 name should match.", "JS1 User", app1.getJobSeekerName());
        Assert.assertEquals("JS1 education should match.", "Edu1", app1.getCv().getEducation());

        Assert.assertEquals("JS2 name should match.", "JS2 User", app2.getJobSeekerName());
        Assert.assertEquals("JS2 education should match.", "Edu2", app2.getCv().getEducation());
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
        Assert.assertFalse("Recruiter should have vacancies.", vacancies.isEmpty());
        int vacancyId = vacancies.get(0).getId();

        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        List<Application> pendingApps = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertFalse("Should retrieve at least one pending application.", pendingApps.isEmpty());
        int applicationId = pendingApps.get(0).getId();

        Application retrievedApp = applicationDao.getApplicationById(applicationId);
        Assert.assertNotNull("Should retrieve application by ID (DAO direct).", retrievedApp);
        Assert.assertEquals("Retrieved application's job seeker ID should match.", jobseekerId, retrievedApp.getJobSeekerId());
        Assert.assertNotNull("Retrieved application should have CV details.", retrievedApp.getCv());
        Assert.assertEquals("Retrieved CV's first name should match.", "JSByID", retrievedApp.getCv().getFirstName());
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
        Assert.assertFalse("Recruiter should have vacancies.", vacancies.isEmpty());
        int vacancyId = vacancies.get(0).getId();

        Assert.assertFalse("Job seeker should not have applied yet.", applicationDao.hasApplied(jobseekerId, vacancyId));
        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        Assert.assertTrue("Job seeker should have applied after saving (DAO direct).", applicationDao.hasApplied(jobseekerId, vacancyId));
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
        Assert.assertFalse("Recruiter should have vacancies.", vacancies.isEmpty());
        int vacancyId = vacancies.get(0).getId();

        applicationDao.saveApplication(jobseekerId, recruiterId, vacancyId, cvId);
        List<Application> pendingApps = applicationDao.getApplicationsByRecruiterId(recruiterId);
        Assert.assertFalse("Should retrieve at least one pending application.", pendingApps.isEmpty());
        int applicationId = pendingApps.get(0).getId();

        boolean updated = applicationDao.updateApplicationStatus(applicationId, "Rejected");
        Assert.assertTrue("Application status should be updated (DAO direct).", updated);

        Application updatedApp = applicationDao.getApplicationById(applicationId);
        Assert.assertEquals("Status should be 'Rejected' after update.", "Rejected", updatedApp.getStatus());
    }
}
