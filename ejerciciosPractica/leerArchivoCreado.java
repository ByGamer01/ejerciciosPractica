import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class leerArchivoCreado {
    public static void main(String[] args) {
        FileInputStream fis = null;
        String text = "";
        int totalBytes = 0;

        try {
            fis = new FileInputStream("dades.txt");
            int size = fis.available();
            for (int i = 0; i < size; i++) {
                char caracter = (char) fis.read();
                text = text + caracter;
                totalBytes++;
            }
            System.out.println("Contingut del fitxer: " + text);
            System.out.println("Total de bytes: " + totalBytes);

            // Alternativa amb length()
            File fitxer = new File("dades.txt");
            System.out.println("Tamany amb length(): " + fitxer.length() + " bytes");

        } catch (IOException e) {
            System.out.println("Error de lectura: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("Error tancant el flux: " + e.getMessage());
            }
        }
    }
}