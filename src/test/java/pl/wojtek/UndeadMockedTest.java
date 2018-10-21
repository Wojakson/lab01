package pl.wojtek;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.wojtek.domain.Undead;
import pl.wojtek.repository.UndeadRepository;
import pl.wojtek.repository.UndeadRepositoryFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UndeadMockedTest {

    UndeadRepository undeadRepository;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement addStatement;
    @Mock
    private PreparedStatement getAllStatement;
    @Mock
    private PreparedStatement getByIdStatement;
    @Mock
    private PreparedStatement updateStatement;
    @Mock
    private PreparedStatement deleteStatement;

    @Mock
    ResultSet resultSet;

    @Before
    public void initRepository() throws SQLException {
        when(connectionMock.prepareStatement("INSERT INTO Undead (name, mana, health) VALUES (?, ?, ?)")).thenReturn(addStatement);
        when(connectionMock.prepareStatement("SELECT id, name, mana, health FROM Undead")).thenReturn(getAllStatement);
        when(connectionMock.prepareStatement("UPDATE Undead SET name = ?, mana = ?, health = ? WHERE id = ?")).thenReturn(updateStatement);
        when(connectionMock.prepareStatement("SELECT id, name, mana, health FROM Undead WHERE id = ?")).thenReturn(getByIdStatement);
        when(connectionMock.prepareStatement("DELETE FROM Undead WHERE id = ?")).thenReturn(deleteStatement);
        undeadRepository = new UndeadRepositoryFactory();
        undeadRepository.setConnection(connectionMock);
        verify(connectionMock).prepareStatement("INSERT INTO Undead (name, mana, health) VALUES (?, ?, ?)");
        verify(connectionMock).prepareStatement("SELECT id, name, mana, health FROM Undead");
        verify(connectionMock).prepareStatement("UPDATE Undead SET name = ?, mana = ?, health = ? WHERE id = ?");
        verify(connectionMock).prepareStatement("SELECT id, name, mana, health FROM Undead WHERE id = ?");
        verify(connectionMock).prepareStatement("DELETE FROM Undead WHERE id = ?");
    }

    @Test
    public void addUndeadTest() throws Exception {
        when(addStatement.executeUpdate()).thenReturn(1);
        Undead skeleton = new Undead();
        skeleton.setId(1);
        skeleton.setMana(5);
        skeleton.setName("Wojownik");
        skeleton.setHealth(10);

        assertEquals(1, undeadRepository.add(skeleton));

        verify(addStatement, times(1)).setString(1, "Wojownik");
        verify(addStatement, times(1)).setInt(2, 5);
        verify(addStatement, times(1)).setInt(3, 10);
        verify(addStatement).executeUpdate();
    }

    @Test
    public void deleteUndeadTest() throws Exception {
        when(deleteStatement.executeUpdate()).thenReturn(1);
        Undead skeleton = new Undead();
        skeleton.setId(1);
        skeleton.setMana(5);
        skeleton.setName("Wojownik");
        skeleton.setHealth(10);
        assertEquals(1, undeadRepository.delete(skeleton));
        verify(deleteStatement, times(1)).setInt(1, skeleton.getId());
        verify(deleteStatement).executeUpdate();
    }


    abstract class AbstractResultSet implements ResultSet {
        int i = 0;

        @Override
        public boolean next() throws SQLException {
            if (i == 1)
                return false;

            i++;
            return true;
        }

        @Override
        public int getInt(String id) throws SQLException {
            return 1;
        }

        @Override
        public String getString(String columnLabel) throws SQLException {
            return "Wojownik";
        }

    }

    @Test
    public void getAllUndeadsTest() throws Exception {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("name")).thenCallRealMethod();
        when(mockedResultSet.getInt("mana")).thenCallRealMethod();
        when(mockedResultSet.getInt("health")).thenCallRealMethod();
        when(getAllStatement.executeQuery()).thenReturn(mockedResultSet);

        assertEquals(1, undeadRepository.getAll().size());

        verify(getAllStatement, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("name");
        verify(mockedResultSet, times(1)).getInt("mana");
        verify(mockedResultSet, times(1)).getInt("health");
        verify(mockedResultSet, times(2)).next();
    }

    @Test
    public void getByIdTest() throws SQLException {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("name")).thenCallRealMethod();
        when(mockedResultSet.getInt("mana")).thenCallRealMethod();
        when(mockedResultSet.getInt("health")).thenCallRealMethod();
        when(getByIdStatement.executeQuery()).thenReturn(mockedResultSet);

        assertNotNull(undeadRepository.getById(1));

        verify(getByIdStatement, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("name");
        verify(mockedResultSet, times(1)).getInt("mana");
        verify(mockedResultSet, times(1)).getInt("health");
        verify(mockedResultSet, times(2)).next();
    }

    @Test
    public void updateAnimalTest() throws SQLException {
        when(updateStatement.executeUpdate()).thenReturn(1);
        Undead skeleton = new Undead();
        skeleton.setId(1);
        skeleton.setMana(5);
        skeleton.setName("Wojownik");
        skeleton.setHealth(10);

        Undead undeadUpdated = new Undead();
        undeadUpdated.setId(skeleton.getId());
        undeadUpdated.setName("Zabojca");
        undeadUpdated.setMana(skeleton.getMana());
        undeadUpdated.setHealth(skeleton.getHealth());

        assertEquals(1, undeadRepository.update(skeleton.getId(), undeadUpdated));
        verify(updateStatement).executeUpdate();


    }


}
