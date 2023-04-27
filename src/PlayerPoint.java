import java.io.Serializable;

/**
 * PlayerPoint - a class that is meant to be used as a simple veriosn of CrewmateRacer and be able to 
 * be simply sent over the server 
 * @author Luka Lasic
 * @since 20-4-2023
 */

public class PlayerPoint implements Serializable{
    private int x;
    private int y;
    private int index;
    private static final long serialVersionUID = 1234567L;
    private String name;
    private boolean isImposter;
    private int numVotes =0;
    private boolean isAlive;

    private String imageName;

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
    
    /** 
     * @return int
     */
    public int getX() {
        return x;
    }
    
    /** 
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /** 
     * @return int
     */
    public int getY() {
        return y;
    }
    
    /** 
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /** 
     * @return int
     */
    public int getIndex() {
        return index;
    }
    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /** 
     * @return boolean
     */
    public boolean isImposter() {
        return isImposter;
    }

    
    /** 
     * @param isImposter
     */
    public void setImposter(boolean isImposter) {
        this.isImposter = isImposter;
    }
    public void addVotes(){
        numVotes++;
    }

    
    /** 
     * @return int
     */
    public int getNumVotes() {
        return numVotes;
    }

    
    /** 
     * @param numVotes
     */
    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    
    /** 
     * @return boolean
     */
    public boolean isAlive() {
        return isAlive;
    }

    
    /** 
     * @param isAlive
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    
    /** 
     * @return String
     */
    public String getImageName() {
        return imageName;
    }

    
    /** 
     * @param imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    

    
    


}