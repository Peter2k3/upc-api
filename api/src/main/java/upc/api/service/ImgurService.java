package upc.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImgurService {

    @Value("${imgur.client.id}")
    private String clientId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sube una imagen a Imgur y devuelve la URL
     */
    public String uploadImage(MultipartFile file) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost("https://api.imgur.com/3/image");

            // Agregar la cabecera de autorización con el Client-ID
            uploadFile.setHeader("Authorization", "Client-ID " + clientId);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("image", new ByteArrayBody(file.getBytes(), file.getOriginalFilename()));
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);

                JsonNode jsonResponse = objectMapper.readTree(responseString);
                if (jsonResponse.get("success").asBoolean()) {
                    // Devolver la URL directa de la imagen
                    return jsonResponse.get("data").get("link").asText();
                } else {
                    throw new IOException("Error al subir la imagen a Imgur: " +
                        jsonResponse.get("data").asText());
                }
            }
        }
    }
}
