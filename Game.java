import java.util.ArrayList;
import java.util.List;

public class Game {
    private Deck deck;
    private Player humanPlayer;
    private AIPlayer aiPlayer;
    private List<Card> communityCards;
    private int pot;
    private String lastRoundWinner;
    private String lastWinningReason;
    private GamePhase currentPhase;
    private boolean playerHasChecked = false;

    public Game() { // สร้าง constructor ของ Game class
        this.deck = new Deck();
        this.humanPlayer = new Player("Human");
        this.aiPlayer = new AIPlayer( "AI");
        this.communityCards = new ArrayList<>();
        this.pot = 0;
    }

    public void startInitialDeal() { // สร้าง method startInitialDeal
        deck.shuffle();
        communityCards.clear();

        humanPlayer.reset();
        aiPlayer.reset();

        for (int i = 0; i < 2; i++) {
            humanPlayer.receiveCard(deck.dealCard());
            aiPlayer.receiveCard(deck.dealCard());
        }
        this.currentPhase = GamePhase.PREFLOP;
    }

    public void startNewRoundWithBet(int betAmount) { // สร้าง method startNewRoundWithBet ที่รับ parameter เป็น int
        if (humanPlayer.getPoints() <= 0) { // ถ้าคะแนนของผู้เล่นเหลือ 0 หรือน้อยกว่า
            startInitialDeal();
        } else {
            deck.shuffle(); // สับไพ่
            communityCards.clear(); // ล้าง community cards

            humanPlayer.makeBet(betAmount); // ผู้เล่นทำเดิมพัน
            aiPlayer.makeBet(betAmount); // AI ทำเดิมพันเท่ากับผู้เล่น

            pot = 2 * betAmount; // ให้ pot เท่ากับ 2 เท่าของ betAmount

            humanPlayer.reset(); // ล้างไพ่ของผู้เล่น
            aiPlayer.reset(); // ล้างไพ่ของ AI

            for (int i = 0; i < 2; i++) { // แจกไพ่ใหม่
                humanPlayer.receiveCard(deck.dealCard());
                aiPlayer.receiveCard(deck.dealCard());
            }
            this.currentPhase = GamePhase.PREFLOP; // เริ่มต้นใน phase ของ preflop
        }
    }

    public void nextPhase() { // สร้าง method nextPhase
        switch (currentPhase) { // ตรวจสอบ phase ปัจจุบัน
            case PREFLOP:
                currentPhase = GamePhase.FLOP;
                break;
            case FLOP:
                dealFlop();
                currentPhase = GamePhase.TURN;
                break;
            case TURN:
                dealTurn(); 
                currentPhase = GamePhase.RIVER;
                break;
            case RIVER:
                dealRiver();
                currentPhase = GamePhase.END;
                showWinner();
                break;
            case END: // กรณีที่เกมจบแล้ว ให้เริ่มเกมใหม่
                break;
        }
    }

    public void addToPot(int amount) { // สร้าง method addToPot ที่รับ parameter เป็น int
        this.pot += amount;
    }

    public GamePhase getCurrentPhase() {  // สร้าง method getCurrentPhase ที่คืนค่า currentPhase
        return this.currentPhase;
    }

