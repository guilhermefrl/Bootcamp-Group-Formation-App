package pmd.di.ubi.pt.projeto_v1;

import java.util.Date;

public class UserIdade {
    public String username;
    public Date dn;

    public UserIdade(String username, Date dn) {
        this.username = username;
        this.dn = dn;
    }

    public String getUsername() {
        return username;
    }

    public UserIdade() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDn() {
        return dn;
    }

    public void setDn(Date dn) {
        this.dn = dn;
    }
}
