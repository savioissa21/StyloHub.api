package com.example.stylohub.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {
    /**
     * Faz upload de uma imagem e retorna a URL pública segura (https).
     *
     * @param file   Arquivo a ser enviado
     * @param folder Pasta no Cloudinary (ex: "profiles", "widgets")
     * @return URL pública da imagem
     */
    String upload(MultipartFile file, String folder);

    /**
     * Remove uma imagem pelo seu publicId do Cloudinary.
     */
    void delete(String publicId);
}
