package br.edu.ifpb.infra.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.edu.ifpb.domain.Dependente;
import br.edu.ifpb.domain.Pessoa;
import br.edu.ifpb.domain.Pessoas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class PessoasJDBC implements Pessoas {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Connection conexao;
    private Pessoa p;
    private DependenteJDBC dBC;
    
    public PessoasJDBC() {
        //abrirConexao();
        dBC = new DependenteJDBC();
    }
    
    @Override
    public void nova(Pessoa pessoa) {
        System.err.println("banxo");
        String sql = "INSERT INTO pessoa (nome,cpf ) VALUES(?,?)";
        abrirConexao();
        try {
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, pessoa.getNome());
            statement.setString(2, pessoa.getCpf().valor());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
    }
    
    @Override
    public List<Pessoa> todas() {
        // TODO Auto-generated method stub
        abrirConexao();
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM pessoa;";
            PreparedStatement statement = conexao.prepareStatement(consulta);
            ResultSet resut = statement.executeQuery();
            while (resut.next()) {
                pessoas.add(criarPessoa(resut));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
        if (pessoas.size() > 0) {
            return Collections.unmodifiableList(pessoas);
        } else {
            return Collections.EMPTY_LIST;
        }
        
    }
    
    @Override
    public void excluir(Pessoa pessoa) {
        // TODO Auto-generated method stub
        String sql = "DELETE FROM pessoa WHERE uuid=?";
        try {
            System.err.println("removendo");
            abrirConexao();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setLong(1, pessoa.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
        
    }
    
    @Override
    public void atualizar(Pessoa pessoa) {
        System.err.println("banxo");
        String sql = "UPDATE pessoa SET nome=?, cpf=? WHERE uuid=?";
        abrirConexao();
        try {
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, pessoa.getNome());
            statement.setString(2, pessoa.getCpf().valor());
            statement.setLong(3, pessoa.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
        
    }
    
    @Override
    public Pessoa localizarPessoaComId(long id) {
        Pessoa pessoa = new Pessoa();
        try {
            abrirConexao();
            String consulta = "SELECT * FROM pessoa WHERE uuid=?;";
            PreparedStatement statement = conexao.prepareStatement(consulta);
            System.err.println("ai b " + id);
            statement.setLong(1, id);
            pessoa = percorrerPessoa(statement);
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
            pessoa = Pessoa.fake();
        } finally {
            fecharConexao();
        }
        
        return pessoa;
    }
    
    @Override
    public List<Dependente> todosOsDepentendes() {
        // TODO Auto-generated method stub
        List<Dependente> e = dBC.todosOsDepentendes();
        //System.err.println("www " + e.get(0).getNome());
        return e;
        
    }
    
    @Override
    public Dependente localizarDependenteComId(long uuid) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void novo(Dependente dependente) {
        // TODO Auto-generated method stub
        if (dependente.getUuid() < 0) {
            dBC.nova(dependente);
        } else {
            dBC.atualizar(dependente);
        }
    }
    
    private void abrirConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conexao = DriverManager.getConnection(
            		"jdbc:postgresql://localhost:5432/Dac",
                    "postgres", "06121995");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fecharConexao() {
        try {
            this.conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Pessoa percorrerPessoa(PreparedStatement statement) {
        try {
            ResultSet executeQuery = statement.executeQuery();
            executeQuery.next();
            
            return criarPessoa(executeQuery);
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Pessoa.fake();
    }
    
    private Pessoa criarPessoa(ResultSet result) {
        Pessoa pessoa = null;
        
        try {
            //  result.next();
            pessoa = Pessoa.of(result.getInt("uuid"),
                    result.getString("nome"),
                    result.getString("cpf"));
            System.err.println("pessoa " + pessoa.getNome());
        } catch (SQLException ex) {
            Pessoa.fake();
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pessoa;
    }
    
    @Override
    public List<Pessoa> localizarPessoaComCPF(String cpf) {
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            abrirConexao();
            String consulta = "SELECT * FROM pessoa WHERE cpf ilike '" + cpf + "%' ";
            System.err.println("consuta " + consulta);
            PreparedStatement statement = conexao.prepareStatement(consulta);
            System.err.println("bsca cpf" + cpf);
            //  statement.setString(1, cpf);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                pessoas.add(criarPessoa(result));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            fecharConexao();
        }
        if (pessoas.size() > 0) {
            return Collections.unmodifiableList(pessoas);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public void excluirDependente(Dependente dependente) {
        dBC.excluir(dependente);
    }
    
}
