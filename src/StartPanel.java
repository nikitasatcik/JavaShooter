import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StartPanel extends JPanel {
    private JPanel btnPanel;

    public StartPanel() {
        super();
        setLayout(new GridLayout(1, 2));
        setVisible(true);
        btnPanel = new JPanel();
        btnPanel.setVisible(true);
        btnPanel.setLayout(new GridLayout(1, 2));

        addButton("START", new StartAction());
        addButton("STOP", new StopAction());
        add(btnPanel, BorderLayout.CENTER);
    }

    public void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        btnPanel.add(button);
    }

    private static final class StartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                new GameFrame();
        }
    }

    private static final class StopAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
}


