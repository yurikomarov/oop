package gui;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends WindowSetter {
    private final GameVisualizer m_visualizer;

    public GameWindow() {

        super();
        initializeWindow("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}