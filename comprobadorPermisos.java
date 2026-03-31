import java.io.File;
import java.util.Scanner;

public class comprobadorPermisos {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introdueix el nom d'un fitxer o directori: ");
        String nom = sc.nextLine();

        File fitxer = new File(nom);

        if (fitxer.exists()) {
            System.out.println("=== Fitxa Tècnica ===");

            // Comprovar si és fitxer o directori
            if (fitxer.isFile()) {
                System.out.println("Tipus: Fitxer");
            } else if (fitxer.isDirectory()) {
                System.out.println("Tipus: Directori");
            }

            // Permisos de lectura
            if (fitxer.canRead()) {
                System.out.println("Lectura: Sí");
            } else {
                System.out.println("Lectura: No");
            }

            // Permisos d'escriptura
            if (fitxer.canWrite()) {
                System.out.println("Escriptura: Sí");
            } else {
                System.out.println("Escriptura: No");
            }

            // Ruta absoluta
            System.out.println("Ruta absoluta: " + fitxer.getAbsolutePath());
        } else {
            System.out.println("El fitxer o directori no existeix.");
        }

        sc.close();
    }
}