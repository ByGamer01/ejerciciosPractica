import java.io.FileOutputStream;
import java.io.IOException;

public class escribirFrases {
    public static void main(String[] args) {
        FileOutputStream fos = null;
        String text = "Hola, estic aprenent Java!";

        try {
            fos = new FileOutputStream("dades.txt");
            for (int i = 0; i < text.length(); i++) {
                char caracter = text.charAt(i);
                fos.write((byte) caracter);
            }
            System.out.println("Fitxer 'dades.txt' creat i escrit correctament.");
        } catch (IOException e) {
            System.out.println("Error d'escriptura: " + e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.out.println("Error tancant el flux: " + e.getMessage());
            }
        }
    }
}