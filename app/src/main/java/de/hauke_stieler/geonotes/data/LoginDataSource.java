package de.hauke_stieler.geonotes.data;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    /**
     * Will attempt to login user with provided credentials
     *
     * @param username - username for login
     * @param password - password for login
     * @return - success or failure
     */
    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    /**
     * Will logout user
     */
    public void logout() {
        // TODO: revoke authentication
    }
}