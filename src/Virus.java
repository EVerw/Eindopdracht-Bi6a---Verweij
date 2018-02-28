/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Erik
 */
public class Virus {
    private int virusTaxID;
    private String virusLineage;
    private int hostID;
    private String hostNaam;
    
    
    public Virus(){   
    }
    
    public Virus(int vtaxID, String vLineage, int hID, String hNaam) {
        this.virusTaxID = vtaxID;
        virusLineage = vLineage;
        this.hostID = hID;
        hostNaam = hNaam;
    }
    
    public int getVirusTaxID() {
        return virusTaxID;
    }
    
    public String getVirusLineage() {
        return virusLineage;
    }

    public int getHostID() {
        return this.hostID;
    }
    
    public String getHostNaam() {
        return hostNaam;
    }
}
