package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Notice;
import upc.api.model.enums.TipoAviso;
import upc.api.service.INoticeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<Notice>> getCurrentActiveNotices() {
        List<Notice> notices = noticeService.getCurrentActiveNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        Optional<Notice> notice = noticeService.getNoticeById(id);
        return notice.map(n -> new ResponseEntity<>(n, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pinned")
    public ResponseEntity<Notice> getPinnedNotice() {
        Optional<Notice> notice = noticeService.getCurrentPinnedNotice();
        return notice.map(n -> new ResponseEntity<>(n, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-type/{tipo}")
    public ResponseEntity<List<Notice>> getNoticesByType(@PathVariable TipoAviso tipo) {
        List<Notice> notices = noticeService.getActiveNoticesByType(tipo);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Notice>> searchNotices(@RequestParam String q) {
        List<Notice> notices = noticeService.searchNotices(q);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_NOTICES')")
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        try {
            Notice savedNotice = noticeService.saveNotice(notice);
            return new ResponseEntity<>(savedNotice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_NOTICES')")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        Optional<Notice> existingNotice = noticeService.getNoticeById(id);
        if (existingNotice.isPresent()) {
            notice.setId(id);
            Notice updatedNotice = noticeService.updateNotice(notice);
            return new ResponseEntity<>(updatedNotice, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_NOTICES')")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        Optional<Notice> notice = noticeService.getNoticeById(id);
        if (notice.isPresent()) {
            noticeService.softDeleteNotice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/pin")
    @PreAuthorize("hasAuthority('PIN_NOTICES')")
    public ResponseEntity<Void> pinNotice(@PathVariable Long id) {
        boolean pinned = noticeService.pinNotice(id);
        return pinned ? new ResponseEntity<>(HttpStatus.OK) 
                      : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/unpin")
    @PreAuthorize("hasAuthority('PIN_NOTICES')")
    public ResponseEntity<Void> unpinNotice(@PathVariable Long id) {
        boolean unpinned = noticeService.unpinNotice(id);
        return unpinned ? new ResponseEntity<>(HttpStatus.OK) 
                        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ACTIVATE_NOTICES')")
    public ResponseEntity<Void> activateNotice(@PathVariable Long id) {
        boolean activated = noticeService.activateNotice(id);
        return activated ? new ResponseEntity<>(HttpStatus.OK) 
                         : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ACTIVATE_NOTICES')")
    public ResponseEntity<Void> deactivateNotice(@PathVariable Long id) {
        boolean deactivated = noticeService.deactivateNotice(id);
        return deactivated ? new ResponseEntity<>(HttpStatus.OK) 
                           : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('READ_ALL_NOTICES')")
    public ResponseEntity<List<Notice>> getAllNoticesForAdmin() {
        List<Notice> notices = noticeService.getAllActiveNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }
}
