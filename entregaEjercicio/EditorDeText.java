import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PROU7EX02 - Editor de Texto con Barra de Herramientas y Gestión de Ficheros
 * 
 * Un procesador de texto básico hecho con Java Swing.
 * Uso FileInputStream y FileOutputStream para la lectura/escritura secuencial,
 */
public class EditorDeText extends JFrame {

    // El área donde escribo, la zona principal del editor
    private JTextArea areaTexto;

    // Aquí guardo el fichero actual (puede ser null si aún no he guardado nada)
    // Esto es clave para diferenciar "Guardar" de "Guardar como..."
    private File ficheroActual;

    // El selector de ficheros, el típico diálogo de abrir/guardar
    private JFileChooser selectorFicheros;

    /**
     * Constructor principal. Aquí monto toda la interfaz gráfica.
     */
    public EditorDeText() {
        // Configuración básica de la ventana
        setTitle("Editor de Texto - PROU7EX02");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centro la ventana, que queda más profesional

        // Inicializo el selector de ficheros
        selectorFicheros = new JFileChooser();

        // Creo la barra de botones en la parte superior
        JToolBar barraHerramientas = crearBarraHerramientas();

        // Creo el área de texto con scroll para poder desplazarse
        areaTexto = new JTextArea();
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Fuente monoespaciada para que se lea bien
        areaTexto.setLineWrap(true);        // Que el texto haga salto de línea automático
        areaTexto.setWrapStyleWord(true);   // Y que corte por palabras, no por la mitad de una
        JScrollPane scrollPane = new JScrollPane(areaTexto);

        // Añado todo al JFrame con BorderLayout
        // La barra arriba (NORTH) y el área de texto en el centro (CENTER)
        setLayout(new BorderLayout());
        add(barraHerramientas, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // El fichero actual empieza a null porque no he abierto nada todavía
        ficheroActual = null;
    }

    /**
     * Creo la barra de herramientas con los 4 botones que me piden:
     * Nuevo, Abrir, Guardar y Guardar como...
    */
    private JToolBar crearBarraHerramientas() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false); // Que no se pueda arrastrar la barra, que luego se lía

        // Botón NUEVO - Para empezar un documento desde cero
        JButton btnNuevo = new JButton("📄 Nuevo");
        btnNuevo.setToolTipText("Nuevo documento - limpia todo y empieza de cero");
        btnNuevo.addActionListener((ActionEvent e) -> {
            accionNuevo();
        });

        // Botón ABRIR - Para cargar un fichero existente con FileInputStream
        JButton btnAbrir = new JButton("📂 Abrir");
        btnAbrir.setToolTipText("Abrir un fichero existente");
        btnAbrir.addActionListener((ActionEvent e) -> {
            accionAbrir();
        });

        // Botón GUARDAR - Guarda directamente si ya tengo fichero, si no, actúa como "Guardar como..."
        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setToolTipText("Guardar los cambios en el fichero actual");
        btnGuardar.addActionListener((ActionEvent e) -> {
            accionGuardar();
        });

        // Botón GUARDAR COMO... - Siempre pregunta dónde quiero guardar
        JButton btnGuardarComo = new JButton("💾➕ Guardar como...");
        btnGuardarComo.setToolTipText("Guardar con un nombre o ruta diferente");
        btnGuardarComo.addActionListener((ActionEvent e) -> {
            accionGuardarComo();
        });

        // Añado todos los botones a la barra
        barra.add(btnNuevo);
        barra.add(btnAbrir);
        barra.addSeparator(); // Un pequeño espacio para separar las acciones de abrir de las de guardar
        barra.add(btnGuardar);
        barra.add(btnGuardarComo);

