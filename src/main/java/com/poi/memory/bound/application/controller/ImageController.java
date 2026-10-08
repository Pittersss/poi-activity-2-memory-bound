package com.poi.memory.bound.application.controller;

import com.poi.memory.bound.application.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("api/imagens")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/processar", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> processarImagem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("operacao") String operacao) throws IOException {

        BufferedImage imagemOriginal = ImageIO.read(file.getInputStream());

        BufferedImage imagemProcessada = imageService.processar(imagemOriginal, operacao);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagemProcessada, "jpg", baos);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(baos.toByteArray());
    }
}
