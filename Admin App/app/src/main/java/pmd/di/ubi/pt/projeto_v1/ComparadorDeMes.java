package pmd.di.ubi.pt.projeto_v1;

import java.util.Comparator;

public class ComparadorDeMes implements Comparator<UserMes> {
    public int compare(UserMes o1, UserMes o2) {
        if (o1.getMes() < o2.getMes()) return -1;
        else if (o1.getMes() > o2.getMes()) return +1;
        else return 0;
    }
}
