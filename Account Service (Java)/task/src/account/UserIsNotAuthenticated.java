package account;

public class UserIsNotAuthenticated extends Throwable {

    public UserIsNotAuthenticated() {
        super();
    }

    public UserIsNotAuthenticated(String message) {
        super(message);
    }
}