        return barra;
    }

    /**
     * Acción del botón "Nuevo".
     * Limpia el área de texto y resetea el fichero actual.
     */
    private void accionNuevo() {
        areaTexto.setText("");    // Vacío el área de texto
        ficheroActual = null;     // Ya no tengo ningún fichero asociado
        setTitle("Editor de Texto - PROU7EX02"); // Vuelvo al título original
    }

    /**
     * Acción del botón "Abrir".
     * Uso JFileChooser para seleccionar un fichero y después
     * lo leo con FileInputStream (lectura secuencial, byte a byte)
     */
    private void accionAbrir() {
        // Muestro el diálogo de abrir fichero
        int resultado = selectorFicheros.showOpenDialog(this);

        // Si el usuario ha seleccionado un fichero (y no ha pulsado "Cancelar")
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File ficheroSeleccionado = selectorFicheros.getSelectedFile();

            // Leo con FileInputStream, lectura secuencial
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(ficheroSeleccionado);

                // Miro cuántos bytes tiene el fichero con available()
                int tamanyo = fis.available();
                String texto = "";
                char caracter;

                // Lectura secuencial: byte a byte, como en el ejemplo de las diapositivas
                for (int i = 0; i < tamanyo; i++) {
                    caracter = (char) fis.read(); // Leo un byte y lo convierto a char
                    texto = texto + caracter;      // Lo añado al String
                }

                // Pongo el texto leído en el área de texto
                areaTexto.setText(texto);

                // Actualizo el fichero actual y el título de la ventana
                ficheroActual = ficheroSeleccionado;
                setTitle("Editor de Texto - " + ficheroActual.getName());

            } catch (IOException ex) {
                // Si algo falla, al menos aviso al usuario
                JOptionPane.showMessageDialog(this,
                        "Error al abrir el fichero: " + ex.getMessage(),
                        "Error de lectura",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Y por consola también, por si toca debuggear
            } finally {
                // IMPORTANTÍSIMO: cerrar el flujo SIEMPRE, pase lo que pase
                // El finally se ejecuta sí o sí, incluso si hay una excepción
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        // Si falla el close no puedo hacer mucho más
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Acción del botón "Guardar".
     * Aquí viene la diferencia clave con "Guardar como...":
     * - Si ficheroActual NO es null -> escribo directamente en el fichero
     * - Si ficheroActual ES null -> redirijo a "Guardar como..." porque no sé dónde guardar
     */
    private void accionGuardar() {
        if (ficheroActual != null) {
            // Ya tengo un fichero asociado, escribo directamente sin preguntar
            escribirFichero(ficheroActual);
        } else {
            // No tengo fichero todavía, así que hago lo mismo que "Guardar como..."
            // Esto pasa cuando escribo algo nuevo y le doy a "Guardar" por primera vez
            accionGuardarComo();
        }
    }

    /**
     * Acción del botón "Guardar como...".
     * SIEMPRE abre el selector de ficheros para elegir una nueva ruta o nombre.
     * Esto es útil para guardar copias con nombres diferentes, por ejemplo.
     */
    private void accionGuardarComo() {
        // Muestro el diálogo de guardar fichero
        int resultado = selectorFicheros.showSaveDialog(this);

        // Si el usuario ha elegido dónde guardar (y no ha cancelado)
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File ficheroNuevo = selectorFicheros.getSelectedFile();

            // Escribo en el fichero seleccionado
            escribirFichero(ficheroNuevo);

            // Actualizo el fichero actual (a partir de ahora, "Guardar" escribirá aquí)
            ficheroActual = ficheroNuevo;
            setTitle("Editor de Texto - " + ficheroActual.getName());
        }
    }

    /**
     * Escribe el contenido del área de texto en el fichero pasado como parámetro.
     * Uso FileOutputStream con escritura secuencial, byte a byte.
     * 
     * @param fichero el File donde quiero escribir
     */
    private void escribirFichero(File fichero) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fichero);

            // Cojo el texto del área
            String texto = areaTexto.getText();
            char caracter;

            // Escritura secuencial: carácter a carácter, convirtiendo a byte
            for (int i = 0; i < texto.length(); i++) {
                caracter = texto.charAt(i);
                fos.write((byte) caracter); // Convierto el char a byte y lo escribo
            }

        } catch (IOException ex) {
            // Si falla la escritura, aviso al usuario
            JOptionPane.showMessageDialog(this,
                    "Error al guardar el fichero: " + ex.getMessage(),
                    "Error de escritura",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            // Otra vez: CERRAR EL FLUJO SIEMPRE dentro del finally
            // Esto es obligatorio según el enunciado
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * El main, el punto de entrada.
     * Simplemente creo la ventana y la hago visible.
     * Uso invokeLater porque Swing va con su propio hilo de ejecución.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EditorDeText editor = new EditorDeText();
                editor.setVisible(true);
            }
        });
    }
}