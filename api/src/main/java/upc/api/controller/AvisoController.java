package upc.api.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Notice;
import upc.api.service.AvisoService;

import java.util.Optional;

@RestController
@RequestMapping("/upc/v1/notices")
public class AvisoController {

    @Autowired
    AvisoService avisoService;

    @PostMapping
    public void saveAviso(@RequestBody Notice aviso){//Este endpoind editara un aviso o creará uno nueno si no tiene id
        avisoService.saveAviso(aviso);
    }

    @GetMapping
    public Optional<Notice> getFijado(){
        return avisoService.getFijado();
    }

    @PutMapping
    public ResponseEntity<?> actualizarAviso(@RequestBody Notice aviso) throws BadRequestException {
        if (aviso.getId() == null) {
            return ResponseEntity.badRequest().body("The id must not be null");
        }
        Optional<Notice> aviso1 = avisoService.obtenerAviso(aviso.getId());

        if (aviso1.isPresent()) {
            avisoService.saveAviso(aviso);
            return ResponseEntity.ok("Updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idAviso}")
    public void borrarAviso(@PathVariable Integer idAviso) {
        avisoService.borrarAviso(idAviso);
    }
}
