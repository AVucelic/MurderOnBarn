
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * AmongUsSettings - all the settings for the game 
 * @author Luka Lasic
 * @since 20-4-2023
 * 
 * 
 * javadoc -d /Users/luka/Desktop/Computer_Science/Programming_2/Among_US_Project_CLEAN_UP/src -sourcepath src -subpackages AmongUsSettings.java

 */

@XmlRootElement
public class AmongUsSettings {

    String serverIP;
    int ipPORT;
    double playerSpeed;
    int numImposters;
    int killCoolDown;
    int killDistance;

    
    /**
     * A constructor to put all the settings for the game 
     * @param serverIP
     * @param ipPORT
     * @param playerSpeed
     * @param numImposters
     * @param killCoolDown
     * @param killDistance
     */
    public AmongUsSettings(String serverIP, int ipPORT, double playerSpeed, int numImposters, int killCoolDown,
            int killDistance) {
        this.serverIP = serverIP;
        this.ipPORT = ipPORT;
        this.playerSpeed = playerSpeed;
        this.numImposters = numImposters;
        this.killCoolDown = killCoolDown;
        this.killDistance = killDistance;
    }

    public AmongUsSettings() {
        
    }

    public AmongUsSettings(String serverIP, int ipPORT, double playerSpeed) {
        this.serverIP = serverIP;
        this.ipPORT = ipPORT;
        this.playerSpeed = playerSpeed;
    }

    
    /** 
     * @param serverIP
     */
    @XmlElement
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    
    /** 
     * @param ipPORT
     */
    @XmlElement
    public void setIpPORT(int ipPORT) {
        this.ipPORT = ipPORT;
    }

    
    /** 
     * @param playerSpeed
     */
    @XmlElement
    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    
    /** 
     * @param numImposters
     */
    @XmlElement
    public void setNumImposters(int numImposters) {
        this.numImposters = numImposters;
    }
    
    /** 
     * @param killCoolDown
     */
    @XmlElement
    public void setKillCoolDown(int killCoolDown) {
        this.killCoolDown = killCoolDown;
    }
    
    /** 
     * @param killDistance
     */
    @XmlElement
    public void setKillDistance(int killDistance) {
        this.killDistance = killDistance;
    }

    
    /** 
     * @return String
     */
    public String getServerIP() {
        return serverIP;
    }

    
    /** 
     * @return int
     */
    public int getIpPORT() {
        return ipPORT;
    }

    
    /** 
     * @return double
     */
    public double getPlayerSpeed() {
        return playerSpeed;
    }

    
    /** 
     * @return int
     */
    public int getNumImposters() {
        return numImposters;
    }

    
    /** 
     * @return int
     */
    public int getKillCoolDown() {
        return killCoolDown;
    }

    
    /** 
     * @return int
     */
    public int getKillDistance() {
        return killDistance;
    }
    


    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "AmongUsSettings [serverIP=" + serverIP + ", ipPORT=" + ipPORT + ", playerSpeed=" + playerSpeed + "]";
    }

}