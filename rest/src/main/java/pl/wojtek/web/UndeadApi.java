package pl.wojtek.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.wojtek.domain.Undead;
import pl.wojtek.service.UndeadRepository;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@RestController
public class UndeadApi {

    @Autowired
    UndeadRepository undeadRepository;

    @RequestMapping("/")
    public String index(){
        return "Works";
    }

    @RequestMapping(value = "/undead/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Undead getUndead(@PathVariable("id") int id) throws SQLException{
        return undeadRepository.getById(id);
    }

    @RequestMapping(value = "/undeads", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Undead> getUndeads(@RequestParam(value = "filter",required = false) String filter) throws SQLException{
        if(filter!= null) {
            List<Undead> undeads = new LinkedList<>();
            for (Undead item : undeadRepository.getAll()) {
                if (item.getName().contains(filter)) {
                    undeads.add(item);
                }
            }
            return undeads;
        }
        else{
            return undeadRepository.getAll();
        }
    }

    @RequestMapping(value = "/undead", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addUndead(@RequestBody Undead item){
        return new Long(undeadRepository.add(item));
    }

    @RequestMapping(value = "/undead/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long deleteUndead(@PathVariable("id") int id) throws SQLException{
        return new Long(undeadRepository.delete(undeadRepository.getById(id)));
    }

    @RequestMapping(value = "/undead/{id}", method = RequestMethod.PUT)
    public Long updateAccount(@PathVariable("id") int id, @RequestBody Undead a) throws SQLException {
        return new Long(undeadRepository.update(id, a));
    }

}
