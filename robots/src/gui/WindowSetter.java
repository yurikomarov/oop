package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;
import java.util.List;

public class WindowSetter extends JInternalFrame {

    private final String homeDirectory = System.getProperty("user.home");
    private final String fileName = "dataToRestore.txt";
    private final String filePath = homeDirectory + File.separator + fileName;

    private final Map<String, Map<String, String>> allFrames = new HashMap<>();

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

        Map<String, String> frameData = new HashMap<>();
        frameData.put("title", title);
        frameData.put("x", String.valueOf(location.x));
        frameData.put("y", String.valueOf(location.y));
        frameData.put("width", String.valueOf(size.width));
        frameData.put("height", String.valueOf(size.height));
        frameData.put("isIcon", String.valueOf(frame.isIcon()));

        allFrames.put(title, frameData);
    }

    public void setLastState() {
        if (allFrames.containsKey(getTitle())) {
            Map<String, String> frameData = allFrames.get(getTitle());
            EventQueue.invokeLater(() -> {
                try {
                    setLocation(
                            Integer.parseInt(frameData.get("x")),
                            Integer.parseInt(frameData.get("y"))
                    );
                    setSize(
                            Integer.parseInt(frameData.get("width")),
                            Integer.parseInt(frameData.get("height"))
                    );
                    setIcon(Boolean.parseBoolean(frameData.get("isIcon")));
                } catch (PropertyVetoException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }

    public void saveLastStateData() {
        List<String> keyOrder = Arrays.asList("title", "x", "y", "width", "height", "isIcon");

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Map<String, String> frameData : allFrames.values()) {
                List<String> orderedValues = new ArrayList<>();
                for (String key : keyOrder) {
                    orderedValues.add(frameData.get(key));
                }
                String line = String.join(";", orderedValues);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getLastStateData() {
        allFrames.clear();

        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");

                Map<String, String> frameData = new HashMap<>();
                frameData.put("title", values[0]);
                frameData.put("x", values[1]);
                frameData.put("y", values[2]);
                frameData.put("width", values[3]);
                frameData.put("height", values[4]);
                frameData.put("isIcon", values[5]);

                allFrames.put(values[0], frameData);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}