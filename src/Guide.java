import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class Guide extends JFrame implements ActionListener {
    JFrame frame;
    JPanel centerPanel;
    JTable table;
    String[] col;
    Object[][] data;
    ImageIcon imageIcon;
    JLabel background;
    JMenuBar menuBar;
    JMenuItem backMenuItem;
    JMenuItem exitMenuItem; 

    public Guide() {
        frame = new JFrame();
        frame.setTitle("Poker Hand Ranking Guide");

        // Create a JLabel with a background image
        imageIcon = new ImageIcon(getClass().getResource("./Images/background.jpg"));
        background = new JLabel(imageIcon);
        background.setLayout(new BorderLayout());

        col = new String[]{"Ranking", "Category", "Description"};
        data = getData();
        table = new JTable(data, col);

        // Set row height
        table.setRowHeight(40);

        // Set column widths
        setColumnWidths();

        // Set column header colors
        setColumnHeaderColors();

        // Center-align cell values
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create panel with GridBagLayout to hold the table
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(table.getTableHeader(), gbc);
        gbc.gridy++;
        centerPanel.add(table, gbc);

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

        // Add center panel to the center of the background
        background.add(centerPanel, BorderLayout.CENTER);
        background.add(menuBar, BorderLayout.NORTH);

        // Set the content pane of the frame to the background JLabel
        frame.setContentPane(background);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.repaint();
    }

    public void setColumnWidths() {
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(90);
        columnModel.getColumn(2).setPreferredWidth(600);
    }

    public void setColumnHeaderColors() {
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY); // Set background color for header
    }

    public Object[][] getData() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/guide.csv");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(streamReader);
            ArrayList<String[]> list = new ArrayList<>(); // Store data as String arrays
            String str="";
            while ((str = br.readLine()) != null) {
                String[] row = str.split(",");
                list.add(row);
                //System.out.println(Arrays.toString(row));
            }

            Object[][] data = new Object[list.size()][3];

            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
            br.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0]; // Return empty array if an error occurs
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backMenuItem) {
            new Menu();
            frame.dispose();
        } else if (e.getSource() == exitMenuItem) {
            System.exit(0);
        }
    }
    // public static void main(String[] args) {
    //     new Guide();
    // }
}
