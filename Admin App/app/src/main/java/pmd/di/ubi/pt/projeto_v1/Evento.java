package pmd.di.ubi.pt.projeto_v1;

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

    public String getNome(){
        return nome;
    }

    public String getDes(){
        return des;
    }

    public String getRegra(){
        return regra;
    }

    public int getTotal(){
        return total;
    }

    public int getNumpessoas(){ return numpessoas; }

    public String toString(){
        return nome;
    }
}
