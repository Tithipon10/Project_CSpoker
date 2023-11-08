import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> cards;
    private int points;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
        this.points = 100; // เริ่มต้นด้วย 100 คะแนน
    }

    public void receiveCard(Card card) {
        cards.add(card);
    }

    public void clearHand() { // สร้าง method clearHand
        this.cards.clear();
    }

    public void addPoints(int amount) { // สร้าง method addPoints ที่รับ parameter เป็น int
        this.points += amount;
    }

    public void subtractPoints(int points) { // สร้าง method subtractPoints ที่รับ parameter เป็น int
        this.points -= points;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public int makeBet(int betAmount) { // สร้าง method makeBet ที่รับ parameter เป็น int
        if (betAmount == 0) { // Check
            return 0;
        }

        if (betAmount <= points) {
            deductPoints(betAmount);
            return betAmount;
        } else {
            deductPoints(points);
            return getPoints();
        }
    }

    public void reset() { // สร้าง method reset
        this.cards.clear();
        this.points = 100; 
    }

    public void deductPoints(int amount) { // สร้าง method deductPoints ที่รับ parameter เป็น int
        this.points -= amount;
        if (this.points < 0) { 
            this.points = 0;
        }
    }
}
