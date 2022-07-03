package pmd.di.ubi.pt.projeto_v1;

public class Admin {
    public String nome;

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String apelido;
    public String email;
    public String password;
    public Admin(){

    }

    public Admin(String nome, String apelido,String email,String password){
        this.nome=nome;
        this.apelido=apelido;
        this.email=email;
        this.password=password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
