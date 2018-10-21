package pl.wojtek.service;

import org.springframework.stereotype.Component;
import pl.wojtek.domain.Animal;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class AnimalRepositoryImpl implements AnimalRepository {

    private Connection connection;

    private PreparedStatement addStatement;
    private PreparedStatement getAllStatement;
    private PreparedStatement getByIdStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public AnimalRepositoryImpl(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady()) {
            createTables();
        }
        this.setConnection(this.connection);
    }

    public AnimalRepositoryImpl() throws SQLException{
        this.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb");
        if (!isDatabaseReady()) {
            createTables();
        }
        this.setConnection(this.connection);
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE "
                        + "Animal(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
                        "name varchar(20) NOT NULL, " + "age integer, " + "numberOfLegs integer)");
    }

    public boolean isDatabaseReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if ("Animal".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) throws SQLException {
        this.connection = connection;
        addStatement = connection.
                prepareStatement
                        ("INSERT INTO Animal (name, age, numberOfLegs) VALUES (?, ?, ?)");
        getAllStatement = connection.
                prepareStatement("SELECT id, name, age, numberOfLegs FROM Animal");
        updateStatement = connection.
                prepareStatement("UPDATE Animal SET name = ?, age = ?, numberOfLegs = ? WHERE id = ?");
        getByIdStatement = connection.
                prepareStatement("SELECT id, name, age, numberOfLegs FROM Animal WHERE id = ?");
        deleteStatement = connection.
                prepareStatement("DELETE FROM Animal WHERE id = ?");
    }

    @Override
    public int add(Animal animal) {
        try{
            addStatement.setString(1,animal.getName());
            addStatement.setInt(2,animal.getAge());
            addStatement.setInt(3,animal.getNumberOfLegs());
            return addStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
    }

    @Override
    public Animal getById(int id){
        Animal p = new Animal();
        try {
            getByIdStatement.setInt(1,id);
            ResultSet rs = getByIdStatement.executeQuery();

            while (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setAge(rs.getInt("numberOfLegs"));

            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }

        return p;
    }

    @Override
    public int update(int oldId, Animal newAnimal) throws SQLException{
        int count = 0;
        try {
            updateStatement.setString(1, newAnimal.getName());
            updateStatement.setInt(2, newAnimal.getAge());
            updateStatement.setInt(3, newAnimal.getNumberOfLegs());
            updateStatement.setInt(4,oldId);

            count = updateStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;

    }

    @Override
    public int delete(Animal animal) throws SQLException{

        try{
            deleteStatement.setInt(1,animal.getId());
            return deleteStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }

    }


    @Override
    public List<Animal> getAll() {
        List<Animal> animals = new LinkedList<>();
        try {
            ResultSet rs = getAllStatement.executeQuery();

            while (rs.next()) {
                Animal p = new Animal();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setAge(rs.getInt("numberOfLegs"));
                animals.add(p);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return animals;
    }

}
