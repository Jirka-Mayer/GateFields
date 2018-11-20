package cs.jirkamayer.gatefields;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {
    public MainMenu() {
        this.buildFileMenu();
        this.buildAddMenu();
    }

    private void buildFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener((x) -> this.newFile());
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newFile);

        JMenuItem saveFile = new JMenuItem("Save file...");
        saveFile.addActionListener((x) -> this.saveFile());
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(saveFile);

        this.add(fileMenu);
    }

    private void buildAddMenu() {
        JMenu addMenu = new JMenu("Add");
        addMenu.setMnemonic('A');

        JMenuItem notGate = new JMenuItem("NOT gate");
        notGate.addActionListener((x) -> this.addNotGate());
        notGate.setMnemonic('N');
        addMenu.add(notGate);

        this.add(addMenu);
    }

    private void newFile() {
        System.out.println("TODO: New file!");
    }

    private void saveFile() {
        System.out.println("TODO: Save file!");
    }

    private void addNotGate() {
        System.out.println("Add not gate...");
    }
}
