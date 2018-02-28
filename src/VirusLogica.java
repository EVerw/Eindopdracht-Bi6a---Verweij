
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.*;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Erik
 */
public class VirusLogica {

    private static final ArrayList<Virus> virusLijst = new ArrayList();
    private static final ArrayList<Virus> othersLijst = new ArrayList();
    private static final ArrayList<Virus> ssRNALijst = new ArrayList();
    private static final ArrayList<Virus> s_VLijst = new ArrayList();
    private static final ArrayList<Virus> ssDNALijst = new ArrayList();
    private static final ArrayList<Virus> viroidLijst = new ArrayList();
    private static final ArrayList<Virus> rvLijst = new ArrayList();
    private static final ArrayList<Virus> dsDNALijst = new ArrayList();
    private static final ArrayList<Virus> dsRNALijst = new ArrayList();
    private static final Set<String> naamID = new HashSet();
    private static JFileChooser fileChooser;
    private static final List<String> lijst = new ArrayList();
    private static final List<Virus> iLijstTA1 = new ArrayList();
    private static final List<Virus> iLijstTA2 = new ArrayList();
    private static final Set<Virus> gezamelijk = new HashSet();
    private static final HashSet<Integer> gezamelijkID = new HashSet();
    private static HashMap<Integer, Integer> hostAantalHashMap = new HashMap();
    public static final List<String> hostLijst = Arrays.asList("None", "Others", "ssRNA", "Satellite/Virophage", "ssDNA", "Viroid", "Retrovirusen", "dsDNA", "dsRNA");

