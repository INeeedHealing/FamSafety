package alarm;

import alarm.UI.EnterPassword;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

import alarm.UI.options;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class Alarm {

    public static LinkedList<String> resP = new LinkedList<>(); //Speichert Programmnamen
    public static boolean killPs = false;   //Ob Ps geschlossen werden sollen
    public static int avlTime = 190;        //Wie viel Zeit mit Ps
    public static boolean remPs = true;    //Ob Nach Zeit in Ps erinnert werden soll
    public static int freqPs = 30;          //wie oft in min
    public static boolean rem = true;      //ob nach Zeit am Pc erinnert werden soll
    public static int freq = 30;            //wie oft in min
    public static boolean needPass = false; //ob Passwort benötigt wird
    public static String password;          //Passwort VERSCHLÜSSEL MAL!!
    public static boolean passWisRight = false;

    //static final String[] OPTIONNAMES = {"killPs", "avlTime", "remPs", "freqPs", "rem", "freq", "rem15", "needPass"};
    public static void main(String args[]) throws Exception {
        //<editor-fold defaultstate="collapsed" desc="comment">
//        Preferences userRootNode = Preferences.userRoot();
//        Preferences opt;
//        killPs = opt.getBoolean(OPTIONNAMES[0], false);
//        avlTime = opt.getInt(OPTIONNAMES[1], 0);
//        remPs = opt.getBoolean(OPTIONNAMES[2], false);
//        freqPs = opt.getInt(OPTIONNAMES[3], 30);
//        rem = opt.getBoolean(OPTIONNAMES[4], false);
//        freq = opt.getInt(OPTIONNAMES[5], 30);
//        needPass = opt.getBoolean(OPTIONNAMES[6], false);
//
//        System.out.println(avlTime);
//        programms p = new programms();
//        p.setVisible(true);
//</editor-fold>
        String warnig = "Vielleicht solltest du aufhören.";

        //!!!Achtung nur Temporär!!!
        resP.add("witcher3.exe");
        resP.add("FortniteClient-Win64-Shipping.exe");

        boolean remSwOnce = true;
        StopWatch remSw = new StopWatch();

        boolean pIsRunning = false;
        boolean remPsSwOnce = true;
        StopWatch remPsSw = new StopWatch();

        boolean rem15Once = true;
        boolean rem2Once = true;

        Alarm al = new Alarm();
        
        options op = new options();
        //Wenn Passwort verlangt wird
        if (needPass && password != null) {
            EnterPassword eP = new EnterPassword();
            eP.setVisible(true);
            eP.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else {
            op.setVisible(true);
            op.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        while (true) {
            if (!op.isVisible() && passWisRight) {
                op.setVisible(true);
                passWisRight = false;
            }
            if (rem) {
                if (remSwOnce) {
                    remSwOnce = false;
                    remSw.start();
                    System.out.println("remSw gestartet");
                }
                if (remSw.isStoped()) {
                    remSw.moveOn();
                    System.out.println("RemSw fortgesetzt");
                }
                if (remSw.getElapsedTimeSecs() != 0 && remSw.getElapsedTimeSecs() % (freq * 60) == 0) {
                    String titel = "Du bist jetzt " + al.minToHour(remSw.getElapsedTimeMins()) + " am Pc!";
                    al.displayTray(titel, warnig);
                    System.out.println("Warnung " + remSw.getElapsedTimeSecs() % (freq * 60));
                }
                System.out.println("Time: " + remSw.getElapsedTimeSecs());
            } else if (remSw.isRunning()) {
                remSw.stop();
                System.out.println("RemSw gestopt");
            }
            if (remPs || killPs) {
                for (String p : resP) {
                    if (TaskEdit.isProcessRunning(p)) {
                        pIsRunning = true;
                        break;
                    }
                    pIsRunning = false;
                }
                if (pIsRunning) {
                    if (remPsSwOnce) {
                        remPsSwOnce = false;
                        remPsSw.start();
                        System.out.println("remPsSw gestartet");
                    }
                    if (remPsSw.isStoped()) {
                        remSw.moveOn();
                        System.out.println("RemPsSw fortgesetzt");
                    }
                    if (remPsSw.getElapsedTimeSecs() != 0 && remPsSw.getElapsedTimeSecs() % (freqPs * 60) == 0 && remPs) {
                        String titel = "Du bist jetzt " + al.minToHour(remPsSw.getElapsedTimeMins()) + " in beschränkten Programmen am Pc!";
                        al.displayTray(titel, warnig);
                        System.out.println("Warnung " + remPsSw.getElapsedTimeSecs() % (freqPs * 60));
                    }
                    System.out.println("PTime: " + remPsSw.getElapsedTimeSecs());
                }
                if (killPs && avlTime < remPsSw.getElapsedTimeMins()) {
                    for (String p : resP) {
                        if (TaskEdit.isProcessRunning(p)) {
                            TaskEdit.killProcess(p);
                        }
                    }
                }
            } else if (remPsSw.isRunning()) {
                remPsSw.stop();
                System.out.println("RemPsSw gestopt");
            }
            if ((killPs || remPs)) {
                if (freqPs - remPsSw.getElapsedTimeMins() < 15 && rem15Once) {
                    String titel = "Du musst in 15 min aufhören zu Spielen";
                    al.displayTray(titel, "Beende deine jetzige Aktivität.");
                    rem15Once = false;
                }

                if (freqPs - remPsSw.getElapsedTimeMins() < 2 && rem2Once) {
                    String titel = "Du musst in 2 min aufhören zu Spielen";
                    al.displayTray(titel, "Beende deine jetzige Aktivität!");
                    rem15Once = false;
                }
            }

            TimeUnit.SECONDS.sleep(1);
        }

    }

    void displayTray(String titel, String text) throws Exception {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(titel, text, MessageType.INFO);
    }

    String minToHour(int minIn) {
        int hour = minIn / 60;
        int minOut = minIn % 60;
        return String.valueOf(hour) + ":" + String.valueOf(minOut) + " h";
    }
}

// <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
//    public static void saveOptions() throws Exception{
//        Preferences opt = Preferences.systemNodeForPackage(alarm.Alarm.class.getClass());
//        opt.putBoolean(OPTIONNAMES[0], killPs);
//        opt.putInt(OPTIONNAMES[1], avlTime);
//        opt.putBoolean(OPTIONNAMES[2], remPs);
//        opt.putInt(OPTIONNAMES[3], freqPs);
//        opt.putBoolean(OPTIONNAMES[4], rem);
//        opt.putInt(OPTIONNAMES[5], freq);
//        
//        opt.flush();
//    }
//    static void readOptions() {
//        try {
//            FileInputStream fis = new FileInputStream(OPTIONSFILE);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader bur = new BufferedReader(isr);
//            String sLine;
//            
//        } catch (IOException eIO) {
//            try {
//                File opFile = new File(OPTIONSFILE);
//                opFile.createNewFile();
//                System.out.println("Options Datei wurde erstellt");
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//    }
//
//    static void readResP() {
//        try {
//            FileInputStream fis = new FileInputStream(RESTRICTEDFILE);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader bur = new BufferedReader(isr);
//            String sLine;
//            do {
//                sLine = bur.readLine();
//                System.out.println("Inhalt ResP: " + sLine);
//                resP.add(sLine);
//            } while (null != sLine);
//            bur.close();
//
//            System.out.println("Restricted wurde gelesen");
//
//        } catch (IOException eIO) { //Wenn File nicht existiert
//            try {
//                File resFile = new File(RESTRICTEDFILE);
//                resFile.createNewFile();
//                System.out.println("Restricted Datei wurde erstellt");
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//}

// </editor-fold>
