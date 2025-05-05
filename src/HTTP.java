import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import java.util.List;

public class HTTP {

    public void leerImagen() {
        //1.1 Hay que leer el archivo usando for each
        // Meter la clase IO
        IO io = new IO();
        //La peticion
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        try {
            //Pasamos lo que tenemos en la lista de IO aqui
            List<String> urls = io.leerArchivo();
            int[] contador = {0};

            List<CompletableFuture<Void>> tareas = new ArrayList<>();
            //Uso del for each
            //Punto 2 de 5 cubierto **
            for (String url : urls) {
                url = url.trim();//elimina espacios

                //Esta cosa es importante
                if (!url.startsWith("http")) {
                    System.out.println("URL inválida");
                    continue;
                }

                int index = contador[0]++;

                HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();

                //este es la descarga Asincrona; esta en el git
                //     ********
                String finalUrl = url;
                CompletableFuture<Void> tarea = client.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray())
                        .thenApply(response -> {
                            if (response.statusCode() != 200) {
                                System.err.println("Error HTTP " + response.statusCode() + " al descargar: " + finalUrl);
                                return null;
                            }
                            return response.body();
                        })
                        .thenAccept(data -> {
                            if (data == null) return; // No proceses si hubo error HTTP
                            try {
                                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                                //El bufferedImage
                                BufferedImage imagen = ImageIO.read(bais);
                                //Lo guardamos por cada filtro
                                BufferedImage sepia = Filtros.aplicarSepia(imagen);
                                IO.escribirArchivo(sepia, "imagenSepia", index, "jpg");

                                BufferedImage bw = Filtros.aplicarBlackAndWhite(imagen);
                                IO.escribirArchivo(bw, "imagenB&N", index, "jpg");

                                BufferedImage sharpen = Filtros.aplicarSharpen(imagen);
                                IO.escribirArchivo(sharpen, "imagenSharpen", index, "jpg");

                                System.out.println("Imagen " + index + " procesada con filtros.");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                tareas.add(tarea);
            }
            //Multiproceso acabado
            CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0])).join();
            System.out.println("Todas las imágenes procesadas.");

            // Esperamos unos segundos para que terminen las descargas asíncronas
            Thread.sleep(1000); // Ajusta según cuántas imágenes descargues
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}