import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IO {
    // Lectura de un archivo
    // Esta parte sacada del git
    public List<String> leerArchivo() throws FileNotFoundException, IOException {
        // la ruta evidentemente cambiara dependiendo tu computadora
        // para este caso, copiare el archivo .txt dentro de la ruta del proyecto
        String pathname = "C:\\Users\\R_Isa_Avi\\IdeaProjects\\DescargayProces\\src\\URLs";
        String filename = "urls.txt";
        File file = new File(pathname + "\\" + filename);
        // Aquí ya tienes la ruta completa, no necesitas agregar "filename" de nuevo

        // Creamos el FileReader para leer el archivo
        FileReader fileReader = new FileReader(file);
        BufferedReader buff = new BufferedReader(fileReader);

        // Lo leemos línea por línea
        List<String> urls = new ArrayList<>();
        String line;
        while ((line = buff.readLine()) != null) {
            line = line.trim(); // Quitamos los espacios al final
            if (!line.isEmpty()) {
                urls.add(line); // Añadimos las líneas no vacías a la lista
            }
        }

        System.out.println(urls);

        // Cerramos la lectura y el archivo
        buff.close();
        fileReader.close();

        return urls; // Devolvemos la lista para usarla después
    }

    // Escritura en un archivo
    public static void escribirArchivo(BufferedImage imagenFiltro, String nombre, int filtro, String extension) {
        try {
            // Crear la carpeta si no existe
            File carpeta = new File("imagenes_filtradas");
            if (!carpeta.exists()) {
                boolean creada = carpeta.mkdir();
                if (creada) {
                    System.out.println("Carpeta 'imagenes_filtradas' creada correctamente.");
                } else {
                    System.err.println("No se pudo crear la carpeta.");
                }
            }

            // Aquí construyes la ruta para guardar la imagen
            File imagen = new File(carpeta, nombre + "" + filtro + "." + extension);

            // Guardar la imagen
            ImageIO.write((RenderedImage) imagenFiltro, extension, imagen);
            System.out.println("Imagen añadida con exito");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}