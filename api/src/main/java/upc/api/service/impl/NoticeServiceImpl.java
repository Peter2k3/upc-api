package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.api.model.Notice;
import upc.api.model.enums.TipoAviso;
import upc.api.model.enums.PrioridadAviso;
import upc.api.repository.NoticeRepository;
import upc.api.service.INoticeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public Notice saveNotice(Notice notice) {
        if (notice.getCreatedAt() == null) {
            notice.setCreatedAt(LocalDateTime.now());
        }
        notice.setUpdatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getAllActiveNotices() {
        return noticeRepository.findAll(); // Implementación básica
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    @Override
    public Notice updateNotice(Notice notice) {
        notice.setUpdatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public void softDeleteNotice(Long id) {
        // Implementación básica
        deleteNotice(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getNoticesByTipo(TipoAviso tipo) {
        return noticeRepository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getNoticesByPrioridad(PrioridadAviso prioridad) {
        return noticeRepository.findByPrioridad(prioridad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getCurrentActiveNotices() {
        return noticeRepository.findCurrentActiveNoticesOrderedByPriority(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notice> getCurrentPinnedNotice() {
        return noticeRepository.findCurrentPinnedNotice(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getNoticesByAutor(Long autorId) {
        return noticeRepository.findByAutorId(autorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> searchNotices(String searchTerm) {
        return noticeRepository.searchNotices(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getActiveNoticesByType(TipoAviso tipo) {
        return noticeRepository.findActiveNoticesByType(tipo, LocalDateTime.now());
    }

    @Override
    public boolean pinNotice(Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()) {
            Notice n = notice.get();
            n.setFijado(true);
            n.setUpdatedAt(LocalDateTime.now());
            noticeRepository.save(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean unpinNotice(Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()) {
            Notice n = notice.get();
            n.setFijado(false);
            n.setUpdatedAt(LocalDateTime.now());
            noticeRepository.save(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateNotice(Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()) {
            Notice n = notice.get();
            n.setActivo(true);
            n.setUpdatedAt(LocalDateTime.now());
            noticeRepository.save(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateNotice(Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()) {
            Notice n = notice.get();
            n.setActivo(false);
            n.setUpdatedAt(LocalDateTime.now());
            noticeRepository.save(n);
            return true;
        }
        return false;
    }
}