    private void showWinner() {
        List<Card> humanCombined = new ArrayList<>(humanPlayer.getCards()); // รวมไพ่ของผู้เล่นกับ community cards
        humanCombined.addAll(communityCards);

        List<Card> aiCombined = new ArrayList<>(aiPlayer.getCards()); // รวมไพ่ของ AI กับ community cards
        aiCombined.addAll(communityCards);

        HandRanking humanRank = PokerHandEvaluator.evaluateHand(humanCombined); // ตรวจสอบ hand ranking ของผู้เล่น
        HandRanking aiRank = PokerHandEvaluator.evaluateHand(aiCombined); // ตรวจสอบ hand ranking ของ AI

        System.out.println("\nHuman hand: " + humanRank); // แสดงผล hand ranking ของผู้เล่น
        System.out.println("AI hand: " + aiRank); // แสดงผล hand ranking ของ AI

        if (humanRank.ordinal() > aiRank.ordinal()) { // ถ้าผู้เล่นมี hand ranking มากกว่า AI
            lastRoundWinner = "Human Player";
            lastWinningReason = humanRank.toString();
            System.out.println("Human player wins with " + "'" + humanRank + "'" );
        } else if (humanRank.ordinal() < aiRank.ordinal()) { // ถ้า AI มี hand ranking มากกว่าผู้เล่น
            lastRoundWinner = "AI Player";
            lastWinningReason = aiRank.toString();
            System.out.println("AI player wins with " + "'" + aiRank + "'" );
        } else { // กรณีที่ผู้เล่นและ AI มี hand ranking เท่ากัน
            lastRoundWinner = "Draw";
            lastWinningReason = "Equal Hand Ranks " + "'" + humanRank + "'";
            System.out.println("It's a tie!");
        }

        awardPotToWinner(lastRoundWinner);
    }

    public void deductAIPlayerPoints(int amount) { // สร้าง method deductAIPlayerPoints ที่รับ parameter เป็น int
        aiPlayer.deductPoints(amount);
    }

    public int getPot() {  // สร้าง method getPot ที่คืนค่า pot
        return this.pot; 
    }

    public String getRoundWinner() { // สร้าง method getRoundWinner ที่คืนค่า lastRoundWinner
        return lastRoundWinner;
    }
 
    public String getWinningReason() { // สร้าง method getWinningReason ที่คืนค่า lastWinningReason
        return lastWinningReason;
    }

    public List<Card> getPlayerCards() { // สร้าง method getPlayerCards ที่คืนค่าไพ่ของผู้เล่น
        return humanPlayer.getCards();
    }

    public int getPlayerPoints() {
        return humanPlayer.getPoints(); // สร้าง method getPlayerPoints ที่คืนค่าคะแนนของผู้เล่น
    }

    public List<Card> getAIPlayerCards() { // สร้าง method getAIPlayerCards ที่คืนค่าไพ่ของ AI
        return aiPlayer.getCards();
    }

    public int getAIPlayerPoints() { // สร้าง method getAIPlayerPoints ที่คืนค่าคะแนนของ AI
        return aiPlayer.getPoints();
    }

    public List<Card> getCommunityCards() { // สร้าง method getCommunityCards ที่คืนค่า community cards
        return this.communityCards;
    }

    public void dealFlop() {
        for (int i = 0; i < 3; i++) {
            communityCards.add(deck.dealCard());  // แจกไพ่ให้ community cards
        }
        this.currentPhase = GamePhase.FLOP;
    }
 
    public void dealTurn() {
        communityCards.add(deck.dealCard()); 
        this.currentPhase = GamePhase.TURN; 
    }

    public void dealRiver() {
        communityCards.add(deck.dealCard());
        this.currentPhase = GamePhase.RIVER; 
    }

    public Player getPlayer() { // สร้าง method getPlayer ที่คืนค่า humanPlayer
        return this.humanPlayer;
    }

    public AIPlayer getAIPlayer() { // สร้าง method getAIPlayer ที่คืนค่า aiPlayer
        return this.aiPlayer; 
    }

    public void setPlayerChecked(boolean checked) { // สร้าง method setPlayerChecked ที่รับ parameter เป็น boolean
        this.playerHasChecked = checked; 
    }

    public boolean hasPlayerChecked() { // สร้าง method hasPlayerChecked ที่คืนค่า playerHasChecked
        return this.playerHasChecked;
    }

    private void awardPotToWinner(String winnerName) { // มอบ pot ให้ผู้ชนะ
        if (winnerName.equals("Human Player")) {
            humanPlayer.addPoints(pot);
        } else if (winnerName.equals("AI Player")) {
            aiPlayer.addPoints(pot);
        } else if (winnerName.equals("Draw")) {
            int halfPot = pot / 2; 
            humanPlayer.addPoints(halfPot);
            aiPlayer.addPoints(halfPot);
        }
        pot = 0; // reset pot หลังจากที่มอบ pot แล้ว
    }

}
