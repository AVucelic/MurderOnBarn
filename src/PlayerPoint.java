import java.io.Serializable;

public class PlayerPoint implements Serializable{
    private int x;
    private int y;
    private int index;
    private static final long serialVersionUID = 1234567L;
    private String name;
    private boolean isImposter;
    private int numVotes =0;
    private boolean isAlive;

    public PlayerPoint(int x, int y,int index){
        this.x =x;
        this.y =y;
        this.index = index;
        this.isAlive = true;
    }

    public PlayerPoint(int x, int y,int index,String name){
        this.x =x;
        this.y =y;
        this.index = index;
        this.name = name;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isImposter() {
        return isImposter;
    }

    public void setImposter(boolean isImposter) {
        this.isImposter = isImposter;
    }
    public void addVotes(){
        numVotes++;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    

    
    


}