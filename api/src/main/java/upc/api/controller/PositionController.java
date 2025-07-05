package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Position;
import upc.api.service.IPositionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "*")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        List<Position> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Position>> getAllActivePositions() {
        List<Position> positions = positionService.getAllActivePositions();
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Optional<Position> position = positionService.getPositionById(id);
        return position.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position savedPosition = positionService.savePosition(position);
        return ResponseEntity.ok(savedPosition);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @RequestBody Position position) {
        Optional<Position> existingPosition = positionService.getPositionById(id);
        if (existingPosition.isPresent()) {
            position.setId(id);
            Position updatedPosition = positionService.updatePosition(position);
            return ResponseEntity.ok(updatedPosition);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
