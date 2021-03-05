package br.edu.ifpb.web.jsf;

//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
import br.edu.ifpb.domain.Dependente;
import br.edu.ifpb.domain.Pessoa;
import br.edu.ifpb.domain.Pessoas;
import br.edu.ifpb.domain.service.PessoaServico;
import br.edu.ifpb.infra.persistence.memory.PessoasEmMemoria;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 01/02/2021, 10:01:33
 */
//@ManagedBean
@Named
//@RequestScoped
@SessionScoped
public class Controlador implements Serializable {

    private Pessoa pessoa = new Pessoa();
    private Dependente dependente = new Dependente();
    private String dtnascimento;
    // private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private PessoasEmMemoria servicoMemoria = new PessoasEmMemoria();
    private Pessoas pessoasServico = new PessoaServico();
    private List<Pessoa> pessoas = Collections.EMPTY_LIST;
    private String CPF;
    // private Pessoas pessoas = new PessoasEmMemoria();

    public String redirecionar() {
        // executando a lógica de negócio

        System.out.println("passei");
        pessoasServico.nova(pessoa);
        servicoMemoria.nova(pessoa);
        pessoa = new Pessoa();
        // redirecionando...
        return null; // fica na página original
//        return "home"; // encmainhar a requisição à página 
//        return "home.xhtml?faces-redirect=true"; // nova requisição
    }

    public String adicionar() {
        pessoasServico.nova(pessoa);
        // deveríamos ter um objeto responsável por encapsular essa regra de negócio

        servicoMemoria.nova(pessoa);
        pessoa = new Pessoa();

        return "list";
    }

    public String excluir(Pessoa pessoa) {
        //servicoMemoria.excluir(pessoa);
        pessoasServico.excluir(pessoa);
        this.pessoas = Collections.EMPTY_LIST;
        this.pessoa = new Pessoa();
        return null;
    }

    public String excluirDependente(Dependente dependente) {
        //servicoMemoria.excluir(pessoa);
        pessoasServico.excluirDependente(dependente);
        return "list";
    }

    public String editar(Pessoa pessoa) {
        this.pessoa = pessoa;
        //servicoMemoria.atualizar(pessoa);
        return "edit";
    }

    public String editarDependente(Dependente dependente) {
        this.dependente = dependente;
        dtnascimento = dependente.getDataDeNascimento().toString();
        pessoasServico.novo(dependente);
        //servicoMemoria.atualizar(pessoa);
        return "edit";
    }

    public String adicionarDependente() {
        LocalDate date = LocalDate.parse(dtnascimento);
        dependente.setDataDeNascimento(date);
        pessoasServico.novo(dependente);
        servicoMemoria.novo(dependente);
        this.dependente = new Dependente();
        return "list";
    }

    public List<Dependente> todosOsDependentes() {
        return servicoMemoria.todosOsDepentendes();
    }

    public List<Pessoa> todasAsPessoas() {
        // return servicoMemoria.todas();
        return pessoasServico.todas();

    }

    public String listCPF() {

        return "buscarCPF";
    }

    public String buscarCPF() {
        this.pessoas = pessoasServico.localizarPessoaComCPF(CPF);

        return null;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public String getDtnascimento() {
        return dtnascimento;
    }

    public void setDtnascimento(String dtnascimento) {
        this.dtnascimento = dtnascimento;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

}
