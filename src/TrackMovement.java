public class TrackMovement{
    private static boolean moveDown;
    private static boolean moveUp;
    private static boolean moveLeft;
    private static boolean moveRight;
    

    public boolean isMoveDown() {
        return moveDown;
    }
    public boolean isMoveUp() {
        return moveUp;
    }
    public boolean isMoveLeft() {
        return moveLeft;
    }
    public boolean isMoveRight() {
        return moveRight;
    }

    public void setMoveDown(boolean moveDown) {
        TrackMovement.moveDown = moveDown;
    }
    public void setMoveUp(boolean moveUp) {
        TrackMovement.moveUp = moveUp;
    }
    public void setMoveLeft(boolean moveLeft) {
        TrackMovement.moveLeft = moveLeft;
    }
    public void setMoveRight(boolean moveRight) {
        TrackMovement.moveRight = moveRight;
    }


}
