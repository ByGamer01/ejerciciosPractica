import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class creadorCarpetas {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introdueix el nom de la nova carpeta: ");
        String nomCarpeta = sc.nextLine();

        File carpeta = new File(nomCarpeta);

        if (carpeta.mkdir()) {
            System.out.println("Carpeta '" + nomCarpeta + "' creada correctament.");

            File fitxer = new File(carpeta, "hola.txt");
            try {
                if (fitxer.createNewFile()) {
                    System.out.println("Fitxer 'hola.txt' creat dins la carpeta.");
                } else {
                    System.out.println("El fitxer 'hola.txt' ja existeix.");
                }
            } catch (IOException e) {
                System.out.println("Error creant el fitxer: " + e.getMessage());
            }
        } else {
            System.out.println("No s'ha pogut crear la carpeta. Pot ser que ja existeixi.");
        }

        sc.close();
    }
}