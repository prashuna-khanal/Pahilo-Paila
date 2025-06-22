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
