package pl.wojtek;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.wojtek.domain.Undead;
import pl.wojtek.repository.UndeadRepository;
import pl.wojtek.repository.UndeadRepositoryFactory;

import java.sql.SQLException;

import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


@Ignore
@RunWith(JUnit4.class)
public class UndeadTest {
    UndeadRepository undeadRepository;

    @Test
    public void testGetById() {
        int findId = undeadRepository.getAll().get(0).getId();
        assertNotNull(undeadRepository.getById(findId));
    }

    @Test
    public void testGetAll() {
        assertNotNull(undeadRepository.getAll());
    }

    @Test
    public void testAdd() {
        Undead skeleton = new Undead();
        skeleton.setId(1);
        skeleton.setMana(5);
        skeleton.setName("Wojownik");
        skeleton.setHealth(10);

        undeadRepository.add(skeleton);
        assertNotNull(undeadRepository.getById(skeleton.getId()));
        assertThat(skeleton.getId(), is(1));
        assertThat(skeleton.getName(), containsString("wnik"));
        assertEquals(5, skeleton.getMana());
        assertEquals(10, skeleton.getHealth());

    }


    @Test
    public void testDelete() throws SQLException {
        Undead skeleton = undeadRepository.getById(0);
        undeadRepository.delete(skeleton);

        if (undeadRepository.getAll().size() > 0) {
            assertNotNull(undeadRepository.getAll());
        }

        assertNull(undeadRepository.getById(skeleton.getId()).getName());

    }
    @Test
    public void testUpdate() throws SQLException {
        Undead undeadTest = undeadRepository.getById(3);
        Undead ghoul = new Undead();
        ghoul.setMana(10);
        ghoul.setName("Mag");
        ghoul.setHealth(10);
        int undeadToUpdate = undeadRepository.getAll().get(0).getId();

        assertEquals(1, undeadRepository.update(undeadToUpdate, ghoul));

        assertEquals(undeadRepository.getById(undeadToUpdate).getName(), ghoul.getName());

    }

    @Before
    public void initRepository() throws SQLException {
        undeadRepository = UndeadRepositoryFactory.getInstance();

        Undead ghoul = new Undead();
        Undead zombie = new Undead();

        ghoul.setId(1);
        ghoul.setMana(10);
        ghoul.setName("Mag");
        ghoul.setHealth(10);

        zombie.setId(2);
        zombie.setMana(5);
        zombie.setName("Zjadacz");
        zombie.setHealth(15);

        undeadRepository.add(ghoul);
        undeadRepository.add(zombie);
    }


}
