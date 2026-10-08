package com.poi.memory.bound.application.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.stream.IntStream;

@Service
public class ImageService {
    public BufferedImage processar(BufferedImage original, String operacao) {
        BufferedImage imagemSaida = new BufferedImage(
                original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        imagemSaida.getGraphics().drawImage(original, 0, 0, null);

        int[] pixels = ((DataBufferInt) imagemSaida.getRaster().getDataBuffer()).getData();

        if ("cinza".equalsIgnoreCase(operacao)) {
            aplicarFiltroCinzaParalelo(pixels);
        } else if ("inverter".equalsIgnoreCase(operacao)) {
            aplicarFiltroInversaoParalelo(pixels);
        }

        return imagemSaida;
    }

    private void aplicarFiltroCinzaParalelo(int[] pixels) {
        IntStream.range(0, pixels.length).parallel().forEach(i -> {
            int rgb = pixels[i];
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

            pixels[i] = (gray << 16) | (gray << 8) | gray;
        });
    }

    private void aplicarFiltroInversaoParalelo(int[] pixels) {
        IntStream.range(0, pixels.length).parallel().forEach(i -> {
            int rgb = pixels[i];
            int r = 255 - ((rgb >> 16) & 0xFF);
            int g = 255 - ((rgb >> 8) & 0xFF);
            int b = 255 - (rgb & 0xFF);

            pixels[i] = (r << 16) | (g << 8) | b;
        });
    }
}
