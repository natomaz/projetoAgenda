package br.ulbra.classes;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AgendaDao {

    Connection con;

    public AgendaDao() throws SQLException {
        con = ConnectionFactory.getConnection();
    }

    public boolean checkLogin(String email, String senha) throws NoSuchAlgorithmException {

        PreparedStatement stmt = null;
        Agenda a = new Agenda();
        ResultSet rs = null;
        boolean check = false;
        BigInteger cripto;
        cripto = a.criptografarSenha(senha);

        try {
            stmt = con.prepareStatement("SELECT * FROM tbagenda where email = ? AND senha = ?");
            stmt.setString(1, email);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            if (rs.next()) {
                check = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
        return check;
    }

    // Salvar
    public void create(Agenda a) throws NoSuchAlgorithmException {
        PreparedStatement stmt = null;
        BigInteger cripto;
        try {
            stmt = con.prepareStatement("INSERT INTO tbagenda (nome,"
                    + "email,senha,telefone, recado) VALUE (?,?,?,?,?)");
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getEmail());
            cripto = a.criptografarSenha(a.getSenha());
            stmt.setString(3, cripto.toString());
            stmt.setString(4, a.getTelefone());
            stmt.setString(4, a.getRecado());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Contato " + a.getNome()
                    + " salvo com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    //Alterar
    public void update(Agenda a) throws NoSuchAlgorithmException {
        PreparedStatement stmt = null;
        BigInteger cripto;
        try {
            stmt = con.prepareStatement("UPDATE tbagenda SET nome = ?,"
                    + "email = ?, senha = ? ,telefone = ?, recado = ? WHERE id = ?");
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getEmail());
            cripto = a.criptografarSenha(a.getSenha());
            stmt.setString(3, cripto.toString());
            stmt.setString(4, a.getTelefone());
            stmt.setString(5, a.getRecado());
            stmt.setInt(6, a.getId());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Contato " + a.getNome()
                    + " modificado com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    //Listar na tabela
    public ArrayList<Agenda> read() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Agenda> contatos = new ArrayList<Agenda>();
        try {
            stmt = con.prepareStatement("SELECT * FROM tbagenda ORDER BY nome ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(rs.getInt("id"));
                agenda.setNome(rs.getString("nome"));
                agenda.setEmail(rs.getString("email"));
                agenda.setSenha(rs.getString("senha"));
                agenda.setTelefone(rs.getString("telefone"));
                agenda.setRecado(rs.getString("recado"));
                contatos.add(agenda);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return (ArrayList<Agenda>) contatos;
    }

    public ArrayList<Agenda> readPesq(String nome) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Agenda> contatos = new ArrayList<Agenda>();
        try {
            stmt = con.prepareStatement("SELECT * FROM tbagenda WHERE nome LIKE ?");
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(rs.getInt("id"));
                agenda.setNome(rs.getString("nome"));
                agenda.setEmail(rs.getString("email"));
                agenda.setSenha(rs.getString("senha"));
                agenda.setTelefone(rs.getString("telefone"));
                agenda.setRecado(rs.getString("recado"));
                contatos.add(agenda);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return (ArrayList<Agenda>) contatos;
    }

        
    //Excluir
    public void delete(Agenda a) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("DELETE FROM tbagenda WHERE id = ?");

            stmt.setInt(1, a.getId());

            if (JOptionPane.showConfirmDialog(null, "Tem certeza que"
                    + " deseja excluir este contato ", "Exclusão",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Contato excluído com sucesso!");
                stmt.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(null, "A exclusão do contato cancelado com sucesso!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

}
