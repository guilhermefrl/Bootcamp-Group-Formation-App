package pmd.di.ubi.pt.projeto_v1;

public class User {
    public String nome,apelido,email,password,dn,genero, distrito;
        public User(){

        }
    public User(String nome, String apelido,String email,String password, String dn,String genero, String distrito){
        this.nome=nome;
        this.apelido=apelido;
        this.email=email;
        this.password=password;
        this.dn=dn;
        this.genero=genero;
        this.distrito=distrito;
    }

    public String getNome() {
        return nome;
    }

    public String getApelido() { return apelido; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDn() {
        return dn;
    }

    public String getGenero() {
        return genero;
    }

    public String getDistrito() {
        return distrito;
    }
}
