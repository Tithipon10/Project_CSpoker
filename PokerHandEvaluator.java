import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerHandEvaluator {

    public static HandRanking evaluateHand(List<Card> hand) { // ตรวจสอบ hand ranking ของผู้เล่น
        if (isRoyalFlush(hand)) {
            return HandRanking.ROYAL_FLUSH;
        } else if (isStraightFlush(hand)) {
            return HandRanking.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(hand)) {
            return HandRanking.FOUR_OF_A_KIND;
        } else if (isFullHouse(hand)) {
            return HandRanking.FULL_HOUSE;
        } else if (isFlush(hand)) {
            return HandRanking.FLUSH;
        } else if (isStraight(hand)) {
            return HandRanking.STRAIGHT;
        } else if (isThreeOfAKind(hand)) {
            return HandRanking.THREE_OF_A_KIND;
        } else if (isTwoPair(hand)) {
            return HandRanking.TWO_PAIR;
        } else if (isOnePair(hand)) {
            return HandRanking.ONE_PAIR;
        } else {
            return HandRanking.HIGH_CARD;
        }
    }
    

    private static boolean isRoyalFlush(List<Card> hand) { // ตรวจสอบว่าเป็น royal flush หรือไม่
        if (!isFlush(hand))
            return false;
        List<String> requiredRanks = List.of("A", "B+", "B", "C+", "C");
        for (Card card : hand) {
            if (!requiredRanks.contains(card.getRank())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isStraightFlush(List<Card> hand) { // ตรวจสอบว่าเป็น straight flush หรือไม่
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(List<Card> hand) { // ตรวจสอบว่าเป็น four of a kind หรือไม่
        Map<String, Integer> rankCount = countRanks(hand);
        return rankCount.values().contains(4);
    }

    private static boolean isFullHouse(List<Card> hand) { // ตรวจสอบว่าเป็น full house หรือไม่
        Map<String, Integer> rankCount = countRanks(hand);
        boolean hasThreeOfAKind = false;
        boolean hasPair = false;
        for (int val : rankCount.values()) {
            if (val == 3) {
                hasThreeOfAKind = true;
            } else if (val == 2) {
                hasPair = true;
            }
        }
        return hasThreeOfAKind && hasPair;
    }

    public static boolean isFlush(List<Card> cards) { // ตรวจสอบว่าเป็น flush หรือไม่
        if (cards.isEmpty())
            return false;
        Card.Suit suit = cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }
 
    private static boolean isStraight(List<Card> hand) { // ตรวจสอบว่าเป็น straight หรือไม่
        List<String> ranksOrdered = List.of("A", "B+", "B", "C+", "C", "D+", "D", "F");
        List<String> handRanks = new ArrayList<>();
        for (Card card : hand) {
            handRanks.add(card.getRank());
        }
        for (int i = 0; i <= ranksOrdered.size() - 5; i++) {
            List<String> subList = ranksOrdered.subList(i, i + 5);
            if (handRanks.containsAll(subList)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> hand) { // ตรวจสอบว่าเป็น three of a kind หรือไม่
        Map<String, Integer> rankCount = countRanks(hand);
        return rankCount.values().contains(3);
    }

    private static boolean isTwoPair(List<Card> hand) { // ตรวจสอบว่าเป็น two pair หรือไม่
        Map<String, Integer> rankCount = countRanks(hand);
        int pairCount = 0;
        for (int val : rankCount.values()) {
            if (val == 2) {
                pairCount++;
            }
        }
        return pairCount == 2;
    }

    private static boolean isOnePair(List<Card> hand) { // ตรวจสอบว่าเป็น one pair หรือไม่
        Map<String, Integer> rankCount = countRanks(hand);
        int pairCount = 0;
        for (int val : rankCount.values()) {
            if (val == 2) {
                pairCount++;
            }
        }
        return pairCount == 1;
    }

    private static Map<String, Integer> countRanks(List<Card> hand) { // นับจำนวน rank ของไพ่
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount;
    }
}
