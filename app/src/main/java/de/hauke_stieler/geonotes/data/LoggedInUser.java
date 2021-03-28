package de.hauke_stieler.geonotes.data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String userId;
    private final String displayName;

    /**
     * Constructor to create a new instance of a LoggedInUser
     *
     * @param userId      - unique user id
     * @param displayName - user's display name
     */
    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    /**
     * Will get user id
     *
     * @return - user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Will get user display name
     *
     * @return - display name
     */
    public String getDisplayName() {
        return displayName;
    }
}