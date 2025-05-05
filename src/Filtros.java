import java.awt.*;
import java.awt.image.BufferedImage;

public class Filtros {

    // Filtro Sepia
    public static BufferedImage aplicarSepia(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage sepiaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(original.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                // Aplicando filtro sepia
                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                // Asegurando que los valores no excedan 255
                tr = Math.min(255, tr);
                tg = Math.min(255, tg);
                tb = Math.min(255, tb);

                sepiaImage.setRGB(x, y, new Color(tr, tg, tb).getRGB());
            }
        }

        return sepiaImage;
    }

    // Filtro Black and White
    public static BufferedImage aplicarBlackAndWhite(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage bwImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(original.getRGB(x, y));
                int gray = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                bwImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }

        return bwImage;
    }

    // Filtro Sharpen
    public static BufferedImage aplicarSharpen(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage sharpenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float[] sharpenKernel = {
                0f, -1f, 0f,
                -1f, 5f,-1f,
                0f, -1f, 0f
        };

        // Aplicando el filtro Sharpen usando un Kernel de 3x3
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float r = 0, g = 0, b = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        Color color = new Color(original.getRGB(x + kx, y + ky));
                        float kernelValue = sharpenKernel[(ky + 1) * 3 + (kx + 1)];
                        r += kernelValue * color.getRed();
                        g += kernelValue * color.getGreen();
                        b += kernelValue * color.getBlue();
                    }
                }
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                sharpenedImage.setRGB(x, y, new Color((int) r, (int) g, (int) b).getRGB());
            }
        }

        return sharpenedImage;
    }
}
