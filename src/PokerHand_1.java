import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class PokerHand_1 {
    private MusicPlayer musicPlayer = MusicPlayer.getInstance();
    // Card class
    private class Card {
        String value;
        String suit;

        Card(String value, String suit) {
            this.value = value;
            this.suit = suit;
        }

        public String toString() {
            return value + suit;
        }
    }
    //deck
    ArrayList<Card> deck;
    Random rand = new Random(); //shuffle deck

    //dealer
    ArrayList<Card> dealerHand;
    String dealerCategory;
    //player
    ArrayList<Card> playerHand;
    String playerCategory;


    /*------------------------------ GUI ------------------------------*/ 
    // window
    int boardWidth = 800;
    int boardHeight = 600;
    // Card
    int cardWidth = 110;
    int cardHeight = 154;

    JFrame frame = new JFrame("Poker Hand");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {

                // Dealer's hand title
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Dealer's Hand", 70+(cardWidth+20)*2, cardHeight + 30 + 40);
                
                //draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Image cardImage = new ImageIcon(getClass().getResource("./cards/" + dealerHand.get(i) + ".png")).getImage();
                    g.drawImage(cardImage, 75 + (cardWidth + 20) * i, 30, cardWidth, cardHeight, null);
                }
                
                // Player's hand title
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Player's Hand", 70+(cardWidth+20)*2, cardHeight + 280 + 40);
                //draw player's hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Image cardImage = new ImageIcon(getClass().getResource("./cards/" + playerHand.get(i) + ".png")).getImage();
                    g.drawImage(cardImage, 75 + (cardWidth + 20) * i, 280, cardWidth, cardHeight, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    // Button
    JPanel buttonPanel = new JPanel();
    JButton newGameButton = new JButton("New Game");
    JButton resultButton = new JButton("Who wins?");

    // Jmenu bar
    JMenuBar menuBar;
    JMenuItem backMenuItem;
    JMenuItem exitMenuItem;
    JMenuItem musicButton;

    // Constructor
    public PokerHand_1() {
        MenuBar();
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(0,128,0));
        frame.add(gamePanel);

        newGameButton.setFocusable(false);
        buttonPanel.add(newGameButton);
        resultButton.setFocusable(false);
        buttonPanel.add(resultButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        // Add action listener to new game button
        newGameButton.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                gamePanel.repaint();
            }
        });
        // Add action listener to the result button
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Ask the user for their prediction with validation
                String prediction = "";
                boolean validInput = false;
                while (!validInput) {
                    prediction = JOptionPane.showInputDialog(frame, "Enter your prediction (-1 for player, 0 for tie, 1 for dealer):");
                    if (prediction != null) {
                        try {
                            int userPrediction = Integer.parseInt(prediction);
                            if (userPrediction >= -1 && userPrediction <= 1) {
                                validInput = true;
                            } else {
                                JOptionPane.showMessageDialog(frame, "Please enter -1, 0, or 1.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                        }
                    } else {
                        // User canceled the input dialog
                        return;
                    }
                }

                int userPrediction = Integer.parseInt(prediction);
                // Compare hands of dealer and player but do not create a new window in GUI
                int comparisonResult = compare_to();
                int predictionResult = Integer.compare(comparisonResult, userPrediction);

                String winnerMessage;
                if (comparisonResult > 0) {
                    winnerMessage = "Dealer wins!";
                } else if (comparisonResult < 0) {
                    winnerMessage = "Player wins!";
                } else {
                    winnerMessage = "It's a tie!";
                }

                // Check if user's prediction is correct
                String predictionStatus;
                if (predictionResult == 0) {
                    predictionStatus = "Your prediction is correct!";
                } else {
                    predictionStatus = "Your prediction is incorrect.";
                }  

                int dealerCategory = get_category(dealerHand);
                int playerCategory = get_category(playerHand);

                String dealerCategoryString = getCategoryString(dealerCategory);
                String playerCategoryString = getCategoryString(playerCategory);

                String tiebreakerExplanation = getTiebreakerExplanation(comparisonResult);

                String htmlMessage = "<html><body><p>Dealer's category: " + dealerCategoryString + "</p>" +
                    "<p>Player's category: " + playerCategoryString + "</p>" +
                    "<p>" + winnerMessage + "</p>" +
                    "<p style='color: " + (predictionResult == 0 ? "blue" : "red") + "'>" + predictionStatus + "</p>" +
                    "<p> Explanation: " + tiebreakerExplanation + "</p></body></html>";
                JOptionPane.showMessageDialog(frame, htmlMessage);
            }
        });
    }
    
    private void MenuBar() {
        menuBar = new JMenuBar();
        // JMenu fileMenu = new JMenu("File");
        backMenuItem = new JMenuItem("Back");
        exitMenuItem = new JMenuItem("Exit");
        musicButton = new JMenuItem("Music");
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
        
        // Set music icon
        if (musicPlayer.isPlaying()){
            ImageIcon musicIcon = new ImageIcon(getClass().getResource("./Images/music.png"));
            musicButton.setIcon(new ImageIcon(musicIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } else {
            ImageIcon musicIcon1 = new ImageIcon(getClass().getResource("./Images/mute.png"));
            musicButton.setIcon(new ImageIcon(musicIcon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        
        // Add action listener to back button
        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to the main menu

                new Menu();
                //stop the music
                musicPlayer.stop();
                frame.dispose();
            }
        });
        // Add action listener to exit button
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
        // Add action listener to music button
        musicButton.addActionListener(e -> { 
            ImageIcon musicIcon = new ImageIcon(getClass().getResource("./Images/music.png"));
            ImageIcon muteIcon = new ImageIcon(getClass().getResource("./Images/mute.png"));

            if (musicPlayer.isPlaying()){
                musicPlayer.stop();
                // Change music button icon to a music icon
                musicButton.setIcon(new ImageIcon(muteIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                // turnOn.setEnabled(true);
            } else {
                musicPlayer.play(getClass().getResource("./Music/music.wav").getPath());
                // Change music button icon to a mute icon
                musicButton.setIcon(new ImageIcon(musicIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                // turnOn.setEnabled(false);
            }
        });
        
        // add back and exit button to the menu bar
        menuBar.add(backMenuItem);
        menuBar.add(exitMenuItem);
        menuBar.add(musicButton);
        menuBar.add(Box.createHorizontalStrut(550));
        
        frame.setJMenuBar(menuBar);
    }

    /*------------------------------ GAME ------------------------------*/
    // Start the game
    public void startGame() {
        buildDeck();
        shuffleDeck();
        //deal cards
        dealerHand = new ArrayList<Card>(deck.subList(0, 5));
        System.out.println("Dealer's hand: ");
        System.out.println(dealerHand);
        playerHand = new ArrayList<Card>(deck.subList(5, 10));
        System.out.println("Player's hand: ");
        System.out.println(playerHand);
    }
    // Build deck
    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] suits = {"H", "D", "C", "S"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }
        System.out.println("Before shuffle");
        System.out.println(deck);
    }
    // Shuffle deck
    public void shuffleDeck() {
        Collections.shuffle(deck);
        System.out.println("After shuffle");
        System.out.println(deck);
    }
    // Get category string
    private String getCategoryString(int category) {
        switch (category) {
            case 1:
                return "High Card";
            case 2:
                return "One Pair";
            case 3:
                return "Two Pair";
            case 4:
                return "Three of a Kind";
            case 5:
                return "Straight";
            case 6:
                return "Flush";
            case 7:
                return "Full House";
            case 8:
                return "Four of a Kind";
            case 9:
                return "Straight Flush";
            default:
                return "Unknown Category";
        }
    }
    // Getting poker Hand categories
    public int get_category(ArrayList<Card> hand) {
        if (StraightFlush(hand)) {
            return 9;
        } else if (FourOfAKind(hand)) {
            return 8;
        } else if (FullHouse(hand)) {
            return 7;
        } else if (Flush(hand)) {
            return 6;
        } else if (Straight(hand)) {
            return 5;
        } else if (ThreeOfAKind(hand)) {
            return 4;
        } else if (TwoPair(hand)) {
            return 3;
        } else if (OnePair(hand)) {
            return 2;
        } else {
            return 1;
        }
    }

    // Check if the hand is a Straight Flush
    private boolean StraightFlush(ArrayList<Card> hand) {
        return (Flush(hand) && Straight(hand));
    }
    // Check if the hand is a Four of a Kind
    private boolean FourOfAKind(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return ((ranks[0] == ranks[3]) || (ranks[4] == ranks[1]));
    }

    // Check if the hand is a Full House
    private boolean FullHouse(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return ((ranks[0] == ranks[2] && ranks[3] == ranks[4]) || (ranks[0] == ranks[1] && ranks[2] == ranks[4]));
    }

    // Check if the hand is a Flush
    private boolean Flush(ArrayList<Card> hand) {
        String suit = hand.get(0).suit;
        for (Card card : hand) {
            if (!card.suit.equals(suit)) {
                return false;
            }
        }
        return true;
    }
    
    // Check if the hand is a Straight
    private boolean Straight(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return (((ranks[4] - ranks[0] == 4) && (ranks[3]-ranks[1]==2)&&(ranks[2]-ranks[1]==1))||
         (ranks[0] == 2 && ranks[1] == 3 && ranks[2] == 4 && ranks[3] == 5 && ranks[4] == 14));
    }

    // Check if the hand is a Three of a Kind
    private boolean ThreeOfAKind(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return (ranks[0] == ranks[2] || ranks[1] == ranks[3] || ranks[2] == ranks[4]);
    }

    // Check if the hand is a Two Pair
    private boolean TwoPair(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return ((ranks[0] == ranks[1] && ranks[2] == ranks[3]) ||
                (ranks[0] == ranks[1] && ranks[3] == ranks[4]) ||
                (ranks[1] == ranks[2] && ranks[3] == ranks[4]));
    }

    // Check if the hand is a One Pair
    private boolean OnePair(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return (ranks[0] == ranks[1] || ranks[1] == ranks[2] || ranks[2] == ranks[3] || ranks[3] == ranks[4]);
    }
    // Get ranks of the hand
    private int[] getRanks(ArrayList<Card> hand) {
        int[] ranks = new int[5];
        for (int i = 0; i < 5; i++) {
            String card = hand.get(i).value;
            switch (card) {
                case "A":
                    ranks[i] = 14;
                    break;
                case "K":
                    ranks[i] = 13;
                    break;
                case "Q":
                    ranks[i] = 12;
                    break;
                case "J":
                    ranks[i] = 11;
                    break;
                case "10":
                    ranks[i] = 10;
                    break;
                default:
                    ranks[i] = Integer.parseInt(card);
            }
        }
        return ranks;
    }

    // Compare hands
    public int compare_to()  {
        int first_category = get_category(dealerHand);
        int second_category = get_category(playerHand);
        if (first_category > second_category) {
            return 1;
        } else if (second_category > first_category) {
            return -1;
        } else {
            // Compare highest rank of Straight Flush and Straight category
            if (first_category == 9 || first_category == 5) {
                int hand1_highest_rank = getHighestCardRank(dealerHand);
                int hand2_highest_rank = getHighestCardRank(playerHand);
                return Integer.compare(hand1_highest_rank, hand2_highest_rank);
            } else if (first_category == 6 || first_category == 1) {
                // Compare rank of Flush and High card category
                int[] ranks_1 = getRanks(dealerHand);
                int[] ranks_2 = getRanks(playerHand);
                Arrays.sort(ranks_1);
                Arrays.sort(ranks_2);
                for (int i = ranks_1.length - 1; i >= 0; i--) {
                    if (ranks_1[i] > ranks_2[i]) {
                        return 1;
                    } else if (ranks_1[i] < ranks_2[i]) {
                        return -1;
                    }
                }
                return 0;

            } else if (first_category == 2 || first_category == 3) { // One Pair and Two Pair
                int hand1_pair_value = getPairValue(dealerHand);
                int hand2_pair_value = getPairValue(playerHand);
                // Compare pair value of each hands
                if (hand1_pair_value < hand2_pair_value) {
                    return -1;
                } else if (hand1_pair_value > hand2_pair_value) {
                    return 1;
                } else {
                    int[] ranks_1 = getRanks(dealerHand);
                    int[] ranks_2 = getRanks(playerHand);
                    Arrays.sort(ranks_1);
                    Arrays.sort(ranks_2);
                    for (int i = ranks_1.length - 1; i >= 0; i--) {
                        if (ranks_1[i] > ranks_2[i]) {
                            return 1;
                        } else if (ranks_1[i] < ranks_2[i]) {
                            return -1;
                        }
                    }
                    return 0;

                }

            } else if (first_category == 4) {
                // Compare ranking of three of a kind category
                int hand1_triplet_value = getTripletValue(dealerHand);
                int hand2_triplet_value = getTripletValue(playerHand);
                if (hand1_triplet_value < hand2_triplet_value) {
                    return -1;
                } else if (hand2_triplet_value < hand1_triplet_value) {
                    return 1;
                } else {
                    int[] ranks_1 = getRanks(dealerHand);
                    int[] ranks_2 = getRanks(playerHand);
                    for (int i = ranks_1.length - 1; i >= 0; i--) {
                        if (ranks_1[i] > ranks_2[i]) {
                            return 1;
                        } else if (ranks_1[i] < ranks_2[i]) {
                            return -1;
                        }
                    }
                    return 0;
                }

            } else if (first_category == 8) {
                // compare rank of four of a kind category
                int hand1_pair_four = getPairFour(dealerHand);
                int hand2_pair_four = getPairFour(playerHand);
                if (hand1_pair_four < hand2_pair_four) {
                    return -1;
                } else if (hand2_pair_four < hand1_pair_four) {
                    return 1;
                } else {
                    int[] ranks_1 = getRanks(dealerHand);
                    int[] ranks_2 = getRanks(playerHand);
                    for (int i = ranks_1.length - 1; i >= 0; i--) {
                        if (ranks_1[i] > ranks_2[i]) {
                            return 1;
                        } else if (ranks_1[i] < ranks_2[i]) {
                            return -1;
                        }
                    }
                    return 0;
                }

            } else {
                // Compare rank of Full house category
                int hand1_triplet_value = getTripletValue(dealerHand);
                int hand2_triplet_value = getTripletValue(playerHand);
                if (hand1_triplet_value > hand2_triplet_value) {
                    return 1;
                } else if (hand1_triplet_value < hand2_triplet_value) {
                    return -1;
                } else {
                    int[] ranks_1 = getRanks(dealerHand);
                    int[] ranks_2 = getRanks(playerHand);
                    for (int i = ranks_1.length - 1; i >= 0; i--) {
                        if (ranks_1[i] > ranks_2[i]) {
                            return 1;
                        } else if (ranks_1[i] < ranks_2[i]) {
                            return -1;
                        }
                    }
                    return 0;

                }
            }   
        }
    }
    // Get the value of the four of a kind
    private int getPairFour(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        if (ranks[0] == ranks[3]) {
            return ranks[0];
        } else {
            return ranks[3];
        }
    }
    // Get the value of the three of a kind
    private int getTripletValue(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        if (ranks[0] == ranks[2]) {
            return ranks[0];
        } else {
            return ranks[2];
        }
    }
    // Get the value of the pair
    private int getPairValue(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        if (ranks[0] == ranks[1]) {
            return ranks[0];
        } else if (ranks[1] == ranks[2]) {
            return ranks[1];
        } else if (ranks[2] == ranks[3]) {
            return ranks[2];
        } else {
            return ranks[3];
        }
    }
    // Get the highest rank of Straiht Flush and Straight category
    private int getHighestCardRank(ArrayList<Card> hand) {
        int[] ranks = getRanks(hand);
        Arrays.sort(ranks);
        return ranks[4];
    }
    // Get the explanation of the tiebreaker
    private String getTiebreakerExplanation(int comparisonResult) {
        int thisCategory = get_category(dealerHand);
        int otherCategory = get_category(playerHand);
        String explanation = "";

        if (thisCategory != otherCategory) {
            // Different categories
            explanation = "Two hands have different categories. ";
            explanation += "The hand with the higher category wins.";
        } else {
            switch (thisCategory) {
                case 9: // Straight Flush
                    explanation = "Both hands are straight flushes.";
                    break;
                case 8: // Four of a Kind
                    explanation = "Both hands are four of a kinds.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the highest four of a kind wins.";
                    } else {
                        explanation += " Both hands have the same four of a kind. The hand with the highest kicker wins.";
                    }
                    break;
                case 7: // Full House
                    explanation = "Both hands are full houses.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the highest three of a kind wins.";
                    } else {
                        explanation += " Both hands have the same three of a kind. The hand with the highest pair wins.";
                    }
                    break;
                case 6: // Flush
                    explanation = "Both hands are flushes.";
                    break;
                case 5: // Straight
                    explanation = "Both hands are straights.";
                    break;
                case 4: // Three of a Kind
                    explanation = "Both hands are three of a kinds.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the highest three of a kind wins.";
                    } else {
                        explanation += " Both hands have the same three of a kind. The hand with the highest kicker wins.";
                    }
                    break;
                case 3: // Two Pair
                    explanation = "Both hands are two pairs.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the highest high pair wins.";
                    } else {
                        explanation += " Both hands have the same high pair. The hand with the highest low pair wins.";
                    }
                    break;
                case 2: // One Pair
                    explanation = "Both hands are one pairs.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the higher pair wins.";
                    } else {
                        explanation += " Both hands have the same pair. The hand with the highest kickers wins.";
                    }
                    break;
                default: // High Card
                    explanation = "Both hands are high cards.";
                    if (comparisonResult != 0) {
                        explanation += " The hand with the higher high card wins by rank.";
                    } else {
                        explanation = " Both hands have the same high card. The hands are tied.";
                    }
                    break;
            }
        }

        return explanation;
    }
    // Main method
    // public static void main(String[] args) {
    //     new PokerHand_1();
    // }
}
