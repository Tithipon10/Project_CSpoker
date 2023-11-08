public class Card implements Comparable<Card> {
    public enum Suit { // สร้าง enum ชื่อ Suit ที่มีค่าคงที่เป็น
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    private final String rank;
    private final Suit suit;

    // กำหนดความสำคัญของ rank เพื่อใช้ในการเปรียบเทียบ
    private static final String[] RANKS = {"F", "D", "D+", "C", "C+", "B", "B+", "A"};
    
    public String getImageName() { // สร้าง method getImageName ที่คืนค่าชื่อไฟล์รูปภาพของไพ่
        return this.rank + "_" + this.suit.name().toLowerCase();
    }
    
    public Card(String rank, String suitString) { // สร้าง constructor ที่รับ parameter เป็น String สองตัว
        this.rank = rank;
        this.suit = Suit.valueOf(suitString.toUpperCase());
    }

    public String getRank() { // สร้าง method getRank ที่คืนค่า rank
        return rank;
    }

    public Suit getSuit() { // สร้าง method getSuit ที่คืนค่า suit
        return suit;
    }

    @Override
    public String toString() { // สร้าง method toString ที่คืนค่า rank และ suit ของไพ่
        return rank + " of " + suit;
    }

    @Override
    public int compareTo(Card other) { // เปรียบเทียบ rank ของไพ่
        return Integer.compare(getRankValue(this.rank), getRankValue(other.rank));
    }

    private int getRankValue(String rank) { // คืนค่าความสำคัญของ rank
        for (int i = 0; i < RANKS.length; i++) {
            if (RANKS[i].equals(rank)) {
                return i;
            }
        }
        return -1; // ไม่ควรเกิดขึ้น
    }
}
