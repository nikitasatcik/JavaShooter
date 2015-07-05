import javax.swing.*;


public class GameFrame extends JFrame {
    public GameFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(new GamePanel());
        pack();
        setVisible(true);
        setResizable(false);
    }
}
