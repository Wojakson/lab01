package pl.wojtek.service;


import pl.wojtek.domain.Undead;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UndeadRepository {
    public void setConnection(Connection connection) throws SQLException;
    public Connection getConnection();
    Undead getById(int id) throws SQLException;
    public List<Undead> getAll();
    public int add(Undead undead);
    public int delete(Undead undead) throws SQLException;
    public int update(int oldId, Undead newUndead) throws SQLException;
}
