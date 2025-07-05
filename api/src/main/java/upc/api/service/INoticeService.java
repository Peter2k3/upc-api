package upc.api.service;

import upc.api.model.Notice;
import upc.api.model.enums.TipoAviso;
import upc.api.model.enums.PrioridadAviso;

import java.util.List;
import java.util.Optional;

public interface INoticeService {
    
    Notice saveNotice(Notice notice);
    
    List<Notice> getAllNotices();
    
    List<Notice> getAllActiveNotices();
    
    Optional<Notice> getNoticeById(Long id);
    
    Notice updateNotice(Notice notice);
    
    void deleteNotice(Long id);
    
    void softDeleteNotice(Long id);
    
    List<Notice> getNoticesByTipo(TipoAviso tipo);
    
    List<Notice> getNoticesByPrioridad(PrioridadAviso prioridad);
    
    List<Notice> getCurrentActiveNotices();
    
    Optional<Notice> getCurrentPinnedNotice();
    
    List<Notice> getNoticesByAutor(Long autorId);
    
    List<Notice> searchNotices(String searchTerm);
    
    List<Notice> getActiveNoticesByType(TipoAviso tipo);
    
    boolean pinNotice(Long id);
    
    boolean unpinNotice(Long id);
    
    boolean activateNotice(Long id);
    
    boolean deactivateNotice(Long id);
}
