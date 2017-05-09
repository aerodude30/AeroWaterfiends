package Aerowaterfiends;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.powerbot.game.api.methods.Environment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager  {

    public static ConcurrentHashMap<String,Serializable> queueInfo = new ConcurrentHashMap<String, Serializable>();
    public static void startHashMap() {
        queueInfo.put("ScriptName", "null");
        queueInfo.put("Version", 0);
        queueInfo.put("Runtime", 0);
        queueInfo.put("Status", "null");
        queueInfo.put("Attack", 0);
        queueInfo.put("Strength", 0);
        queueInfo.put("Defense", 0);
        queueInfo.put("Magic", 0);
        queueInfo.put("Ranged", 0);
        queueInfo.put("Prayer", 0);
        queueInfo.put("Runecrafting", 0);
        queueInfo.put("Construction", 0);
        queueInfo.put("Dungeoneering", 0);
        queueInfo.put("Constitution", 0);
        queueInfo.put("Agility", 0);
        queueInfo.put("Herblore", 0);
        queueInfo.put("Thieving", 0);
        queueInfo.put("Crafting", 0);
        queueInfo.put("Fletching", 0);
        queueInfo.put("Slayer", 0);
        queueInfo.put("Hunter", 0);
        queueInfo.put("Mining", 0);
        queueInfo.put("Smithing", 0);
        queueInfo.put("Fishing", 0);
        queueInfo.put("Cooking", 0);
        queueInfo.put("Firemaking", 0);
        queueInfo.put("Woodcutting", 0);
        queueInfo.put("Farming", 0);
        queueInfo.put("Summoning", 0);
        queueInfo.put("Username", "null");
        System.out.println("Initial HashMap Deployed");

    }



    public static void stop() {
        try {
            ScriptManager.queueInfo.clear();
            URL submit = new URL("http://www.aeroscripts.x10.bz/dashboard.php?ScriptName="
                    + null +
                    "&Version=" + 0 +
                    "&Runtime=" + 0 +
                    "&Status=" +  null +
                    "&Ranged=" +  0);
            URLConnection con = submit.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            System.out.println("Closed Queue'd Data : " + submit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Deleted Script from Dashboard");
    }

    public static void uploadFile() {
        File path = Environment.getStorageDirectory();
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
            ImageIO.write(bufferedImage, "png", new File(path + "/img_"+ queueInfo.get("SessionID")));

            client.connect("*******");
            client.login("*******", "*********");
            client.enterLocalPassiveMode();
            System.out.println(client.getReplyString());

            client.setFileType(FTP.BINARY_FILE_TYPE);

            System.out.println(client.getReplyString());
            String filename = "img_" + queueInfo.get("SessionID");
            fis = new FileInputStream(path + "/img_"+ queueInfo.get("SessionID"));

            client.storeFile(filename, fis);
            System.out.println("Stored File Successfully");
            client.logout();
        } catch (Exception e) {
            System.out.println("Error_1");
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }
        }
    }

    public  static void sendData() {
                try {
                    URL dashboard = new URL("http://aeroscripts.x10.bz/dashboard.php?ScriptName="
                            + ScriptManager.queueInfo.get("ScriptName") +
                            "&Version=" + ScriptManager.queueInfo.get("Version") +
                            "&Runtime=" + ScriptManager.queueInfo.get("Runtime") +
                            "&Status=" +  ScriptManager.queueInfo.get("Status") +
                            "&Ranged=" +   ScriptManager.queueInfo.get("Ranged") +
                             "&Attack=" + ScriptManager.queueInfo.get("Attack") +
                            "&sessionID=" + AeroWaterfiends.lines.get(2) +
                             "&Defense=" + ScriptManager.queueInfo.get("Defense") +
                              "&Strength=" + ScriptManager.queueInfo.get("Strength") +
                              "&Magic=" + ScriptManager.queueInfo.get("Magic"));
                    URLConnection  con = dashboard.openConnection();
                    con.setDoInput(true);
                    con.setUseCaches(false);
                    con.setDoOutput(true);
                    Collection<Serializable> HashMap = queueInfo.values();
                    System.out.println("[INFO] SENT DASHBOARD DATA: " + dashboard);
                    System.out.println("[INFO] HASHMAP: " + HashMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

    }

}

