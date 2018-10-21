package pl.wojtek.repository;

import pl.wojtek.domain.Undead;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class UndeadRepositoryFactory implements UndeadRepository {

    private Connection connection;

    private PreparedStatement addStatement;
    private PreparedStatement getAllStatement;
    private PreparedStatement getByIdStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public UndeadRepositoryFactory() throws SQLException {
    }

    public UndeadRepositoryFactory(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady()) {
            createTables();
        }
        setConnection(connection);
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE "
                        + "Undead(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
                        "name varchar(20) NOT NULL, " + "mana integer, " + "health integer, " + "created_at datetime DEFAULT CURRENT_TIMESTAMP " + "updated_at datatime DEFAULT CURRENT_TIMESTAMP)");
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
                        ("INSERT INTO Undead (name, mana, health, updated_at) VALUES (?, ?, ?, ?)");
        getAllStatement = connection.
                prepareStatement("SELECT * FROM Undead");
        updateStatement = connection.
                prepareStatement("UPDATE Undead SET name = ?, mana = ?, health = ? WHERE id = ?");
        getByIdStatement = connection.
                prepareStatement("SELECT * FROM Undead WHERE id = ?");
        deleteStatement = connection.
                prepareStatement("DELETE FROM Undead WHERE id = ?");
    }

    public static UndeadRepository getInstance() throws SQLException {
        String url = "jdbc:hsqldb:hsql://localhost/workdb";
        return new UndeadRepositoryFactory(DriverManager.getConnection(url));
    }

    @Override
    public int add(Undead undead) {
        try{
            addStatement.setString(1,undead.getName());
            addStatement.setInt(2,undead.getMana());
            addStatement.setInt(3,undead.getHealth());
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
                p.setMana(rs.getInt("mana"));
                p.setHealth(rs.getInt("health"));

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
            updateStatement.setInt(2, newUndead.getMana());
            updateStatement.setInt(3, newUndead.getHealth());
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
                p.setMana(rs.getInt("mana"));
                p.setHealth(rs.getInt("health"));
                undeads.add(p);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return undeads;
    }

}
