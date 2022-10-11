package cards;

public class ResponseAuthUserCard {

    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserCard user;

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserCard getUser() {
        return user;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }
}
