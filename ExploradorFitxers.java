import java.io.File;
import java.util.Scanner;

public class ExploradorFitxers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introdueix una ruta absoluta d'un directori: ");
        String ruta = sc.nextLine();

        File directori = new File(ruta);

        if (directori.exists() && directori.isDirectory()) {
            System.out.println("Contingut del directori: " + directori.getAbsolutePath());
            System.out.println("-------------------------------------------");

            String[] contingut = directori.list();
            for (int i = 0; i < contingut.length; i++) {
                File element = new File(directori, contingut[i]);
                if (element.isDirectory()) {
                    System.out.println("[DIR]  " + contingut[i]);
                } else {
                    System.out.println("[FIT]  " + contingut[i]);
                }
            }
        } else {
            System.out.println("La ruta no existeix o no és un directori.");
        }

        sc.close();
    }
}