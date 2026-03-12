package com.example.stylohub.adapter.out.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.stylohub.application.port.out.ImageStoragePort;
import com.example.stylohub.domain.exception.DomainValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryAdapter implements ImageStoragePort {

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final java.util.Set<String> ALLOWED_TYPES = java.util.Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final Cloudinary cloudinary;

    public CloudinaryAdapter(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(MultipartFile file, String folder) {
        validateFile(file);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder",          "stylohub/" + folder,
                            "resource_type",   "image",
                            "transformation",  "q_auto,f_auto",
                            "overwrite",       false
                    )
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao fazer upload da imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao remover imagem do Cloudinary: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DomainValidationException("O arquivo de imagem não pode estar vazio.");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new DomainValidationException("A imagem não pode ultrapassar 5 MB.");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new DomainValidationException(
                "Formato de imagem inválido. Aceitos: JPEG, PNG, WebP, GIF."
            );
        }
    }
}
