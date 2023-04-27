
/**
 * EmergencyButton - a more specefic interactable only to be used asan emergecny button 
 * @author Luka Lasic
 * @since 20-4-2023
 */
public class EmergencyButton extends Interactable{

    private int posXBasedOnBack = -1930 + 1930 + 375;
    private int posYBasedOnBack  = -160 + 160 + 425;

    /**
     * A constructor for the emergency button that is sending information to its parent 
     * @param name
     */
    public EmergencyButton(String name) {
        super(name);
        //TODO Auto-generated constructor stub
        setPosXBasedOnBack(posXBasedOnBack);
        setPosYBasedOnBack(posYBasedOnBack);
    }

    
    /** 
     * update -  a method to move the emergency button so it stays in the same place on the map
     * @param x
     * @param y
     */
    public void update(double x,double y){
        //need to alter the update statment in order correcly adjust were the task will be 
        //need to have add to it so it isnt just palce on the top left corner of the background movement 
        int xForInter = (int)x + 1930 + 375;
        int yForInter = (int)y + 160 + 425;
        //System.out.println("Button Emg" +getName() + " X Cord " + xForInter);
        //System.out.println("Button Emg " + getName() + " Y Cord " + yForInter);
        getInteractable().setTranslateX(xForInter);
        getInteractable().setTranslateY(yForInter);
        setPosXBasedOnBack(xForInter);
        setPosYBasedOnBack(yForInter);
        posXBasedOnBack = xForInter;
        posYBasedOnBack = yForInter;
    }

    
    /** 
     * distanceEmgButton - getting the distance between the player and button using distance formula 
     * @param player
     * @return boolean
     */
    public boolean distanceEmgButton(CrewmateRacer player){
        double xDis = 0;
        double yDis = 0;
        synchronized (player) {
           xDis = Math.pow(player.getRacerPosX() - 375, 2);
           yDis = Math.pow(player.getRacerPosY() - 35, 2);
        }
        double distance = Math.sqrt(xDis + yDis);
        //System.out.println("Distance between button " + this.getName() + " and players posistion is:" + distance + "\n\n");
        if (distance < 80) {
           return true;
        } else {
           
            
            
            return false;
              //task.removeHighlight();
           
  
        }
    }
    
}
