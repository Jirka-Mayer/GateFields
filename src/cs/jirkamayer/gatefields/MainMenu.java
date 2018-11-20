package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.actions.ActionController;
import cs.jirkamayer.gatefields.editor.actions.AddElementAction;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.NotGate;
import cs.jirkamayer.gatefields.scheme.Scheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {
    private Scheme scheme;
    private ActionController actionController;
    private Camera camera;

    public MainMenu(Scheme scheme, ActionController actionController, Camera camera) {
        this.scheme = scheme;
        this.actionController = actionController;
        this.camera = camera;

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

        Element element = new NotGate();
        AddElementAction action = new AddElementAction(scheme, element, camera);
        actionController.activateActionManually(action);
    }
}
