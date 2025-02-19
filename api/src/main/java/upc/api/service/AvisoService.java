package upc.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.api.mapper.TimeUtils;
import upc.api.model.Aviso;
import upc.api.repository.AvisosRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AvisoService {

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    AvisosRepository avisosRepository;

    @Transactional
    public void saveAviso(Aviso aviso){
        if (aviso.isFijado(true)) changeFijado(aviso);
        aviso.setFechaCreacion(LocalDateTime.now());
        avisosRepository.save(aviso);
    }

    public void changeFijado(Aviso aviso){ //Para cambiar el que este fijado a false
        Optional<Aviso> fijado = avisosRepository.findByFijado(true);
        fijado.ifPresent(value -> value.setFijado(false));
    }

    public Optional<Aviso> getFijado(){
        return avisosRepository.findByFijado(true);
    }

    public Optional<Aviso> obtenerAviso(Integer idAviso){
        return avisosRepository.findById(idAviso);
    }

    public void desactivarAviso(Integer idAviso){
        Optional<Aviso> aviso = avisosRepository.findById(idAviso);
        aviso.ifPresent(value -> value.setStatus(false));
    }
}
