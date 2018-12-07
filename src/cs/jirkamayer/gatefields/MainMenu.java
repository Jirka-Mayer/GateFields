package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.actions.ActionController;
import cs.jirkamayer.gatefields.editor.actions.AddElementAction;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainMenu extends JMenuBar {
    private Scheme scheme;
    private ActionController actionController;
    private Camera camera;
    private MainWindow mainWindow;

    public MainMenu(Scheme scheme, ActionController actionController, Camera camera, MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.scheme = scheme;
        this.actionController = actionController;
        this.camera = camera;

        this.buildFileMenu();
        this.buildAddMenu();
    }

    private void buildFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New scheme");
        newFile.addActionListener((x) -> this.newFile());
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newFile);

        JMenuItem openFile = new JMenuItem("Open file...");
        openFile.addActionListener((x) -> this.openFile());
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(openFile);

        JMenuItem saveFile = new JMenuItem("Save file...");
        saveFile.addActionListener((x) -> this.saveFile());
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(saveFile);

        this.add(fileMenu);
    }

    private void buildAddMenu() {
        JMenu addMenu = new JMenu("Add");
        addMenu.setMnemonic('A');

        JMenuItem inputGate = new JMenuItem("Input");
        inputGate.addActionListener((x) -> this.addGate(new LogicalInput()));
        inputGate.setMnemonic('I');
        addMenu.add(inputGate);

        JMenuItem notGate = new JMenuItem("NOT gate");
        notGate.addActionListener((x) -> this.addGate(new NotGate()));
        notGate.setMnemonic('N');
        addMenu.add(notGate);

        JMenuItem bufferGate = new JMenuItem("Buffer");
        bufferGate.addActionListener((x) -> this.addGate(new BufferGate()));
        bufferGate.setMnemonic('B');
        addMenu.add(bufferGate);

        JMenuItem andGate = new JMenuItem("AND gate");
        andGate.addActionListener((x) -> this.addGate(new AndGate()));
        andGate.setMnemonic('A');
        addMenu.add(andGate);

        JMenuItem orGate = new JMenuItem("OR gate");
        orGate.addActionListener((x) -> this.addGate(new OrGate()));
        orGate.setMnemonic('O');
        addMenu.add(orGate);

        JMenuItem xorGate = new JMenuItem("XOR gate");
        xorGate.addActionListener((x) -> this.addGate(new XorGate()));
        xorGate.setMnemonic('X');
        addMenu.add(xorGate);

        JMenuItem nandGate = new JMenuItem("NAND gate");
        nandGate.addActionListener((x) -> this.addGate(new NandGate()));
        nandGate.setMnemonic('D');
        addMenu.add(nandGate);

        JMenuItem norGate = new JMenuItem("NOR gate");
        norGate.addActionListener((x) -> this.addGate(new NorGate()));
        norGate.setMnemonic('R');
        addMenu.add(norGate);

        JMenuItem srLatch = new JMenuItem("SR latch");
        srLatch.addActionListener((x) -> this.addGate(new SRLatch()));
        srLatch.setMnemonic('S');
        addMenu.add(srLatch);

        JMenuItem dLatch = new JMenuItem("D latch");
        dLatch.addActionListener((x) -> this.addGate(new DLatch()));
        dLatch.setMnemonic('L');
        addMenu.add(dLatch);

        JMenuItem jkLatch = new JMenuItem("JK latch");
        jkLatch.addActionListener((x) -> this.addGate(new JKLatch()));
        jkLatch.setMnemonic('J');
        addMenu.add(jkLatch);

        this.add(addMenu);
    }

    private void newFile() {
        if (JOptionPane.showConfirmDialog(null, "Forget current scheme?") != JOptionPane.OK_OPTION)
            return;

        mainWindow.stopSimulation();

        scheme.clear();
        scheme.add(new LogicalInput());
        camera.reset();

        mainWindow.startSimulation();
    }

    private void saveFile() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("untitled.gfs"));

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (file.isDirectory()) {
                JOptionPane.showMessageDialog(null, "Select a file, not a folder.");
                return;
            }

            if (file.isFile()) {
                if (JOptionPane.showConfirmDialog(null, "Overwrite file?") != JOptionPane.OK_OPTION)
                    return;
            }

            // perform the actual saving
            mainWindow.stopSimulation();
            Saver saver = new Saver(scheme, camera);
            saver.saveTo(file);
            mainWindow.startSimulation();
        }
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (!file.isFile()) {
                JOptionPane.showMessageDialog(null, "File does not exist.");
                return;
            }

            // perform the actual loading
            mainWindow.stopSimulation();
            scheme.clear();
            Saver saver = new Saver(scheme, camera);
            boolean response = saver.loadFrom(file);
            mainWindow.startSimulation();

            if (!response)
                JOptionPane.showMessageDialog(null, "File cannot be read.");
        }
    }

    private void addGate(Element element) {
        AddElementAction action = new AddElementAction(scheme, element, camera);
        actionController.activateActionManually(action);
    }
}
