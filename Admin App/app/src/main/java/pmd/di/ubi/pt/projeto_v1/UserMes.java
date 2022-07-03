package pmd.di.ubi.pt.projeto_v1;


import java.util.Comparator;

public class UserMes {
    public String username;
    public int mes;

    public UserMes() {
    }

    public UserMes(String username, int mes) {
        this.username = username;
        this.mes = mes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    @Override
    public String toString() {
        return "UserMes{" +
                "username='" + username + '\'' +
                '}';
    }
}
