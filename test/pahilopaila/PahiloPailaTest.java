package pahilopaila;

import pahilopaila.Dao.*;
import pahilopaila.model.*;
import pahilopaila.database.MySqlConnection;

import org.junit.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class PahiloPailaTest {

    private UserDao userDao;
    private CVDao cvDao;
    private VacancyDao vacancyDao;
    private RatingDao ratingDao;
    private NotificationDao notificationDao;
    private ApplicationDao applicationDao;
    private static Connection testConnection;

    @BeforeClass
    public static void setUpClass() {
        try {
            testConnection = MySqlConnection.getInstance().getConnection();
            Assert.assertNotNull("Database connection failed", testConnection);
        } catch (Exception e) {
            Assert.fail("DB connection error: " + e.getMessage());
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            if (testConnection != null) testConnection.close();
        } catch (SQLException e) {
            System.err.println("Error closing DB: " + e.getMessage());
        }
    }

    @Before
    public void setUp() {
        userDao = new UserDao();
        cvDao = new CVDao();
        vacancyDao = new VacancyDao();
        ratingDao = new RatingDao();
        notificationDao = new NotificationDao();
        applicationDao = new ApplicationDao();
        cleanupDatabase();
    }

    @After
    public void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        try {
            testConnection.prepareStatement("DELETE FROM applications").executeUpdate();
            testConnection.prepareStatement("DELETE FROM cvs").executeUpdate();
            testConnection.prepareStatement("DELETE FROM vacancies").executeUpdate();
            testConnection.prepareStatement("DELETE FROM ratings").executeUpdate();
            testConnection.prepareStatement("DELETE FROM notifications").executeUpdate();
            testConnection.prepareStatement("DELETE FROM users WHERE email LIKE 'test_%' OR email LIKE 'uvc_%' OR email LIKE 'forget@%'").executeUpdate();
        } catch (SQLException e) {
            Assert.fail("Cleanup error: " + e.getMessage());
        }
    }

    private int registerTestUser(String username, String email, String password, String role) throws SQLException {
        if (!userDao.checkEmailExists(email)) {
            Assert.assertTrue("User registration failed", userDao.registerUser(username, email, password, role));
        }
        UserData user = userDao.login(new LoginRequest(email, password));
        Assert.assertNotNull("User login failed post-registration", user);
        return user.getId();
    }

    @Test
    public void testUserDao_login_Success_DAO() throws SQLException {
        String email = "dao_login_user_" + System.currentTimeMillis() + "@example.com";
        String password = "dao_password_valid";
        registerTestUser("DAO Login User", email, password, "Job Seeker");
        UserData user = userDao.login(new LoginRequest(email, password));
        Assert.assertNotNull("Login should succeed", user);
        Assert.assertEquals(email, user.getEmail());
    }

    @Test
    public void testUserDao_login_Fail_DAO() {
        UserData user = userDao.login(new LoginRequest("no_user@example.com", "badpass"));
        Assert.assertNull("Login should fail", user);
    }

    @Test
    public void testUserDao_registerUser_DAO() throws SQLException {
        String email = "dao_reg_user_" + System.currentTimeMillis() + "@example.com";
        boolean registered = userDao.registerUser("DAO Reg", email, "reg_pass", "Employer");
        Assert.assertTrue("Should register user", registered);
        Assert.assertTrue("Email should exist", userDao.checkEmailExists(email));
    }

    @Test
    public void testUserDao_verifyPassword_DAO() throws SQLException {
        String email = "dao_verify_pass_" + System.currentTimeMillis() + "@example.com";
        String password = "verify_password_dao";
        int userId = registerTestUser("VerifyPassUser", email, password, "Job Seeker");

        Assert.assertTrue("Correct password should verify", userDao.verifyPassword(userId, password));
        Assert.assertFalse("Wrong password should not verify", userDao.verifyPassword(userId, "wrongpass"));
    }

    @Test
    public void testUserDao_updateUser_DAO() throws SQLException {
        String email = "dao_update_user_" + System.currentTimeMillis() + "@example.com";
        String password = "old_pass";
        int userId = registerTestUser("UpdateUser", email, password, "Job Seeker");

        String newEmail = "updated_user_" + System.currentTimeMillis() + "@example.com";
        boolean updated = userDao.updateUser(userId, "Updated Name", newEmail, "new_pass");
        Assert.assertTrue("User should be updated", updated);

        UserData updatedUser = userDao.login(new LoginRequest(newEmail, "new_pass"));
        Assert.assertNotNull("Should login with new credentials", updatedUser);
        Assert.assertEquals("Updated Name", updatedUser.getUsername());
        Assert.assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    public void testUserDao_getUserById_DAO() throws SQLException {
        String email = "dao_getbyid_" + System.currentTimeMillis() + "@example.com";
        int userId = registerTestUser("GetById", email, "pass", "Employer");

        UserData user = userDao.getUserById(userId);
        Assert.assertNotNull("Should fetch user by ID", user);
        Assert.assertEquals(userId, user.getId());
        Assert.assertEquals("GetById", user.getUsername());
        Assert.assertEquals(email, user.getEmail());
    }
}