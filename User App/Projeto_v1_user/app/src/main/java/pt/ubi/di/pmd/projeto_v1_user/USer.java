package pt.ubi.di.pmd.projeto_v1_user;

public class USer {
    public String nome,apelido,email,password,dn,genero,distrito;

    public USer(){

    }
    public USer(String nome, String apelido,String email,String password, String dn,String genero, String distrito){
        this.nome=nome;
        this.apelido=apelido;
        this.email=email;
        this.password=password;
        this.dn=dn;
        this.genero=genero;
        this.distrito=distrito;
    }

    public String getNome(){
        return nome;
    }

    public String getApelido(){return apelido;}

    public String getEmail(){return email;}

    public String getDn(){return dn;}

    public String getGenero(){return genero;}

    public String getDistrito(){return distrito;}

}
