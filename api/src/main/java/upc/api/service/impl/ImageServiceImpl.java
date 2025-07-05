package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upc.api.model.Image;
import upc.api.repository.ImageRepository;
import upc.api.service.IImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageServiceImpl implements IImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        if (image.getCreatedAt() == null) {
            image.setCreatedAt(LocalDateTime.now());
        }
        return imageRepository.save(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getAllActiveImages() {
        return imageRepository.findAll(); // Implementación básica
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> getImageByNombreArchivo(String nombreArchivo) {
        return imageRepository.findByNombreArchivo(nombreArchivo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> getImageByIdStrapi(String idStrapi) {
        return imageRepository.findByIdStrapi(idStrapi);
    }

    @Override
    public Image updateImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public void softDeleteImage(Long id) {
        // Implementación básica
        deleteImage(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImagesByTipo(String tipo) {
        // No hay campo tipo en Image, devuelve todas
        return getAllImages();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImagesByBlogPost(Long blogPostId) {
        return imageRepository.findImagesByBlogPostId(blogPostId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getImageUsageCount(Long imageId) {
        return imageRepository.countUsageInBlogPosts(imageId);
    }

    @Override
    public Image uploadImageToStrapi(MultipartFile file, String altText, String tipo) {
        // Implementación básica - integración con Strapi se puede implementar después
        Image image = new Image();
        image.setNombreOriginal(file.getOriginalFilename());
        image.setNombreArchivo(file.getOriginalFilename());
        image.setUrl("/api/images/placeholder/" + file.getOriginalFilename());
        image.setMimeType(file.getContentType());
        image.setTamanoBytes(file.getSize());
        image.setAltText(altText);
        image.setCreatedAt(LocalDateTime.now());
        return saveImage(image);
    }

    @Override
    public boolean deleteImageFromStrapi(String idStrapi) {
        // Implementación básica
        return true;
    }

    @Override
    public String getImageUrlFromStrapi(String idStrapi) {
        // Implementación básica
        return "/api/images/strapi/" + idStrapi;
    }

    @Override
    public String extractImageIdsFromContent(String content) {
        // Implementación básica - extraer IDs de imágenes del contenido JSON
        return content;
    }

    @Override
    public String replaceImageIdsWithUrls(String content) {
        // Implementación básica - reemplazar IDs con URLs reales
        return content;
    }
}
