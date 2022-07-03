package pmd.di.ubi.pt.projeto_v1;

import java.util.Comparator;

public class ComparadorDeIdades implements Comparator<UserIdade> {
    public int compare(UserIdade o1, UserIdade o2) {
        if (o1.getDn().before(o2.getDn()) ) return -1;
        else if (o1.getDn().after(o2.getDn())) return +1;
        else return 0;
    }
}
