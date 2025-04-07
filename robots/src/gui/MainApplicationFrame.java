package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import log.Logger;

import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowSetter windowSetter = new WindowSetter();

    public MainApplicationFrame() throws PropertyVetoException {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = createGameWindow();
        addWindow(gameWindow);

        ApplicationMenuBar menuBar = new ApplicationMenuBar(this);
        setJMenuBar(menuBar.createMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitFromApplication();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        return new GameWindow();
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    protected void exitFromApplication() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));

        UIManager.put("OptionPane.yesButtonText", bundle.getString("yesButtonText"));
        UIManager.put("OptionPane.noButtonText", bundle.getString("noButtonText"));

        EventQueue.invokeLater(() -> {
            int response = JOptionPane.showConfirmDialog
                    (null,
                            "Завершить работу?",
                            "Подтверждение действия",
                            JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    windowSetter.getSizeAndLocation(frame);
                }
                windowSetter.saveLastStateData();
                dispose();
                System.exit(0);
            }
        });
    }
}