import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Settings extends JFrame implements ActionListener {
    private MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private JFrame frame;
    private JLabel background;
    private ImageIcon imageIcon;
    private JButton turnOn;
    private JButton turnOff;
    JMenuBar menuBar;
    JMenuItem backMenuItem;
    JMenuItem exitMenuItem;
    // private Clip clip;

    public Settings() {
        frame = new JFrame();
        frame.setTitle("Settings");

        // Create a JLabel with a background image
        imageIcon = new ImageIcon(getClass().getResource("./Images/background.jpg"));
        background = new JLabel(imageIcon);
        background.setLayout(new BorderLayout());

        // Create the buttons
        turnOn = createButton("Turn On", new Color(0, 130, 0));
        turnOff = createButton("Turn Off", new Color(0, 130, 0));
        JLabel title1 = new JLabel("Music Background", JLabel.CENTER);
        title1.setForeground(Color.WHITE);
        title1.setFont(new Font("Arial", Font.BOLD, 18));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        buttonPanel.add(title1);
        buttonPanel.setOpaque(false);
        buttonPanel.add(turnOn);
        buttonPanel.add(turnOff);

        // private void MenuBar() {
            menuBar = new JMenuBar();
            // JMenu fileMenu = new JMenu("File");
            backMenuItem = new JMenuItem("Back");
            exitMenuItem = new JMenuItem("Exit");
            //create back button with arrow icon
            ImageIcon backIcon = new ImageIcon(getClass().getResource("./Images/arrow.png"));
            //resize the icon to fit the menu bar
            Image img = backIcon.getImage();
            Image newImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            backIcon = new ImageIcon(newImg);
            backMenuItem.setIcon(backIcon);
            // CREATE EXIT BUTTON WITH EXIT ICON
            ImageIcon exitIcon = new ImageIcon(getClass().getResource("./Images/exit.png"));
            //resize the icon to fit the menu bar
            Image img2 = exitIcon.getImage();
            Image newImg2 = img2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            exitIcon = new ImageIcon(newImg2);
            exitMenuItem.setIcon(exitIcon);
    
            backMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Go back to the main menu
                    new Menu();
                    frame.dispose();
                }
            });
    
            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });
            // add back and exit button to the menu bar
            menuBar.add(backMenuItem);
            menuBar.add(exitMenuItem);
            menuBar.add(Box.createHorizontalStrut(650));
            frame.setJMenuBar(menuBar);
        // }

        // Add the center panel to the background label
        background.setBorder(BorderFactory.createEmptyBorder(180, 300, 180, 300));
        background.add(buttonPanel, BorderLayout.CENTER);

        // Set the content pane
        frame.setContentPane(background);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
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
        // Handle button clicks 
        if (e.getSource() == turnOn) {
            musicPlayer.play(getClass().getResource("./Music/music.wav").getPath());
            // Button 1 clicked
            System.out.println("Background music is turning on!");
            // Handle double click on turn on button, nothing happens
            turnOn.setEnabled(false);        
            
        } else if (e.getSource() == turnOff) {
            // Button 2 clicked
            musicPlayer.stop();
            System.out.println("Background music is turning off!");
            // When turn off button is clicked, turn on button is enabled again
            turnOn.setEnabled(true);
        } else if (e.getSource() == backMenuItem) {
            // Back menu item clicked
            System.out.println("Back menu item clicked!");
            new Menu();
            frame.dispose();
        } else if (e.getSource() == exitMenuItem) {
            // Exit menu item clicked
            System.out.println("Exit menu item clicked!");
            System.exit(0);
        }
    }
}