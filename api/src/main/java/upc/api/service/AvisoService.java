package upc.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.api.mapper.TimeUtils;
import upc.api.model.Notice;
import upc.api.repository.AvisosRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvisoService {

    @Autowired
    private AvisosRepository avisosRepository;

    @Transactional
    public void saveAviso(Notice aviso) {
        if (Boolean.TRUE.equals(aviso.getFijado())) {
            changeFijado();
        }
        aviso.setCreationDate(LocalDateTime.now());
        avisosRepository.save(aviso);
    }

    public void changeFijado() { // Ahora cambia cualquier aviso fijado a false
        Optional<Notice> fijado = avisosRepository.findByFijado(true);
        fijado.ifPresent(value -> {
            value.setFijado(false);
            avisosRepository.save(value); // Guardar los cambios
        });
    }

    public Optional<Notice> getFijado() {
        return avisosRepository.findByFijado(true);
    }

    public Optional<Notice> obtenerAviso(Integer idAviso) {
        return avisosRepository.findById(idAviso);
    }

    public List<Notice> getAllNotices(){
        return avisosRepository.findAll();
    }

    @Transactional
    public void desactivarAviso(Integer idAviso) {
        Optional<Notice> aviso = avisosRepository.findById(idAviso);
        aviso.ifPresent(value -> {
            value.setStatus(false);
            avisosRepository.save(value); // Guardar el cambio
        });
    }

    @Transactional
    public void borrarAviso(Integer idAviso) {
        avisosRepository.deleteById(idAviso);
    }
}
