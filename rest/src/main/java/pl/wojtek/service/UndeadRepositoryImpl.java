package pl.wojtek.service;

import org.springframework.stereotype.Component;
import pl.wojtek.domain.Undead;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class UndeadRepositoryImpl implements UndeadRepository {

    private Connection connection;

    private PreparedStatement addStatement;
    private PreparedStatement getAllStatement;
    private PreparedStatement getByIdStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public UndeadRepositoryImpl(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady()) {
            createTables();
        }
        this.setConnection(this.connection);
    }

    public UndeadRepositoryImpl() throws SQLException{
        this.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb");
        if (!isDatabaseReady()) {
            createTables();
        }
        this.setConnection(this.connection);
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE "
                        + "Undead(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
                        "name varchar(20) NOT NULL, " + "age integer, " + "numberOfLegs integer)");
    }

    public boolean isDatabaseReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if ("Undead".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
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
                        ("INSERT INTO Undead (name, age, numberOfLegs) VALUES (?, ?, ?)");
        getAllStatement = connection.
                prepareStatement("SELECT id, name, age, numberOfLegs FROM Undead");
        updateStatement = connection.
                prepareStatement("UPDATE Undead SET name = ?, age = ?, numberOfLegs = ? WHERE id = ?");
        getByIdStatement = connection.
                prepareStatement("SELECT id, name, age, numberOfLegs FROM Undead WHERE id = ?");
        deleteStatement = connection.
                prepareStatement("DELETE FROM Undead WHERE id = ?");
    }

    @Override
    public int add(Undead undead) {
        try{
            addStatement.setString(1,undead.getName());
            addStatement.setInt(2,undead.getAge());
            addStatement.setInt(3,undead.getNumberOfLegs());
            return addStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
    }

    @Override
    public Undead getById(int id){
        Undead p = new Undead();
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
    public int update(int oldId, Undead newUndead) throws SQLException{
        int count = 0;
        try {
            updateStatement.setString(1, newUndead.getName());
            updateStatement.setInt(2, newUndead.getAge());
            updateStatement.setInt(3, newUndead.getNumberOfLegs());
            updateStatement.setInt(4,oldId);

            count = updateStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;

    }

    @Override
    public int delete(Undead undead) throws SQLException{

        try{
            deleteStatement.setInt(1,undead.getId());
            return deleteStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }

    }


    @Override
    public List<Undead> getAll() {
        List<Undead> undeads = new LinkedList<>();
        try {
            ResultSet rs = getAllStatement.executeQuery();

            while (rs.next()) {
                Undead p = new Undead();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setAge(rs.getInt("numberOfLegs"));
                undeads.add(p);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return undeads;
    }

}