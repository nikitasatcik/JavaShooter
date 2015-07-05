import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    public StartFrame() {
        super("Java Shooter");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(250, 100);
        setVisible(true);
        StartPanel panel = new StartPanel();
        add(panel, BorderLayout.CENTER);
        setResizable(false);
    }
}
