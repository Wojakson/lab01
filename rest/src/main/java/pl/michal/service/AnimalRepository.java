package pl.wojtek.service;


import pl.wojtek.domain.Animal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AnimalRepository {
    public void setConnection(Connection connection) throws SQLException;
    public Connection getConnection();
    Animal getById(int id) throws SQLException;
    public List<Animal> getAll();
    public int add(Animal animal);
    public int delete(Animal animal) throws SQLException;
    public int update(int oldId, Animal newAnimal) throws SQLException;
}
