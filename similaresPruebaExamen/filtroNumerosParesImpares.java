import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class filtroNumerosParesImpares extends JFrame {

    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextArea areaPares;
    private JTextArea areaImpares;
    private JLabel lblErrores;
    private JButton btnAnalizar;
    private JButton btnGuardar;
    private String contenidoPares = "";

    public FiltroNumeros() {
        setTitle("Filtro de Números Pares e Impares");
        setSize(700, 520);
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

        JPanel panelPares = new JPanel(new BorderLayout());
        panelPares.add(new JLabel("Números Pares"), BorderLayout.NORTH);
        areaPares = new JTextArea();
        areaPares.setEditable(false);
        panelPares.add(new JScrollPane(areaPares), BorderLayout.CENTER);

        JPanel panelImpares = new JPanel(new BorderLayout());
        panelImpares.add(new JLabel("Números Impares"), BorderLayout.NORTH);
        areaImpares = new JTextArea();
        areaImpares.setEditable(false);
        panelImpares.add(new JScrollPane(areaImpares), BorderLayout.CENTER);

        panelCentral.add(panelPares);
        panelCentral.add(panelImpares);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con contador de errores
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblErrores = new JLabel("Líneas con errores: 0");
        panelInferior.add(lblErrores);
        add(panelInferior, BorderLayout.SOUTH);

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

        areaPares.setText("");
        areaImpares.setText("");
        contenidoPares = "";
        int errores = 0;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fichero);
            int size = fis.available();
            String texto = "";
            for (int i = 0; i < size; i++) {
                char c = (char) fis.read();
                texto = texto + c;
            }

            String[] lineas = texto.split("\n");
            for (int i = 0; i < lineas.length; i++) {
                String linea = lineas[i].trim();
                if (linea.length() == 0) {
                    continue;
                }

                int numero;
                try {
                    numero = Integer.parseInt(linea);
                } catch (NumberFormatException ex) {
                    errores++;
                    continue;
                }

                if (numero % 2 == 0) {
                    areaPares.append(numero + "\n");
                    contenidoPares = contenidoPares + numero + "\n";
                } else {
                    areaImpares.append(numero + "\n");
                }
            }

            lblErrores.setText("Líneas con errores: " + errores);
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

    private void guardar() {
        String ruta = txtDestino.getText().trim();
        if (ruta.length() == 0) {
            JOptionPane.showMessageDialog(this, "Escribe una ruta de destino.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ruta);
            for (int i = 0; i < contenidoPares.length(); i++) {
                fos.write((byte) contenidoPares.charAt(i));
            }
            JOptionPane.showMessageDialog(this, "Números pares guardados correctamente.");
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
        FiltroNumerosParesImpares ventana = new FiltroNumerosParesImpares();
        ventana.setVisible(true);
    }
}