    public static String openBestand() {
        int reply;
        File selectedFile = null;
        fileChooser = new JFileChooser();
        reply = fileChooser.showOpenDialog(null);
        if (reply == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        leesBestand(selectedFile.getAbsolutePath());
        return selectedFile.getAbsolutePath();
    }

    private static void leesBestand(String pad) {   //leest het bestand ,stuurt informatie naar objectenmaker en vult combobox 1 
        BufferedReader inFile;
        try {
            inFile = new BufferedReader(new FileReader(pad));
            @SuppressWarnings("UnusedAssignment")
            String line = null;
            inFile.readLine();
            int hostID;
            int taxID;
            while ((line = inFile.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                try {
                    hostID = Integer.parseInt(parts[7]);
                } catch (NumberFormatException ex) {
                    hostID = 0;
                }
                try {
                    taxID = Integer.parseInt(parts[0]);
                } catch (NumberFormatException ex) {
                    taxID = 0;
                }
                objectMaker(taxID, parts[2], hostID, parts[8]);
            }
            inFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VirusLogica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VirusLogica.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboBox1Vuller();
    }

    private static void objectMaker(int taxID, String vLineage, int hID, String hNaam) {    //Maakt virus objecten en stuurt ze naar soortLijstVuller om bij de goede soort gezet te worden
        Virus virus = new Virus(taxID, vLineage, hID, hNaam);
        virusLijst.add(virus);
        soortLijstVuller(virus, vLineage);
    }

    public static ArrayList<Virus> soort() { //pakt de juist lijst bij soort en geeft deze terug
        String soort = (String) VirusGUI.jComboBox1.getSelectedItem();
        ArrayList<Virus> soortLijst = new ArrayList<>();
        if (soort.toLowerCase().contains("ssDNA") == true) {
            soortLijst = ssDNALijst;
        } else if (soort.toLowerCase().contains("retrovirusen") == true) {
            soortLijst = rvLijst;
        } else if (soort.toLowerCase().contains("virophage") == true) {
            soortLijst = s_VLijst;
        } else if (soort.toLowerCase().contains("viroid") == true) {
            soortLijst = viroidLijst;
        } else if (soort.toLowerCase().contains("dsrna") == true) {
            soortLijst = dsRNALijst;
        } else if (soort.toLowerCase().contains("dsDNA") == true) {
            soortLijst = dsDNALijst;
        } else if (soort.toLowerCase().contains("ssRNA") == true) {
            soortLijst = ssRNALijst;
        } else if (soort.toLowerCase().contains("none") == true) {
            soortLijst = virusLijst;
        } else {
            soortLijst = othersLijst;
        }
        return soortLijst;
    }

    public static void virusBijSoort(ArrayList<Virus> lijstv) { //Zoekt de virussen van een soort en pakt de unieke host ids en bijbehorende namen
        for (Virus virus : lijstv) {
            String naam = virus.getHostNaam();
            Integer hostid = virus.getHostID();
            String naamid = hostid.toString() + " " + naam;
            naamID.add(naamid);
        }
        comboBox23Vuller(naamID);
    }

    public static void comboBox1Vuller() { //Vult combo box 1 met hosts
        for (String s : hostLijst) {
            VirusGUI.jComboBox1.addItem(s);
        }
    }

    private static void comboBox23Vuller(Set set) { // Vult combobox 2 en 3 met host id's en de naam van de host
        lijst.addAll(set);
        for (String s : lijst) {
            VirusGUI.jComboBox2.addItem(s);
            VirusGUI.jComboBox3.addItem(s);
        }
    }

    private static void soortLijstVuller(Virus virus, String vLineage) { //Maakt per soort virus een lijst met de bijbehorende virus objecten
        if (vLineage.toLowerCase().contains("ssdna") == true) {
            ssDNALijst.add(virus);
        } else if (vLineage.toLowerCase().contains("retrovirus") == true) {
            rvLijst.add(virus);
        } else if (vLineage.toLowerCase().contains("virophage") == true || vLineage.toLowerCase().contains("satellite") == true) {
            s_VLijst.add(virus);
        } else if (vLineage.toLowerCase().contains("viroid") == true) {
            viroidLijst.add(virus);
        } else if (vLineage.toLowerCase().contains("dsrna") == true) {
            dsRNALijst.add(virus);
        } else if (vLineage.toLowerCase().contains("dsdna") == true) {
            dsDNALijst.add(virus);
        } else if (vLineage.toLowerCase().contains("ssrna") == true) {
            ssRNALijst.add(virus);
        } else {
            othersLijst.add(virus);
        }
    }

    public static void virusBijHost(int nummer, String host) {
        Set<Virus> virusIDSet = new HashSet<>();
        String[] naamID = host.split(" ");
        for (Virus virus : soort()) {
            try {
                int nID = Integer.parseInt(naamID[0]);
                if (nID == virus.getHostID()) {
                    int virusID = virus.getVirusTaxID();
                    virusIDSet.add(virus);
                }
            } catch (NumberFormatException ex) {
            }
        }
        if (nummer == 1) {
            textArea1Vuller(virusIDSet);
        } else if (nummer == 2) {
            textArea2Vuller(virusIDSet);
        }
    }

    private static void vergelijker() {
        Set<Virus> vergelijking = new HashSet<>();
        if (iLijstTA2.size() > 0 && iLijstTA1.size() > 0) {
            VirusGUI.jTextArea3.setText(null);
            vergelijking.clear();
            gezamelijk.clear();
            for (Virus virus2 : iLijstTA2) {
                vergelijking.add(virus2);
            }
            for (Virus virus1 : iLijstTA1) {
                if (!vergelijking.add(virus1)) {
                    gezamelijk.add(virus1);
                    gezamelijkID.add(virus1.getVirusTaxID());
                }
            }
            for (Virus virus : gezamelijk) {
                String s = Integer.toString(virus.getVirusTaxID());
                VirusGUI.jTextArea3.append(s + "\n");
            }
        }
    }

    private static void textArea1Vuller(Set lijst) {
        iLijstTA1.clear();
        iLijstTA1.addAll(lijst);
        VirusGUI.jTextArea1.setText("");
        for (Virus virus : iLijstTA1) {
            String s = Integer.toString(virus.getVirusTaxID());
            VirusGUI.jTextArea1.append(s + "\n");
        }
        vergelijker();
    }

    private static void textArea2Vuller(Set lijst) {
        iLijstTA2.clear();
        iLijstTA2.addAll(lijst);
        VirusGUI.jTextArea2.setText(null);
        for (Virus virus : iLijstTA2) {
            String s = Integer.toString(virus.getVirusTaxID());
            VirusGUI.jTextArea2.append(s + "\n");
        }
        vergelijker();
    }

    static void radioButton1() {
        TreeSet<Integer> tset = new TreeSet<Integer>();
        VirusGUI.jTextArea3.setText(null);
        for (Virus virus : gezamelijk) {
            tset.add(virus.getVirusTaxID());
        }
        for (Integer i : tset) {
            String s = i.toString();
            VirusGUI.jTextArea3.append(s + "\n");
        }
    }

    static void radioButton23() {
        TreeSet<String> tset = new TreeSet();
        VirusGUI.jTextArea3.setText(null);
        for (Virus virus : gezamelijk) {
            int virusID = virus.getVirusTaxID();
            int count = 0;
            for (Virus virusAlle : virusLijst) {
                if (virusID == virusAlle.getVirusTaxID()) {
                    count++;
                }
            }
            tset.add(count + "-" + virusID);
        }
        List<String> LijstTel = new ArrayList<>();
        LijstTel.addAll(tset);
        Collections.reverse(LijstTel);
        for (String s : LijstTel) {
            String[] countString = s.split("-");
            VirusGUI.jTextArea3.append(countString[1] + "\n");
        }
    }

    static void radioButton2() {

        Set<Integer> hostAantalSet = new HashSet<Integer>();
        for (Virus alleVirussen : virusLijst) {
            if (gezamelijkID.contains(alleVirussen.getVirusTaxID())) {
                try {
                    hostAantalHashMap.put(alleVirussen.getVirusTaxID(), hostAantalHashMap.get(alleVirussen.getVirusTaxID()) + 1);
                } catch (NullPointerException ex) {
                    hostAantalHashMap.put(alleVirussen.getVirusTaxID(), 0);
                }
            }
        }
        while (hostAantalHashMap.isEmpty() == false) {
            hostAantalSet.add(radioButton2MaxWaarde());
        }
        for (Integer i : hostAantalSet) {
            String s = i.toString();
            VirusGUI.jTextArea3.append(s + "\n");
        }
    }
    
    private static Integer radioButton2MaxWaarde(){
        int terug = 0;
        int delete = 0;
        int maxValueInMap = (Collections.max(hostAantalHashMap.values()));
            for (Entry<Integer, Integer> entry : hostAantalHashMap.entrySet()) {
                if (entry.getValue() == maxValueInMap) {
                    delete = entry.getKey();
                    terug = entry.getKey();
                }
            }
        hostAantalHashMap.remove(delete);
        return terug;
    }

    static void radioButton3() {
        String vLineage = null;
        VirusGUI.jTextArea3.setText(null);
        Set<String> rb3 = new HashSet<>();
        String rb3String = null;
        for (Virus virus : gezamelijk) {
            vLineage = virus.getVirusLineage();
            rb3String = (vLineage + "\n" + Integer.toString(virus.getVirusTaxID()));
            rb3.add(rb3String);
        }
        Set<String> tset = new TreeSet<>(rb3);
        for (String s : tset) {
            String[] lineage = s.split("\n");
            VirusGUI.jTextArea3.append(lineage[1] + "\n");
        }
    }

}
