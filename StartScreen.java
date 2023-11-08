import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.AbstractBorder;

public class StartScreen {
    private JFrame startFrame; 
    private JButton startButton;
    private Image backgroundImage;

    public StartScreen() {
        startFrame = new JFrame("Welcome to CS Poker");  
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(1200, 900);

        startButton = new JButton("Start Game");
        startButton.setBackground(new Color(255, 165, 0)); // กำหนดพื้นหลัง button สีส้ม
        startButton.setForeground(Color.black);

        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.setFont(new Font("Arial", Font.BOLD, 25)); 
        startButton.setFocusPainted(false); // ไม่แสดงขอบเมื่อได้รับโฟกัส

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.setVisible(false); // ซ่อนหน้าต่างเริ่มต้น
                new PokerGameGUI(); // แสดงหน้าเกม
            }
        });

        ImageIcon backgroundIcon = new ImageIcon("Picture/BG_startgame.png"); // โหลดภาพพื้นหลังจากไฟล์
        backgroundImage = backgroundIcon.getImage(); // ดึงภาพพื้นหลังจาก ImageIcon

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this); // วาดภาพพื้นหลัง
            }
        };
        // ใช้ BorderLayout สำหรับ JPanel
        panel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // ทำให้สีพื้นหลังของปุ่มแสดงผล
        buttonPanel.add(startButton); // แสดงปุ่มในส่วนบนของ panel

        panel.add(buttonPanel, BorderLayout.PAGE_END); // แสดงปุ่มในส่วนบนของ panel

        startFrame.add(panel); // แสดง panel ใน frame
        startFrame.setVisible(true); // แสดง frame
    } 

    public static void main(String[] args) {
        new StartScreen();
    }
}

class RoundedBorder extends AbstractBorder {
    private int radius;

    RoundedBorder(int r) {
        this.radius = r;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // วาดกรอบสี่เหลี่ยม
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
    }
}

class PokerGameGUI {
    private JFrame frame;
    private Game game;

    private JButton betButton;
    private JButton foldButton;
    private JButton checkButton;

    private JLabel aiPlayerPointsLabel;
    private JLabel playerPointsLabel;
    private JLabel potLabel;

    private JPanel playerCardsPanel;
    private JPanel aiPlayerCardsPanel;
    private JPanel communityCardsPanel;

