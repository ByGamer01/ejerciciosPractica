import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ContadorPalabras extends JFrame {

    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextArea areaLargas;
    private JTextArea areaCortas;
    private JLabel lblContadorLargas;
    private JLabel lblContadorCortas;
    private JButton btnAnalizar;
    private JButton btnGuardar;
    private String contenidoLargas = "";

    public ContadorPalabras() {
        setTitle("Contador de Palabras Largas");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));

        // Fila origen
        JPanel filaOrigen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaOrigen.add(new JLabel("Fichero origen:"));
        txtOrigen = new JTextField(30);
        filaOrigen.add(txtOrigen);
        btnAnalizar = new JButton("Analizar");
        filaOrigen.add(btnAnalizar);
        panelSuperior.add(filaOrigen);

        // Fila destino
        JPanel filaDestino = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaDestino.add(new JLabel("Fichero destino:"));
        txtDestino = new JTextField(30);
        filaDestino.add(txtDestino);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setEnabled(false);
        filaDestino.add(btnGuardar);
        panelSuperior.add(filaDestino);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelLargas = new JPanel(new BorderLayout());
        lblContadorLargas = new JLabel("Palabras largas (>= 6 letras): 0");
        panelLargas.add(lblContadorLargas, BorderLayout.NORTH);
        areaLargas = new JTextArea();
        areaLargas.setEditable(false);
        panelLargas.add(new JScrollPane(areaLargas), BorderLayout.CENTER);

        JPanel panelCortas = new JPanel(new BorderLayout());
        lblContadorCortas = new JLabel("Palabras cortas (< 6 letras): 0");
        panelCortas.add(lblContadorCortas, BorderLayout.NORTH);
        areaCortas = new JTextArea();
        areaCortas.setEditable(false);
        panelCortas.add(new JScrollPane(areaCortas), BorderLayout.CENTER);

        panelCentral.add(panelLargas);
        panelCentral.add(panelCortas);
        add(panelCentral, BorderLayout.CENTER);

        // Listeners
        btnAnalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analizar();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
    }

    private void analizar() {
        String ruta = txtOrigen.getText().trim();
        File fichero = new File(ruta);

        if (!fichero.exists()) {
            JOptionPane.showMessageDialog(this, "El fichero no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        areaLargas.setText("");
        areaCortas.setText("");
        contenidoLargas = "";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fichero);
            int size = fis.available();
            String texto = "";
            for (int i = 0; i < size; i++) {
                char c = (char) fis.read();
                texto = texto + c;
            }

            // Separar por espacios, saltos de línea, tabuladores, puntuación básica
            String[] palabras = texto.split("[\\s,;.!?()\\[\\]\"]+");

            // Para evitar repetidas usamos una comprobación manual
            String largasUnicas = "";
            String cortasUnicas = "";
            int contLargas = 0;
            int contCortas = 0;

            for (int i = 0; i < palabras.length; i++) {
                String palabra = palabras[i].trim();
                if (palabra.length() == 0) {
                    continue;
                }

                if (palabra.length() >= 6) {
                    // Comprobar si ya está añadida
                    if (!yaExiste(largasUnicas, palabra)) {
                        largasUnicas = largasUnicas + palabra + "\n";
                        contLargas++;
                    }
                } else {
                    if (!yaExiste(cortasUnicas, palabra)) {
                        cortasUnicas = cortasUnicas + palabra + "\n";
                        contCortas++;
                    }
                }
            }

            areaLargas.setText(largasUnicas);
            areaCortas.setText(cortasUnicas);
            contenidoLargas = largasUnicas;
            lblContadorLargas.setText("Palabras largas (>= 6 letras): " + contLargas);
            lblContadorCortas.setText("Palabras cortas (< 6 letras): " + contCortas);

            btnGuardar.setEnabled(true);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean yaExiste(String lista, String palabra) {
        String[] lineas = lista.split("\n");
        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].trim().equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    private void guardar() {
        String ruta = txtDestino.getText().trim();
        if (ruta.length() == 0) {
            JOptionPane.showMessageDialog(this, "Escribe una ruta de destino.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ruta);
            for (int i = 0; i < contenidoLargas.length(); i++) {
                fos.write((byte) contenidoLargas.charAt(i));
            }
            JOptionPane.showMessageDialog(this, "Palabras largas guardadas correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ContadorPalabras ventana = new ContadorPalabras();
        ventana.setVisible(true);
    }
}