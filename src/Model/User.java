package Model;

public class User {

    private static int userID;
    private static String username;
    private static String password;

    /**
     * Constructs a new User with default values.
     */
    public User() {
        this.userID = 0;
        this.username = null;
        this.password = null;
    }

    /**
     * Constructs a new User with the specified ID, username, and password.
     *
     * @param userID the ID of the user
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the ID of the user
     */
    public static int getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user.
     *
     * @param userID the new ID of the user
     */
    public static void setUserID(int userID) {
        User.userID = userID;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

