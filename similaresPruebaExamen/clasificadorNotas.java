import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ClasificadorNotas extends JFrame {

    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextArea areaAprobados;
    private JTextArea areaSuspensos;
    private JButton btnAnalizar;
    private JButton btnGuardar;
    private String contenidoAprobados = "";

    public ClasificadorNotas() {
        setTitle("Clasificador de Notas");
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

        JPanel panelAprobados = new JPanel(new BorderLayout());
        panelAprobados.add(new JLabel("Aprobados (>= 5)"), BorderLayout.NORTH);
        areaAprobados = new JTextArea();
        areaAprobados.setEditable(false);
        panelAprobados.add(new JScrollPane(areaAprobados), BorderLayout.CENTER);

        JPanel panelSuspensos = new JPanel(new BorderLayout());
        panelSuspensos.add(new JLabel("Suspensos (< 5)"), BorderLayout.NORTH);
        areaSuspensos = new JTextArea();
        areaSuspensos.setEditable(false);
        panelSuspensos.add(new JScrollPane(areaSuspensos), BorderLayout.CENTER);

        panelCentral.add(panelAprobados);
        panelCentral.add(panelSuspensos);
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

        areaAprobados.setText("");
        areaSuspensos.setText("");
        contenidoAprobados = "";

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

                // Formato esperado: NombreAlumno;Nota
                int posSeparador = linea.indexOf(';');
                if (posSeparador == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Línea mal formateada: " + linea, "Aviso", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                String nombre = linea.substring(0, posSeparador);
                String notaStr = linea.substring(posSeparador + 1).trim();

                double nota;
                try {
                    nota = Double.parseDouble(notaStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Nota no válida en línea: " + linea, "Aviso", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                String resultado = nombre + " - " + nota;
                if (nota >= 5) {
                    areaAprobados.append(resultado + "\n");
                    contenidoAprobados = contenidoAprobados + resultado + "\n";
                } else {
                    areaSuspensos.append(resultado + "\n");
                }
            }

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
            for (int i = 0; i < contenidoAprobados.length(); i++) {
                fos.write((byte) contenidoAprobados.charAt(i));
            }
            JOptionPane.showMessageDialog(this, "Aprobados guardados correctamente.");
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
        ClasificadorNotas ventana = new ClasificadorNotas();
        ventana.setVisible(true);
    }
}