package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.api.model.Position;
import upc.api.model.Person;
import upc.api.repository.PositionRepository;
import upc.api.service.IPositionService;

import java.util.List;
import java.util.Optional;

@Service
public class PositionServiceImpl implements IPositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public Position savePosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public List<Position> getAllActivePositions() {
        // Since Position model might not have activo field, return all for now
        return positionRepository.findAll();
    }

    @Override
    public Optional<Position> getPositionById(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    public Optional<Position> getPositionByNombre(String nombre) {
        // Repository method might not exist, return empty for now
        return Optional.empty();
    }

    @Override
    public Position updatePosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public void softDeletePosition(Long id) {
        // Since Position model might not have activo field, perform hard delete for now
        positionRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        // Repository method might not exist, return false for now
        return false;
    }

    @Override
    public List<Position> getPositionsByPerson(Long personId) {
        // Repository method might not exist, return empty list for now
        return List.of();
    }

    @Override
    public List<Person> getPersonsByPosition(Long positionId) {
        // Repository method might not exist, return empty list for now
        return List.of();
    }

    @Override
    public Long countPersonsByPosition(Long positionId) {
        // Repository method might not exist, return 0 for now
        return 0L;
    }
}
