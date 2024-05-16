import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame implements ActionListener {
    int boardWidth = 800;
    int boardHeight = 600;

    ImageIcon imageIcon;
    JLabel background;
    JButton startButton;
    JButton guideButton;
    JButton quitButton;
    JButton settingButton;

    public Menu() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Menu");

        // Load background image
        imageIcon = new ImageIcon(getClass().getResource("./Images/background2.jpg"));

        // Create background label
        background = new JLabel(imageIcon);
        background.setLayout(new BorderLayout());

        // Create and configure buttons
        startButton = createButton("Start Game", new Color(0, 0, 0));
        guideButton = createButton("Guide", new Color(0, 0, 0));
        settingButton = createButton("Settings", new Color(0, 0, 0));
        quitButton = createButton("Quit", new Color(0, 0, 0));
        JLabel title1 = new JLabel("Poker Hand Menu", JLabel.CENTER);
        title1.setForeground(Color.WHITE);
        title1.setFont(new Font("Arial", Font.BOLD, 18));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        buttonPanel.add(title1);
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.add(startButton);
        buttonPanel.add(guideButton);
        buttonPanel.add(settingButton);
        buttonPanel.add(quitButton);

        // Create center border to contain the buttons
        background.setBorder(BorderFactory.createEmptyBorder(180, 300, 180, 300));
        background.add(buttonPanel, BorderLayout.CENTER);

        // Add background label to the frame
        this.add(background);

        // Add component listener to adjust background size on frame resize
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBackground();
            }
        });


        this.setSize(boardWidth, boardHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void resizeBackground() {
        int width = this.getContentPane().getWidth();
        int height = this.getContentPane().getHeight();
        Image backgroundImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        background.setIcon(new ImageIcon(backgroundImage));
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            new PokerHand_1();
            this.dispose();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        } else if (e.getSource() == guideButton) {
            new Guide();
            this.dispose();
            System.out.println("Guide");
            this.dispose();
        } else if (e.getSource() == settingButton) {
            new Settings();
            System.out.println("Settings");
            this.dispose();
        }
    }
    // public static void main(String[] args) {
    //     new Menu();
    // }
}