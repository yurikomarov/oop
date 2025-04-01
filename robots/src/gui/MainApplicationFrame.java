package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import log.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
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
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");

    /// /        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }
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

        //UIManager.put("OptionPane.yesButtonText", "Да");
        //UIManager.put("OptionPane.noButtonText", "Нет");

        EventQueue.invokeLater(() -> {
            int response = JOptionPane.showConfirmDialog
                    (null,
                            "Завершить работу?",
                            "Подтверждение действия",
                            JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}
