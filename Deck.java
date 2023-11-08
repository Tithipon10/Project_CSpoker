import java.util.ArrayList;
import java.util.Collections;

public class Deck { // สร้าง class ชื่อ Deck
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>(); 
        String[] suits = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};
        String[] ranks = {"A", "B+", "B", "C+", "C", "D+", "D", "F"};

        for (String suit : suits) { 
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }

        shuffle();
    }

    // สับไพ่
    public void shuffle() { 
        Collections.shuffle(cards);
    }

    // แจกไพ่
    public Card dealCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null; // กรณีที่ไพ่หมด
    }

    // ตรวจสอบจำนวนไพ่ที่เหลือใน deck
    public int remainingCards() {
        return cards.size();
    }
}
