import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FiltrePasswords extends JFrame {

    private JTextField txtFitxerOrigen;
    private JTextField txtFitxerDesti;
    private JTextArea areaSegures;
    private JTextArea areaNoSegures;
    private JButton btnAnalitzar;
    private JButton btnDesar;
    private String contrasenyesSegures = "";

    public FiltrePasswords() {
        setTitle("Filtre de Seguretat de Passwords");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel panelOrigen = new JPanel(new BorderLayout(5, 0));
        panelOrigen.add(new JLabel("Fitxer origen: "), BorderLayout.WEST);
        txtFitxerOrigen = new JTextField();
        panelOrigen.add(txtFitxerOrigen, BorderLayout.CENTER);
        btnAnalitzar = new JButton("Analitzar");
        panelOrigen.add(btnAnalitzar, BorderLayout.EAST);

        JPanel panelDesti = new JPanel(new BorderLayout(5, 0));
        panelDesti.add(new JLabel("Fitxer destí:   "), BorderLayout.WEST);
        txtFitxerDesti = new JTextField();
        panelDesti.add(txtFitxerDesti, BorderLayout.CENTER);
        btnDesar = new JButton("Desar");
        btnDesar.setEnabled(false);
        panelDesti.add(btnDesar, BorderLayout.EAST);

        panelSuperior.add(panelOrigen);
        panelSuperior.add(panelDesti);

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel panelSegures = new JPanel(new BorderLayout(0, 5));
        panelSegures.add(new JLabel("Contrasenyes Segures"), BorderLayout.NORTH);
        areaSegures = new JTextArea();
        areaSegures.setEditable(false);
        panelSegures.add(new JScrollPane(areaSegures), BorderLayout.CENTER);

        JPanel panelNoSegures = new JPanel(new BorderLayout(0, 5));
        panelNoSegures.add(new JLabel("Contrasenyes No Segures"), BorderLayout.NORTH);
        areaNoSegures = new JTextArea();
        areaNoSegures.setEditable(false);
        panelNoSegures.add(new JScrollPane(areaNoSegures), BorderLayout.CENTER);

        panelCentral.add(panelSegures);
        panelCentral.add(panelNoSegures);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        add(panelPrincipal);

        btnAnalitzar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analitzarFitxer();
            }
        });

        btnDesar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desarFitxer();
            }
        });
    }

    private boolean esSegura(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean teNumero = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                teNumero = true;
                break;
            }
        }

        boolean teMajuscula = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                teMajuscula = true;
                break;
            }
        }

        return teNumero && teMajuscula;
    }

    // ======================================================
    // LECTURA DE FICHERO (FileInputStream)
    // ======================================================
    private void analitzarFitxer() {
        String ruta = txtFitxerOrigen.getText();

        // Creamos un objeto File con la ruta que ha escrito el usuario
        // OJO: esto NO crea un fichero real, solo representa la ruta
        File fitxer = new File(ruta);

        // Comprobamos si el fichero existe de verdad en el disco
        if (!fitxer.exists()) {
            JOptionPane.showMessageDialog(this,
                "El fitxer no existeix!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        areaSegures.setText("");
        areaNoSegures.setText("");
        contrasenyesSegures = "";

        // Declaramos FUERA del try para poder cerrarlo en el finally
        FileInputStream fis = null;

        try {
            // Abrimos un flujo de LECTURA hacia el fichero
            fis = new FileInputStream(fitxer);

            // available() nos dice cuántos bytes tiene el fichero
            int size = fis.available();
            String text = "";

            // Leemos carácter a carácter (lectura secuencial)
            for (int i = 0; i < size; i++) {
                // read() devuelve un int (byte), lo convertimos a char
                char caracter = (char) fis.read();
                text = text + caracter;
            }

            // Dividimos el texto por saltos de línea → cada línea es una contraseña
            String[] linies = text.split("\n");

            for (int i = 0; i < linies.length; i++) {
                String password = linies[i].trim();
                if (password.length() > 0) {
                    if (esSegura(password)) {
                        areaSegures.append(password + "\n");
                        contrasenyesSegures = contrasenyesSegures + password + "\n";
                    } else {
                        areaNoSegures.append(password + "\n");
                    }
                }
            }

            btnDesar.setEnabled(true);

        } catch (IOException e) {
            // Si hay cualquier error de lectura, mostramos popup de error
            JOptionPane.showMessageDialog(this,
                "Error al llegir el fitxer: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // SIEMPRE se ejecuta: cerramos el flujo para liberar recursos
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ======================================================
    // ESCRITURA DE FICHERO (FileOutputStream)
    // ======================================================
    private void desarFitxer() {
        String ruta = txtFitxerDesti.getText();

        if (ruta.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Indica un camí de destí!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Declaramos FUERA del try para poder cerrarlo en el finally
        FileOutputStream fos = null;

        try {
            // Abrimos un flujo de ESCRITURA hacia el fichero
            // Si no existe lo crea, si existe lo sobreescribe
            fos = new FileOutputStream(ruta);

            // Escribimos carácter a carácter (escritura secuencial)
            for (int i = 0; i < contrasenyesSegures.length(); i++) {
                char caracter = contrasenyesSegures.charAt(i);
                // write() espera un byte, por eso hacemos cast (byte)
                fos.write((byte) caracter);
            }

            JOptionPane.showMessageDialog(this,
                "Fitxer desat correctament!", "Èxit",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            // Si hay error de escritura, mostramos popup de error
            JOptionPane.showMessageDialog(this,
                "Error al desar el fitxer: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // SIEMPRE cerramos el flujo
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FiltrePasswords finestra = new FiltrePasswords();
        finestra.setVisible(true);
    }
}