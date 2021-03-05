/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.infra.persistence.jdbc;

import br.edu.ifpb.domain.Dependente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class DependenteJDBC {

    private static final long serialVersionUID = 1L;
    private Connection conexao;
    private Dependente dep;

    public void nova(Dependente dependente) {
        System.err.println("banxo");
        String sql = "INSERT INTO dependente (nome, dataDeNascimento) VALUES(?,?)";
        abrirConexao();
        try {
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, dependente.getNome());
            statement.setString(2, dependente.getDataDeNascimento().toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
    }

    public List<Dependente> todas() {
        // TODO Auto-generated method stub
        abrirConexao();
        List<Dependente> dependente = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM dependente ";
            PreparedStatement statement = conexao.prepareStatement(consulta);
            ResultSet resut = statement.executeQuery();
            while (resut.next()) {
                dependente.add(criarDependente(resut));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
        if (dependente.size() > 0) {
            return Collections.unmodifiableList(dependente);
        } else {
            return Collections.EMPTY_LIST;
        }

    }

    public void excluir(Dependente dependente) {
        // TODO Auto-generated method stub
        String sql = "DELETE FROM dependente WHERE uuid=?";
        try {
            System.err.println("removendo");
            abrirConexao();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setLong(1, dependente.getUuid());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }

    }

    public void atualizar(Dependente dependente) {
        System.err.println("banxo");
        String sql = "UPDATE dependente SET nome=?, dataDenascimento=? WHERE uuid=?";
        abrirConexao();
        try {
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, dependente.getNome());
            statement.setString(2, dependente.getDataDeNascimento().toString());
            statement.setLong(3, dependente.getUuid());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }

    }

    public Dependente localizardependenteComId(long id) {
        Dependente dependente = new Dependente();
        try {
            abrirConexao();
            String consulta = "SELECT * FROM dependente WHERE uuid=?;";
            PreparedStatement statement = conexao.prepareStatement(consulta);
            System.err.println("ai b " + id);
            statement.setLong(1, id);
            dependente = percorrerDependente(statement);
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
            dependente = Dependente.fake();
        } finally {
            fecharConexao();
        }

        return dependente;
    }

    public List<Dependente> todosOsDepentendes() {
        abrirConexao();
        List<Dependente> deps = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM dependente;";
            PreparedStatement statement = conexao.prepareStatement(consulta);
            ResultSet resut = statement.executeQuery();
            while (resut.next()) {
                deps.add(criarDependente(resut));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fecharConexao();
        }
        if (deps.size() > 0) {
            return Collections.unmodifiableList(deps);
        } else {
            return Collections.EMPTY_LIST;
        }

    }

    public void novo(Dependente dependente) {
        // TODO Auto-generated method stub

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

    private Dependente percorrerDependente(PreparedStatement statement) {
        try {
            ResultSet executeQuery = statement.executeQuery();
            executeQuery.next();

            return criarDependente(executeQuery);
        } catch (SQLException ex) {
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Dependente.fake();
    }

    private Dependente criarDependente(ResultSet result) {
        Dependente dependente = null;

        try {
            //  result.next();
            dependente = Dependente.of(result.getInt("uuid"),
                    result.getString("nome"),
                    result.getString("cpfafiliado"),
                    result.getString("dataDenascimento")
            );
            System.err.println("pessoa " + dependente.getNome());
        } catch (SQLException ex) {
            Dependente.fake();
            Logger.getLogger(PessoasJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dependente;
    }

}
