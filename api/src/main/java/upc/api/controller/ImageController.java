package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.api.model.Image;
import upc.api.service.IImageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_IMAGES')")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllActiveImages();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Optional<Image> image = imageService.getImageById(id);
        return image.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-type/{tipo}")
    @PreAuthorize("hasAuthority('READ_IMAGES')")
    public ResponseEntity<List<Image>> getImagesByType(@PathVariable String tipo) {
        List<Image> images = imageService.getImagesByTipo(tipo);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('UPLOAD_IMAGES')")
    public ResponseEntity<Image> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "tipo", defaultValue = "general") String tipo) {
        try {
            Image uploadedImage = imageService.uploadImageToStrapi(file, altText, tipo);
            return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_IMAGES')")
    public ResponseEntity<Image> updateImage(@PathVariable Long id, @RequestBody Image image) {
        Optional<Image> existingImage = imageService.getImageById(id);
        if (existingImage.isPresent()) {
            image.setId(id);
            Image updatedImage = imageService.updateImage(image);
            return new ResponseEntity<>(updatedImage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_IMAGES')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        Optional<Image> image = imageService.getImageById(id);
        if (image.isPresent()) {
            imageService.softDeleteImage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/usage-count")
    @PreAuthorize("hasAuthority('READ_IMAGES')")
    public ResponseEntity<Long> getImageUsageCount(@PathVariable Long id) {
        Long count = imageService.getImageUsageCount(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/blog-post/{blogPostId}")
    public ResponseEntity<List<Image>> getImagesByBlogPost(@PathVariable Long blogPostId) {
        List<Image> images = imageService.getImagesByBlogPost(blogPostId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }
}
