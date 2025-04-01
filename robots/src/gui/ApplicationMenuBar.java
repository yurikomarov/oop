package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class ApplicationMenuBar {
    private final MainApplicationFrame mainFrame;

    public ApplicationMenuBar(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu appMenu = createAppMenu();
        JMenu lookAndFeelMenu = createLookAndFeelMenu();
        JMenu testMenu = createTestMenu();

        menuBar.add(appMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);

        return menuBar;
    }

    private JMenu createAppMenu() {
        JMenu appMenu = new JMenu("Действия");
        appMenu.setMnemonic(KeyEvent.VK_R);
        appMenu.getAccessibleContext().setAccessibleDescription("Действия с приложением");

        JMenuItem exitOption = new JMenuItem("Выйти из приложения");
        exitOption.addActionListener((event) -> mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING)));
        appMenu.add(exitOption);

        return appMenu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> mainFrame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> mainFrame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));
        lookAndFeelMenu.add(crossplatformLookAndFeel);

        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
        testMenu.add(addLogMessageItem);

        return testMenu;
    }
}