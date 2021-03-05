package br.edu.ifpb.domain.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.edu.ifpb.domain.Dependente;
import br.edu.ifpb.domain.Pessoa;
import br.edu.ifpb.domain.Pessoas;
import br.edu.ifpb.infra.persistence.jdbc.PessoasJDBC;
import br.edu.ifpb.infra.persistence.memory.PessoasEmMemoria;

@RequestScoped
public class PessoaServico implements Pessoas {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Pessoas PessoasJDBC;

    public PessoaServico() {
        this.PessoasJDBC = new PessoasJDBC();

    }

    @Override
    public void nova(Pessoa pessoa) {
        System.err.println("ssssss");
        Pessoa retorno = this.PessoasJDBC.localizarPessoaComId(pessoa.getId());
        System.err.println("retorno " + retorno);
        //  PessoasJDBC.nova(pessoa);
        if (null == retorno || Pessoa.fake().equals(retorno)) {
            System.err.println("aqui ");
            PessoasJDBC.nova(pessoa);

        } else {
            PessoasJDBC.atualizar(pessoa);
            System.err.println("up aqui");
        }

    }

    @Override
    public List<Pessoa> todas() {
        // TODO Auto-generated method stub
        return this.PessoasJDBC.todas();
    }

    @Override
    public void excluir(Pessoa pessoa) {
        // TODO Auto-generated method stub
        this.PessoasJDBC.excluir(pessoa);
    }

    @Override
    public void excluirDependente(Dependente dependente) {
        this.PessoasJDBC.excluirDependente(dependente);
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        // TODO Auto-generated method stub
        this.PessoasJDBC.atualizar(pessoa);
        //this.PessoasJDBC.atualizar(pessoa);

    }

    @Override
    public Pessoa localizarPessoaComId(long id) {
        // TODO Auto-generated method stub
        return PessoasJDBC.localizarPessoaComId(id);

    }

    @Override
    public List<Dependente> todosOsDepentendes() {
        // TODO Auto-generated method stub
        System.err.println("dddddd");
        return this.PessoasJDBC.todosOsDepentendes();
    }

    @Override
    public Dependente localizarDependenteComId(long uuid) {
        // TODO Auto-generated method stub
        return this.PessoasJDBC.localizarDependenteComId(uuid);
    }

    @Override
    public void novo(Dependente dependente) {
        // TODO Auto-generated method stub
        System.out.println("ffffff");
        this.PessoasJDBC.novo(dependente);

    }

    @Override
    public List<Pessoa> localizarPessoaComCPF(String cpf) {
        List<Pessoa> ps = this.PessoasJDBC.localizarPessoaComCPF(cpf);
        for (Pessoa p : ps) {
        }
        return ps;
    }
}
