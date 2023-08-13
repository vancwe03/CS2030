public class SelfCheckOut extends Server {
    
    SelfCheckOut(int serverNum, double totalTime, int queueNum) {
        super(serverNum, totalTime, queueNum);
    }

    @Override 
    public boolean isHuman() {
        return false;
    }

}
