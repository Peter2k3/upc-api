package upc.api.service;

import upc.api.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IImageService {
    
    Image saveImage(Image image);
    
    List<Image> getAllImages();
    
    List<Image> getAllActiveImages();
    
    Optional<Image> getImageById(Long id);
    
    Optional<Image> getImageByNombreArchivo(String nombreArchivo);
    
    Optional<Image> getImageByIdStrapi(String idStrapi);
    
    Image updateImage(Image image);
    
    void deleteImage(Long id);
    
    void softDeleteImage(Long id);
    
    List<Image> getImagesByTipo(String tipo);
    
    List<Image> getImagesByBlogPost(Long blogPostId);
    
    Long getImageUsageCount(Long imageId);
    
    // Métodos para integración con Strapi
    Image uploadImageToStrapi(MultipartFile file, String altText, String tipo);
    
    boolean deleteImageFromStrapi(String idStrapi);
    
    String getImageUrlFromStrapi(String idStrapi);
    
    // Métodos para procesamiento de imágenes en contenido JSON
    String extractImageIdsFromContent(String content);
    
    String replaceImageIdsWithUrls(String content);
}
