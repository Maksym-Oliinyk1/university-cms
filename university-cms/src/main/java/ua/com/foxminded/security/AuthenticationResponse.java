package ua.com.foxminded.security;


public class AuthenticationResponse {

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse() {

    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
