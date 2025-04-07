package gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;
import java.util.List;

public class WindowSetter extends JInternalFrame {

    private final String homeDirectory = System.getProperty("user.home");
    private final String fileName = "dataToRestore.json";
    private final String filePath = homeDirectory + File.separator + fileName;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Map<String, Object>> allFrames = new HashMap<>();

    public WindowSetter() {
        JPanel panel = new JPanel(new BorderLayout());
        getContentPane().add(panel);
        getLastStateData();
        pack();
    }

    protected void initializeWindow(String title, boolean resizable, boolean closable,
                                    boolean maximizable, boolean iconifiable) {
        setTitle(title);
        setResizable(resizable);
        setClosable(closable);
        setMaximizable(maximizable);
        setIconifiable(iconifiable);
        pack();
        setLastState();
    }

    protected void getSizeAndLocation(JInternalFrame frame) {
        Dimension size = frame.getSize();
        Point location = frame.getLocation();
        String title = frame.getTitle();

        Map<String, Object> frameData = new LinkedHashMap<>();
        frameData.put("title", title);
        frameData.put("x", location.x);
        frameData.put("y", location.y);
        frameData.put("width", size.width);
        frameData.put("height", size.height);
        frameData.put("isIcon", frame.isIcon());

        allFrames.put(title, frameData);
    }

    public void setLastState() {
        if (allFrames.containsKey(getTitle())) {
            Map<String, Object> frameData = allFrames.get(getTitle());
            EventQueue.invokeLater(() -> {
                try {
                    setLocation(
                            ((Number) frameData.get("x")).intValue(),
                            ((Number) frameData.get("y")).intValue()
                    );
                    setSize(
                            ((Number) frameData.get("width")).intValue(),
                            ((Number) frameData.get("height")).intValue()
                    );
                    setIcon((Boolean) frameData.get("isIcon"));
                } catch (PropertyVetoException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }

    public void saveLastStateData() {
        try {
            objectMapper.writeValue(new File(filePath), allFrames.values());
        } catch (IOException e) {
            System.err.println("Error saving window state: " + e.getMessage());
        }
    }

    public void getLastStateData() {
        allFrames.clear();

        File file = new File(filePath);

        try {
            List<Map<String, Object>> framesList = objectMapper.readValue(
                    file, new TypeReference<>() {
                    });

            for (Map<String, Object> frameData : framesList) {
                if (frameData.containsKey("title")) {
                    allFrames.put(frameData.get("title").toString(), frameData);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading window state: " + e.getMessage());
        }
    }
}