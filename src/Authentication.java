public class Authentication {
    private String password;

    public boolean setPassword(String newPassword) {
        this.password = newPassword;
        return true;
    }

    public boolean verifyPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