    public PokerGameGUI() {

        game = new Game();

        frame = new JFrame("CS Poker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);
        frame.setLayout(new BorderLayout());

        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        aiPlayerCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        communityCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        game.startInitialDeal(); // แจกไพ่เริ่มต้น
        updatePlayerInfo(); // แสดงไพ่ของ Player
        updateAIPlayerInfo(false); // ซ่อนไพ่ของ AI

        foldButton = new JButton("Fold"); // ปุ่ม Fold
        betButton = new JButton("Bet"); // ปุ่ม Bet 
        checkButton = new JButton("Check"); // ปุ่ม Check
        betButton.setVisible(true); // แสดงปุ่ม Bet
        checkButton.setVisible(false); // ซ่อนปุ่ม Check

        foldButton.setBackground(new Color(255, 99, 71)); // กำหนดพื้นหลัง button สีแดง
        foldButton.setBorderPainted(false); // ไม่แสดงเส้นขอบ
        foldButton.setPreferredSize(new Dimension(120, 70)); // กำหนดขนาดปุ่ม
        foldButton.setFont(new Font("Arial", Font.BOLD, 25)); // กำหนด font และขนาดตัวอักษร
        foldButton.setFocusPainted(false); // ไม่แสดงขอบเมื่อได้รับโฟกัส

        betButton.setBackground(new Color(255, 165, 0));
        betButton.setBorderPainted(false);
        betButton.setPreferredSize(new Dimension(120, 70));
        betButton.setFont(new Font("Arial", Font.BOLD, 25));
        betButton.setFocusPainted(false);

        checkButton.setBackground(new Color(255, 165, 0));
        checkButton.setBorderPainted(false);
        checkButton.setPreferredSize(new Dimension(120, 70));
        checkButton.setFont(new Font("Arial", Font.BOLD, 25));
        checkButton.setFocusPainted(false);

        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter your bet amount (10-100):"); // แสดง Input Dialog

                if (input != null && !input.isEmpty()) { // ตรวจสอบว่าผู้เล่นป้อนข้อมูลหรือไม่
                    int betAmount = Integer.parseInt(input); // แปลง String เป็น int

                    if (betAmount >= 10 && betAmount <= 100) { // ตรวจสอบว่ามีค่าระหว่าง 10-100 หรือไม่
                        if (!game.hasPlayerChecked()) { // ตรวจสอบว่าผู้เล่นเคย Check หรือยัง
                            game.addToPot(betAmount);   // เพิ่ม betAmount ใน pot   
                            game.getPlayer().deductPoints(betAmount);   // หัก betAmount จาก points ของ Player
                            int aiDecisionAmount = game.getAIPlayer().makeBet(betAmount); // AI จะทำการเดิมพันเท่ากับ betAmount
                            game.addToPot(aiDecisionAmount); // เพิ่ม aiDecisionAmount ใน pot
                            updateAIPlayerPoints(); // อัปเดต UI
                            updatePlayerPoints(); // อัปเดต UI
                            updatePotInfo(); // อัปเดต UI
                            betButton.setVisible(false);
                            checkButton.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(frame, "You have already checked. You can't bet again.",
                                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid bet amount between 10 and 100.",
                                "Invalid Bet", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // กำหนดให้ AIPlayer ชนะ
                game.getAIPlayer().addPoints(game.getPot());

                // รีเซ็ต pot เป็น 0
                game.addToPot(-game.getPot());

                // อัปเดต UI
                updateAIPlayerPoints();
                updatePotInfo();

                // แสดงข้อความว่า AIPlayer ชนะ
                JOptionPane.showMessageDialog(frame, "You Folded! AI Player wins this round.", "Round Over",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // ปิดหน้าต่างปัจจุบัน
                new StartScreen(); // เปิดหน้า StartScreen ใหม่
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.nextPhase();
                updatePlayerInfo();
                updateAIPlayerInfo(false); // Hide AI's real cards
                updateCommunityCards();

                // ตรวจสอบว่าถึงฟาสสุดท้ายหรือยัง
                if (game.getCurrentPhase() == GamePhase.END) {
                    updateAIPlayerInfo(true); // Show AI's real cards
                    updatePotInfo();

                    // หน่วงเวลาก่อนประกาศผู้ชนะ
                    Timer timer = new Timer(700, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            String winner = game.getRoundWinner();
                            String winningReason = game.getWinningReason();

                            if (winner.equals("Human Player")) {
                                ImageIcon winnerIcon = new ImageIcon("Picture/Winner.png");
                                JOptionPane.showMessageDialog(frame, winner + " wins with " + winningReason, "Winner",
                                        JOptionPane.INFORMATION_MESSAGE, winnerIcon);

                            } else {
                                ImageIcon GameOverIcon = new ImageIcon("Picture/GameOver.png");

                                // Custom JOptionPane for Try Again
                                final JOptionPane optionPane = new JOptionPane(winner + " wins with " + winningReason,
                                        JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, GameOverIcon,
                                        null, null);

                                JButton tryAgainButton = new JButton("Try again");
                                tryAgainButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Window window = SwingUtilities.windowForComponent(tryAgainButton);
                                        if (window != null) {
                                            window.setVisible(false);
                                        }
                                        frame.dispose(); // Close current frame
                                        new StartScreen(); // Open StartScreen again
                                    }
                                });

                                optionPane.setOptions(new JButton[] { tryAgainButton });
                                JDialog dialog = optionPane.createDialog(frame, "Game Over");
                                dialog.setVisible(true);
                            }

                            frame.dispose(); // ปิดหน้าต่างปัจจุบัน
                            new StartScreen(); // เปิดหน้า StartScreen ใหม่
                        }

                    });

                    timer.setRepeats(false); // ตั้งค่าให้ Timer ทำงานเพียงครั้งเดียว
                    timer.start(); // เริ่มต้น Timer
                }
                updatePlayerPoints();
                updateAIPlayerPoints();
            }
        });

        // Main Panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 128, 0));
        GridBagConstraints gbc = new GridBagConstraints();

        // Community Cards in the center
        gbc.gridy = 3;
        panel.add(communityCardsPanel, gbc);

        potLabel = new JLabel("Pot: " + game.getPot());
        potLabel.setPreferredSize(new Dimension(120, 70));
        potLabel.setFont(new Font("Arial", Font.BOLD, 25));
        potLabel.setForeground(Color.white);

        // AI Player
        JPanel aiPlayerPanel = new JPanel(new GridBagLayout());
        aiPlayerPointsLabel = new JLabel("Points: " + game.getAIPlayerPoints());
        JLabel aiPlayerNameLabel = new JLabel("Bot Player");
        gbc.gridy = 0;
        aiPlayerPanel.add(aiPlayerNameLabel, gbc);
        gbc.gridy = 1;
        aiPlayerPanel.add(aiPlayerPointsLabel, gbc);
        gbc.gridy = 2;
        aiPlayerPanel.add(new JLabel("Cards:"), gbc);
        gbc.gridy = 3;
        aiPlayerPanel.add(aiPlayerCardsPanel, gbc);
        aiPlayerNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        aiPlayerPointsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        // Player
        JPanel playerPanel = new JPanel(new GridBagLayout());
        playerPointsLabel = new JLabel("Points: " + game.getPlayerPoints());
        JLabel playerNameLabel = new JLabel("Human Player");
        gbc.gridy = 0;
        playerPanel.add(playerNameLabel, gbc);
        gbc.gridy = 1;
        playerPanel.add(playerPointsLabel, gbc);
        gbc.gridy = 2;
        playerPanel.add(new JLabel("Cards:"), gbc);
        gbc.gridy = 3;
        playerPanel.add(playerCardsPanel, gbc);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        playerPointsLabel.setFont(new Font("Arial", Font.BOLD, 25));

        // จัดวางตำแหน่งส่วนประกอบในเฟรมหลัก
        gbc.gridy = 2;
        panel.add(aiPlayerPanel, gbc);
        gbc.gridy = 4;
        panel.add(playerPanel, gbc);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // ใช้ FlowLayout หรือ Layout ที่คุณต้องการ
        buttonsPanel.add(potLabel);
        buttonsPanel.add(betButton);
        buttonsPanel.add(checkButton);
        buttonsPanel.add(foldButton);
        buttonsPanel.setBackground(new Color(0, 128, 0));
        buttonsPanel.setForeground(Color.black);
        buttonsPanel.setOpaque(true);

        frame.add(buttonsPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);

    }

    private void updatePlayerInfo() {
        playerCardsPanel.removeAll();
        for (Card card : game.getPlayerCards()) {
            JLabel cardLabel = new JLabel(loadCardImage(card));
            cardLabel.setPreferredSize(new Dimension(130, 160));
            cardLabel.setBorder(new RoundedBorder(10));
            playerCardsPanel.add(cardLabel);
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    private void updateAIPlayerInfo(boolean showRealCards) {
        aiPlayerCardsPanel.removeAll();
        if (showRealCards) {
            for (Card card : game.getAIPlayerCards()) {
                JLabel cardLabel = new JLabel(loadCardImage(card));
                cardLabel.setPreferredSize(new Dimension(130, 160));
                cardLabel.setBorder(new RoundedBorder(10));
                aiPlayerCardsPanel.add(cardLabel);
            }
        } else {
            for (int i = 0; i < game.getAIPlayerCards().size(); i++) {
                ImageIcon backCardIcon = loadBackCardImage();
                JLabel cardLabel = new JLabel(backCardIcon);
                cardLabel.setPreferredSize(new Dimension(130, 160));
                cardLabel.setBorder(new RoundedBorder(10));
                aiPlayerCardsPanel.add(cardLabel);
            }
        }
        aiPlayerCardsPanel.revalidate();
        aiPlayerCardsPanel.repaint();
    }

    private void updateCommunityCards() {
        communityCardsPanel.removeAll();
        for (Card card : game.getCommunityCards()) {
            JLabel cardLabel = new JLabel(loadCardImage(card));
            cardLabel.setPreferredSize(new Dimension(130, 160));
            cardLabel.setBorder(new RoundedBorder(10));
            communityCardsPanel.add(cardLabel);
        }
        communityCardsPanel.revalidate();
        communityCardsPanel.repaint();
    }

    public ImageIcon loadBackCardImage() {
        String filePath = "Picture/Back_card.png";
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(filePath));
        Image resizedImage = originalIcon.getImage().getScaledInstance(130, 160, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void updateAIPlayerPoints() {
        aiPlayerPointsLabel.setText("Points: " + game.getAIPlayer().getPoints());
    }

    private void updatePlayerPoints() {
        playerPointsLabel.setText("Points: " + game.getPlayer().getPoints());
    }

    private void updatePotInfo() {
        potLabel.setText("Pot: " + game.getPot());
    }

    public ImageIcon loadCardImage(Card card) {
        String baseFolderPath = "Picture/";
        String rank = card.getRank().replace("+", "_plus");
        String suit = card.getSuit().toString().toLowerCase();
        String filePath = baseFolderPath + rank + "_" + suit + ".png";
        ImageIcon originalIcon = new ImageIcon(filePath);
        Image resizedImage = originalIcon.getImage().getScaledInstance(130, 160, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
