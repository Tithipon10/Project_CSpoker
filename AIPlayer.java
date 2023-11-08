public class AIPlayer extends Player { // สืบทอดคลาส Player
    public AIPlayer(String name) {// สร้าง constructor ที่รับ parameter เป็น String และเรียก constructor ของคลาสแม่ 
        super(name); 
    }
    @Override 
    public int makeBet(int opponentBet) {  // สร้าง method makeBet ที่รับ parameter เป็น int และเป็น override method ของคลาสแม่
        return super.makeBet(opponentBet); 
    }
}
