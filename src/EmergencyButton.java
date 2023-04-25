public class EmergencyButton extends Interactable{

    private int posXBasedOnBack = -1930 + 1930 + 375;
    private int posYBasedOnBack  = -160 + 160 + 35;

    public EmergencyButton(String name) {
        super(name);
        //TODO Auto-generated constructor stub
        setPosXBasedOnBack(posXBasedOnBack);
        setPosYBasedOnBack(posYBasedOnBack);
    }

    public void update(double x,double y){
        //need to alter the update statment in order correcly adjust were the task will be 
        //need to have add to it so it isnt just palce on the top left corner of the background movement 
        int xForInter = (int)x + 1930 + 375;
        int yForInter = (int)y + 160 + 35;
        //System.out.println("Button Emg" +getName() + " X Cord " + xForInter);
        //System.out.println("Button Emg " + getName() + " Y Cord " + yForInter);
        getInteractable().setTranslateX(xForInter);
        getInteractable().setTranslateY(yForInter);
        setPosXBasedOnBack(xForInter);
        setPosYBasedOnBack(yForInter);
        posXBasedOnBack = xForInter;
        posYBasedOnBack = yForInter;
    }

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
