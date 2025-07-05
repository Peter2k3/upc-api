package upc.api.service;

import upc.api.model.Position;
import upc.api.model.Person;

import java.util.List;
import java.util.Optional;

public interface IPositionService {
    
    Position savePosition(Position position);
    
    List<Position> getAllPositions();
    
    List<Position> getAllActivePositions();
    
    Optional<Position> getPositionById(Long id);
    
    Optional<Position> getPositionByNombre(String nombre);
    
    Position updatePosition(Position position);
    
    void deletePosition(Long id);
    
    void softDeletePosition(Long id);
    
    boolean existsByNombre(String nombre);
    
    List<Position> getPositionsByPerson(Long personId);
    
    List<Person> getPersonsByPosition(Long positionId);
    
    Long countPersonsByPosition(Long positionId);
}
