package pl.wojtek.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.wojtek.domain.Animal;
import pl.wojtek.service.AnimalRepository;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@RestController
public class AnimalApi {

    @Autowired
    AnimalRepository animalRepository;

    @RequestMapping("/")
    public String index(){
        return "Works";
    }

    @RequestMapping(value = "/animal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Animal getAnimal(@PathVariable("id") int id) throws SQLException{
        return animalRepository.getById(id);
    }

    @RequestMapping(value = "/animals", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Animal> getAnimals(@RequestParam(value = "filter",required = false) String filter) throws SQLException{
        if(filter!= null) {
            List<Animal> animals = new LinkedList<>();
            for (Animal item : animalRepository.getAll()) {
                if (item.getName().contains(filter)) {
                    animals.add(item);
                }
            }
            return animals;
        }
        else{
            return animalRepository.getAll();
        }
    }

    @RequestMapping(value = "/animal", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addAnimal(@RequestBody Animal item){
        return new Long(animalRepository.add(item));
    }

    @RequestMapping(value = "/animal/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long deleteAnimal(@PathVariable("id") int id) throws SQLException{
        return new Long(animalRepository.delete(animalRepository.getById(id)));
    }

    @RequestMapping(value = "/animal/{id}", method = RequestMethod.PUT)
    public Long updateAccount(@PathVariable("id") int id, @RequestBody Animal a) throws SQLException {
        return new Long(animalRepository.update(id, a));
    }

}
