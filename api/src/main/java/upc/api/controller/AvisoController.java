package upc.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Aviso;
import upc.api.service.AvisoService;

import java.util.Optional;

@RestController
@RequestMapping("/upc/v1/avisos")
public class AvisoController {

    @Autowired
    AvisoService avisoService;

    @PostMapping
    public void saveAviso(@RequestBody Aviso aviso){//Este endpoind editara un aviso o creará uno nueno si no tiene id
        avisoService.saveAviso(aviso);
    };

    @GetMapping
    public Optional<Aviso> getFijado(){
        return avisoService.getFijado();
    }

    @PutMapping
    public ResponseEntity<?> actualizarAviso(Aviso aviso) throws BadRequestException {

        if (aviso.getId() == null){
            return ResponseEntity.badRequest().body("Debe incluir el id del aviso");
        }
        Optional<Aviso> aviso1 = avisoService.obtenerAviso(aviso.getId());

        if (aviso1.isPresent()){
            avisoService.saveAviso(aviso);
        }
        return ResponseEntity.badRequest().body("Aviso inexistente");
    }


}
