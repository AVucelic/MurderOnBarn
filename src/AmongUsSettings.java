import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AmongUsSettings {

    String serverIP;
    int ipPORT;
    double playerSpeed;
    int numImposters;
    int killCoolDown;
    int killDistance;

    

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

    @XmlElement
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @XmlElement
    public void setIpPORT(int ipPORT) {
        this.ipPORT = ipPORT;
    }

    @XmlElement
    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    @XmlElement
    public void setNumImposters(int numImposters) {
        this.numImposters = numImposters;
    }
    @XmlElement
    public void setKillCoolDown(int killCoolDown) {
        this.killCoolDown = killCoolDown;
    }
    @XmlElement
    public void setKillDistance(int killDistance) {
        this.killDistance = killDistance;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getIpPORT() {
        return ipPORT;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public int getNumImposters() {
        return numImposters;
    }

    public int getKillCoolDown() {
        return killCoolDown;
    }

    public int getKillDistance() {
        return killDistance;
    }
    


    @Override
    public String toString() {
        return "AmongUsSettings [serverIP=" + serverIP + ", ipPORT=" + ipPORT + ", playerSpeed=" + playerSpeed + "]";
    }

}