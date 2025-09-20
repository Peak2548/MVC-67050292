package Controller;

public class Authentication {
    private String user, pass;

    public Authentication() {
        this.user = null; this.pass = null;
    }
    public Authentication(String user, String pass) {
        this.user = user; this.pass = pass;
    }
    public boolean isAdmin(String user) {
        return user.equals("admin");
    }
}