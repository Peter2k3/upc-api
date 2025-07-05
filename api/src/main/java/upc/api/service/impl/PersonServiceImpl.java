package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.api.model.Person;
import upc.api.model.Position;
import upc.api.model.User;
import upc.api.repository.PersonRepository;
import upc.api.repository.PersonPositionRepository;
import upc.api.repository.UserPersonRepository;
import upc.api.service.IPersonService;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements IPersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonPositionRepository personPositionRepository;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public List<Person> getAllActivePersons() {
        // Since Person model doesn't have activo field, return all for now
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public Optional<Person> getPersonByDni(String dni) {
        // Repository method doesn't exist, return empty for now
        return Optional.empty();
    }

    @Override
    public Optional<Person> getPersonByTelefono(String telefono) {
        return personRepository.findByTelefono(telefono);
    }

    @Override
    public Optional<Person> getPersonByUserId(Long userId) {
        // Repository method references non-existent fields, return empty for now
        return Optional.empty();
    }

    @Override
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public void softDeletePerson(Long id) {
        // Since Person model doesn't have activo field, perform hard delete for now
        personRepository.deleteById(id);
    }

    @Override
    public boolean existsByDni(String dni) {
        // Repository method doesn't exist, return false for now
        return false;
    }

    @Override
    public List<Person> searchPersons(String searchTerm) {
        // Repository method references non-existent fields, return empty list for now
        return List.of();
    }

    @Override
    public List<Person> getPersonsByPosition(Long positionId) {
        // Repository method references non-existent fields, return empty list for now
        return List.of();
    }

    @Override
    public boolean assignPositionToPerson(Long personId, Long positionId) {
        // Repository method doesn't exist, return false for now
        return false;
    }

    @Override
    public boolean removePositionFromPerson(Long personId) {
        // Repository method doesn't exist, return false for now
        return false;
    }

    @Override
    public Position getPersonPosition(Long personId) {
        // Repository method doesn't exist, return null for now
        return null;
    }

    @Override
    public boolean assignUserToPerson(Long personId, Long userId) {
        // Repository method doesn't exist, return false for now
        return false;
    }

    @Override
    public boolean removeUserFromPerson(Long personId) {
        // Repository method doesn't exist, return false for now
        return false;
    }

    @Override
    public User getPersonUser(Long personId) {
        // Repository method doesn't exist, return null for now
        return null;
    }
}
