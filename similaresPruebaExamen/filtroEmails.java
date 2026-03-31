import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FiltroEmails extends JFrame {

    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextArea areaValidos;
    private JTextArea areaNoValidos;
    private JButton btnAnalizar;
    private JButton btnGuardar;
    private String contenidoValidos = "";

    public FiltroEmails() {
        setTitle("Filtro de Emails Válidos");
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

        // Panel central con las dos áreas de texto
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelValidos = new JPanel(new BorderLayout());
        panelValidos.add(new JLabel("Emails Válidos"), BorderLayout.NORTH);
        areaValidos = new JTextArea();
        areaValidos.setEditable(false);
        panelValidos.add(new JScrollPane(areaValidos), BorderLayout.CENTER);

        JPanel panelNoValidos = new JPanel(new BorderLayout());
        panelNoValidos.add(new JLabel("Emails No Válidos"), BorderLayout.NORTH);
        areaNoValidos = new JTextArea();
        areaNoValidos.setEditable(false);
        panelNoValidos.add(new JScrollPane(areaNoValidos), BorderLayout.CENTER);

        panelCentral.add(panelValidos);
        panelCentral.add(panelNoValidos);
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

        areaValidos.setText("");
        areaNoValidos.setText("");
        contenidoValidos = "";

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
                String email = lineas[i].trim();
                if (email.length() == 0) {
                    continue;
                }
                if (esEmailValido(email)) {
                    areaValidos.append(email + "\n");
                    contenidoValidos = contenidoValidos + email + "\n";
                } else {
                    areaNoValidos.append(email + "\n");
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

    private boolean esEmailValido(String email) {
        // Debe contener exactamente un @
        int contadorArroba = 0;
        int posArroba = -1;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                contadorArroba++;
                posArroba = i;
            }
        }
        if (contadorArroba != 1) {
            return false;
        }
        // Al menos un carácter antes del @
        if (posArroba == 0) {
            return false;
        }
        // Debe contener un punto después del @
        String despuesArroba = email.substring(posArroba + 1);
        boolean tienePunto = false;
        for (int i = 0; i < despuesArroba.length(); i++) {
            if (despuesArroba.charAt(i) == '.') {
                tienePunto = true;
            }
        }
        return tienePunto;
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
            for (int i = 0; i < contenidoValidos.length(); i++) {
                fos.write((byte) contenidoValidos.charAt(i));
            }
            JOptionPane.showMessageDialog(this, "Emails válidos guardados correctamente.");
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
        FiltroEmails ventana = new FiltroEmails();
        ventana.setVisible(true);
    }
}