package pt.ubi.di.pmd.projeto_v1_user;

public class Evento {
    public String nome,des,regra;
    public int total,numpessoas,fechado;
    public Evento(){
    }
    public Evento(String nome,String des,String regra,int total,int numpessoas,int fechado){
        this.nome=nome;
        this.des=des;
        this.regra=regra;
        this.total=total;
        this.numpessoas=numpessoas;
        this.fechado=fechado;
    }

    public String getNome() {
        return nome;
    }

    public String getDes() {
        return des;
    }

    public String getRegra() {
        return regra;
    }

    public int getTotal() {
        return total;
    }

    public int getNumpessoas() {
        return numpessoas;
    }

    public int getFechado() { return fechado; }

    @Override
    public String toString() {
        return "Evento{" +
                "nome='" + nome + '\'' +
                ", des='" + des + '\'' +
                ", regra='" + regra + '\'' +
                ", total=" + total +
                ", numpessoas=" + numpessoas +
                ", fechado=" + fechado +
                '}';
    }
}
