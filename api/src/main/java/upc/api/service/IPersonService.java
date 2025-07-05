package upc.api.service;

import upc.api.model.Person;
import upc.api.model.Position;
import upc.api.model.User;

import java.util.List;
import java.util.Optional;

public interface IPersonService {
    
    Person savePerson(Person person);
    
    List<Person> getAllPersons();
    
    List<Person> getAllActivePersons();
    
    Optional<Person> getPersonById(Long id);
    
    Optional<Person> getPersonByDni(String dni);
    
    Optional<Person> getPersonByTelefono(String telefono);
    
    Optional<Person> getPersonByUserId(Long userId);
    
    Person updatePerson(Person person);
    
    void deletePerson(Long id);
    
    void softDeletePerson(Long id);
    
    boolean existsByDni(String dni);
    
    List<Person> searchPersons(String searchTerm);
    
    List<Person> getPersonsByPosition(Long positionId);
    
    boolean assignPositionToPerson(Long personId, Long positionId);
    
    boolean removePositionFromPerson(Long personId);
    
    Position getPersonPosition(Long personId);
    
    boolean assignUserToPerson(Long personId, Long userId);
    
    boolean removeUserFromPerson(Long personId);
    
    User getPersonUser(Long personId);
}
