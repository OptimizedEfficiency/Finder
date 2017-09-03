package thosekids.com.finder_sohacks4;

/**
 * Created by drewneely on 8/4/17.
 */

public class UserInformation {

    public String name;
    public String username;
    public String uid;

    public UserInformation(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public UserInformation(String name, String username, String uid) {
        this.name = name;
        this.username = username;
        this.uid = uid;
    }

    public String toString() {
        return String.format("{name = %s, username = %s}", name, username);
    }
}
