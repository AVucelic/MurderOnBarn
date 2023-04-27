/**
 * TrackMovement - a class to hold the movenet keys of the player
 * @author Luka Lasic
 * @since 20-4-2023
 */
public class TrackMovement{
    private static boolean moveDown;
    private static boolean moveUp;
    private static boolean moveLeft;
    private static boolean moveRight;
    

    
    /** 
     * @return boolean
     */
    public boolean isMoveDown() {
        return moveDown;
    }
    
    /** 
     * @return boolean
     */
    public boolean isMoveUp() {
        return moveUp;
    }
    
    /** 
     * @return boolean
     */
    public boolean isMoveLeft() {
        return moveLeft;
    }
    
    /** 
     * @return boolean
     */
    public boolean isMoveRight() {
        return moveRight;
    }

    
    /** 
     * @param moveDown
     */
    public void setMoveDown(boolean moveDown) {
        TrackMovement.moveDown = moveDown;
    }
    
    /** 
     * @param moveUp
     */
    public void setMoveUp(boolean moveUp) {
        TrackMovement.moveUp = moveUp;
    }
    
    /** 
     * @param moveLeft
     */
    public void setMoveLeft(boolean moveLeft) {
        TrackMovement.moveLeft = moveLeft;
    }
    
    /** 
     * @param moveRight
     */
    public void setMoveRight(boolean moveRight) {
        TrackMovement.moveRight = moveRight;
    }


}
