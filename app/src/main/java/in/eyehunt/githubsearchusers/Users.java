package in.eyehunt.githubsearchusers;

import java.io.Serializable;

/**
 * Created by rkkanojia7 on 20/05/18.
 */

public class Users implements Serializable {
    private int id;
    private String login;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
