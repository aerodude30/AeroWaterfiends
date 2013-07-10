/**
 * Created with IntelliJ IDEA.
 * User: Christian
 * Date: 5/28/13
 * Time: 9:10 AM
 * To change this template use File | Settings | File Templates.
 */
package Aerowaterfiends;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.tab.Summoning.Familiar;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;


@Manifest(authors = { "ChristianBartram" }, name = "AeroWaterfiends", description = "Kill waterfiends, collects charms, makes money." )
public class AeroWaterfiends extends ActiveScript implements MessageListener, PaintListener, MouseListener {
    public	int fiendsKilled, i, charmsGained;
    public long startTime;
    private long startTask = System.currentTimeMillis();
    public int startExp = Skills.getExperience(Skills.RANGE);   //hooks broken...
    public int rangeExpGained = Skills.getExperience(Skills.RANGE) - startExp;
    Rectangle close = new Rectangle(481, 392, 30, 29);
    Rectangle open = new Rectangle(481, 392, 30, 29);
    private final Image button = getImage("http://i1202.photobucket.com/albums/bb362/aerodude30/close-open-1.png");
    private final Image img1 = getImage("http://i1202.photobucket.com/albums/bb362/aerodude30/paintBackground_zps219c8d0b-1_zps8a34a415.png");
    Point p;
    boolean hide = false;
    public String status;

    Area BARBARIAN_OUTPOST = new Area(new Tile[] { new Tile(2528, 3580, 0),  new Tile(2515, 3560, 0)});
    Area ANCIENT_CAVERN = new Area(new Tile[] { new Tile(1772, 5340, 0), new Tile(1766, 5366, 0)});

    Tile[] SUMMONING_OBELISK = new Tile[] { new Tile(2517, 3568, 0), new Tile(2517, 3571, 0), new Tile(2518, 3576, 0),
            new Tile(2518, 3577, 0), new Tile(2518, 3578, 0), new Tile(2518, 3580, 0),
            new Tile(2516, 3583, 0), new Tile(2516, 3586, 0), new Tile(2515, 3588, 0) };
    Tile[] WHIRLPOOL = new Tile[] { new Tile(2515, 3586, 0), new Tile(2518, 3582, 0), new Tile(2517, 3577, 0),
            new Tile(2518, 3572, 0), new Tile(2518, 3567, 0), new Tile(2517, 3562, 0),
            new Tile(2514, 3558, 0), new Tile(2514, 3553, 0), new Tile(2514, 3548, 0),
            new Tile(2513, 3543, 0), new Tile(2513, 3538, 0), new Tile(2511, 3533, 0),
            new Tile(2510, 3528, 0), new Tile(2509, 3523, 0), new Tile(2509, 3518, 0),
            new Tile(2510, 3514, 0) };
    Tile[] WATERFIENDS = new Tile[] { new Tile(1767, 5361, 0), new Tile(1758, 5357, 0), new Tile(1755, 5354, 0) };

    Tile[] TO_BANK = new Tile[] { new Tile(2963, 3381, 0), new Tile(2961, 3381, 0), new Tile(2958, 3381, 0),
            new Tile(2954, 3381, 0), new Tile(2949, 3379, 0), new Tile(2946, 3375, 0),
            new Tile(2945, 3369, 0) };
    /**
     *
    Defines Variables that relate to RSBOT class files
     */

    username gui = new username();
    private SecureRandom random = new SecureRandom();

    HashMap<String, Serializable> sessionID = new HashMap<String, Serializable>();
    public static ConcurrentHashMap<Integer, String> lines = new ConcurrentHashMap<Integer, String>();

    private long summoningPercent = Math.round(Summoning.getPoints() * 100.0 / Skills.getRealLevel(Skills.SUMMONING));
    SceneObject obelisk = SceneEntities.getNearest(29943);
    SceneObject stairs = SceneEntities.getNearest(25338);
    NPC Banker = NPCs.getNearest(6200);
    public SceneObject whirlpool = SceneEntities.getNearest(Constants.WHIRLPOOL);
    public NPC waterfiend = NPCs.getNearest(Constants.WATERFIEND);
    public  GroundItem g = GroundItems.getNearest(Constants.LOOT_IDS);
    public GroundItem fullLoot = GroundItems.getNearest(Constants.FULL_LOOT_IDS);
    Timer t = new Timer();
    public boolean userIsValidated;

    private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    private Tree jobContainer = null;
    public synchronized final void provide(final Node... jobs) {
        for (final Node job : jobs) {
            if(!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public int loop() {
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(10, 50);
    }

    public  int getFiendsKilled() {
        if(Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getAnimation() == 300) {
            fiendsKilled++;
            while(Players.getLocal().getInteracting().getAnimation() == 300) {
                Task.sleep(50);
            }

        }
        return fiendsKilled;
    }



    public String sessionId = new BigInteger(130, random).toString(32);


    public void updateHashMap() {
                ScriptManager.queueInfo.put("ScriptName","AeroWaterfiends");
                ScriptManager.queueInfo.put("Version", 1.0);
                ScriptManager.queueInfo.put("Runtime", System.currentTimeMillis()-startTime);
                ScriptManager.queueInfo.put("Status", status);  //  this line may throw a NP Exception because status is null when onStart() is called.
                ScriptManager.queueInfo.put("Ranged", rangeExpGained);
                ScriptManager.queueInfo.put("sessionID", sessionId);

                //TODO put info for strength attack defense magic into HashMap

                if(ScriptManager.queueInfo.size() >= 1) {
                } else {
                    if(ScriptManager.queueInfo.size() < 1) {
                        System.out.println("HashMap contains No Data to Refresh");
                    }
                }
    }


        @Override
    public void onStart() {
        status = "GUI";
        ScriptManager.startHashMap();
        gui.setVisible(true);
        while(gui.isVisible()) {
            updateHashMap();
            sleep(50);
        }
            t.schedule(new TimerTask() {
                @Override
            public void run() {
                  //ScriptManager.uploadFile();
                    if(userIsValidated) {
                    updateHashMap();
                    ScriptManager.sendData();
                    }
                }
            }, 0 , 6 * 1000);

        startTime = System.currentTimeMillis();
        provide(new bank());
        provide(new teleport());
        provide(new enterWater());
        provide(new eat());
        provide(new fightWaterfiends());
        provide(new exit());

    }

   @Override
    public void onStop() {
        t.purge();
        t.cancel();
        ScriptManager.stop();
        System.out.println("Timer canceled, Data Purged, Dashboard Cleared.");
        fiendsKilled = getFiendsKilled();
            try {
                URL submit = new URL(
                        "http://www.aeroscripts.x10.bz/submitdata.php?user="
                                + Environment.getDisplayName() + "&timerun="
                                + (System.currentTimeMillis() - startTime)
                                + "&variable1=" + charmsGained + "&variable2="
                                + fiendsKilled + "&variable3=" + rangeExpGained);
                URLConnection con = submit.openConnection();
                System.out.println("Connection Opened");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                System.out.println("Signature Data Sent " + submit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private Image getImage(String url) {

        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private boolean inventoryContains(int... i) {
        for (Item ii : Inventory.getItems()) {
            for (int iii : i) {
                if (ii.getId() == iii) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clickStairs() {
        if(stairs != null && Game.getPlane() == 1) {
            status = "Climbing";
            stairs.interact("Climb-down");
            sleep(2000, 3000);
        }
    }

    private void loot() {
        status = "Looting Items";
        if(g != null && g.isOnScreen()) {
            g.interact("Take", g.getGroundItem().getName());
            Utilities.sleepWhile(new Condition() {
                @Override
            public boolean validate() {
                    return Players.getLocal().isMoving();
                }
            }, 6000);
            if(Inventory.isFull() || Inventory.getCount() >= 27) {
                fullLoot.interact("Take", fullLoot.getGroundItem().getName());
            }
        }
    }

    //##################### NESTED CLASSES START HERE ##########################

    public class bank extends Node {
        public boolean activate() {
            //something is wrong here
                return Banker != null && Distance.distanceTo(Players.getLocal().getLocation(), Banker.getLocation(), 3);
        }

        public void execute() {
            status = "Banking";
            Bank1.open();
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    if(Bank1.isOpen()) {
                        log.info("Bank is open");
                        return true;
                    } else {
                        return false;
                    }
                }
            }, 3000);
            Bank1.withdraw(8009, 5);
            Bank1.withdraw(7946, 14);
            Bank1.withdraw(Constants.UNICORN_POUCH, 2);
            Bank1.withdraw(Constants.HEALING_AURA_SCROLLS, 100);
            Bank1.withdraw(Constants.GAMES_NECKLACE, 1);
            Bank1.close();
            Walking.walk(new Tile(2941, 3376, 0));
            sleep(2000);
            Utilities.sleepWhile(new Condition() {
                @Override
                public boolean validate() {
                    return Players.getLocal().isMoving();
                }
            }, 10 * 1000);
        }
    }


    public class teleport extends Node {
        public boolean activate() {
            return Inventory.contains(Constants.GAMES_NECKLACE);
        }

        public void execute() {
            status = "Teleporting";
            for (Item necklace : Inventory.getItems()) {
                if (necklace.getId() == Constants.GAMES_NECKLACE) {
                    necklace.getWidgetChild().interact("Rub");
                    sleep(2000);
                    Utilities.waitFor(new Condition() {
                        @Override
                        public boolean validate() {
                            if(Widgets.get(635, 13) != null && Widgets.get(635, 13).visible()) {
                                System.out.println("Widgets Visible");
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }, 200);
                } else {
                    if(!inventoryContains(Constants.GAMES_NECKLACE)) {
                        log.info("Oops missing the necklace!");
                        Walking.walk(Banker.getLocation());
                        Bank1.open();
                        Bank1.withdraw(Constants.GAMES_NECKLACE, 1);
                    }
                }
            }
            if(Widgets.get(635, 13).visible()) {
                Widgets.get(635, 13).click(true);
            }
            Utilities.sleepWhile(new Condition() {
                @Override
                public boolean validate() {
                    return Players.getLocal().getAnimation() == 9603;
                }
            }, 10 * 1000);
        }
    }

    public class enterWater extends Node {
        public boolean activate() {
            return getCharges() < 8 && getCharges() != 0;
        }

        public void execute() {
            if (Summoning.isFamiliarSummoned()) {
                status = "Dismissing Familiar";
                Summoning.dismissFamiliar();
            }

            if (summoningPercent < 90 && obelisk != null) {
                status = "Renewing";
                Walking.newTilePath(SUMMONING_OBELISK).traverse();
                Utilities.sleepWhile(new Condition() {
                    @Override
                    public boolean validate() {
                        return !obelisk.isOnScreen();
                    }
                }, 10 * 1000);
                Camera.turnTo(obelisk);
                obelisk.interact("Renew points");
                status = "Walking";
                Walking.newTilePath(WHIRLPOOL).traverse();
                Utilities.waitForCondition(new Condition() {
                    @Override
                    public boolean validate() {
                        return whirlpool != null && whirlpool.isOnScreen();
                    }
                });
            } else {
                if (summoningPercent >= 90) {
                    status = "Walking";
                    Walking.newTilePath(WHIRLPOOL).traverse();
                    Utilities.sleepWhile(new Condition() {
                        @Override
                        public boolean validate() {
                            log.info("Whirlpool is not on screen :  sleeping");
                            return  whirlpool != null && Distance.distanceTo(Players.getLocal().getLocation(), whirlpool.getLocation(), 10);    //stuck between walking and entering...
                        }
                    }, 30 * 1000);
                    status = "Entering";
                    if(whirlpool != null) {
                    Camera.turnTo(whirlpool);
                    whirlpool.interact("Dive-in");
                    clickStairs();
                    }


                }

            }

        }
    }

    public class eat extends Node {
        public boolean activate() {
            return Players.getLocal().getHealthPercent() <= 70;
        }

        public void execute() {
            status = "eating";
            for (Item i : Inventory.getItems()) {
                if (i.getId() == Constants.SHARK) { // this is more of a failsafe at this point
                    i.getWidgetChild().interact("Eat");
                } else {
                    if (i.getId() == Constants.MONKFISH)
                        i.getWidgetChild().interact("Eat");
                }            //sleep while player is eating

            }
        }
    }

    public class fightWaterfiends extends Node {
        public boolean activate() {
            if(waterfiend != null && Players.getLocal() != null) {
            return  Distance.distanceTo(Players.getLocal().getLocation(),
                    waterfiend.getLocation()) <= 10;
            } else {
                return false;
            }
        }

        public void execute() {
            status = "Fighting Waterfiends";
            if (Distance.distanceTo(Players.getLocal().getLocation(), NPCs
                    .getNearest(Constants.WATERFIEND).getLocation()) <= 10
                    && !Summoning.isFamiliarSummoned()) {
                Summoning.summonFamiliar(Familiar.UNICORN_STALLION);
            }
            if (Distance.distanceTo(Players.getLocal().getLocation(), NPCs
                    .getNearest(Constants.WATERFIEND).getLocation()) <= 5
                    && Players.getLocal().getHealthPercent() >= 80) {
                Camera.turnTo(waterfiend);
                waterfiend.interact("Attack");
                sleep(1000, 2000);
                while (Players.getLocal().isInCombat()) {
                    loot();
                    if (Players.getLocal().getHealthPercent() < 80) {
                        if (Summoning.isFamiliarSummoned()) {
                            Summoning.cast();
                            if(!Summoning.isFamiliarSummoned()) {
                                status = "eating";
                                for (Item i : Inventory.getItems()) {
                                    if (i.getId() == Constants.SHARK) {
                                        i.getWidgetChild().interact("Eat");
                                    } else {
                                        if (i.getId() == Constants.MONKFISH)
                                            i.getWidgetChild().interact("Eat");
                                    }

                                }

                            }
                        }

                    }
                }

            } else {
                if (Distance.distanceTo(Players.getLocal().getLocation(),
                        NPCs.getNearest(Constants.WATERFIEND).getLocation()) > 5
                        && Players.getLocal().getHealthPercent() >= 80) {
                    Distance.walkTo(NPCs.getNearest(Constants.WATERFIEND)
                            .getLocation());

                }
            }

        }// closes execute body
    }// closes class body

    public class exit extends Node {
        public boolean activate() {
            if(!inventoryContains(Constants.MONKFISH, Constants.SHARK) || Players.getLocal().getHealthPercent() <= 60) {
                return true;
            } else {
                return false;
            }
        }

        public void execute() {
            status = "Returning";
            NPC guard = NPCs.getNearest(Constants.GUARD_ID);
            for (Item i : Inventory.getItems()) {
                if (i.getId() == Constants.FALADOR_TELEPORT) {
                    i.getWidgetChild().interact("Break");
                    if(guard != null && Distance.distanceTo(Players.getLocal().getLocation(), guard.getLocation()) <= 5) {
                        status = "Walking to Bank";
                        Walking.newTilePath(TO_BANK).traverse();
                    }
                }
            }
        }

    } // closes class



    //############################## PAINT STARTS HERE ################################

    public int getCharges() {
        return i;
    }
    public void messageReceived(MessageEvent e) {
        if (e.getMessage().contains("Your games necklace has")) {
            String s = e.getMessage();
            String one = s.substring(24, 27);
            if(one.toLowerCase().contains("one")) {
                i = 1;
            }
            if(one.toLowerCase().contains("thr")) {
                i = 3;
            }
            if(one.toLowerCase().contains("fou")) {
                i = 4;
            }
            if(one.toLowerCase().contains("fiv")) {
                i = 5;
            }
            if(one.toLowerCase().contains("six")) {
                i = 6;
            }
            if(one.toLowerCase().contains("sev")) {
                i = 7;
            }
            if(one.contains("two")) {
                i = 2;
            }
        }


    }






    public void mouseClicked(MouseEvent e) {
        p = e.getPoint();
        if (close.contains(p) && !hide) {
            hide = true;
        } else if (open.contains(p) && hide) {
            hide = false;
        }

    }


    public void mouseEntered(MouseEvent arg0) {

    }


    public void mouseExited(MouseEvent arg0) {

    }


    public void mousePressed(MouseEvent arg0) {

    }


    public void mouseReleased(MouseEvent arg0) {

    }

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5,
                Mouse.getY() + 5);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5,
                Mouse.getY() - 5);

        if (Game.getClientState() == 11) {
            if (!hide) {
                g.drawImage(img1, -12, 337, null);
                g.setFont(Constants.FONT_ONE);
                g.setColor(Constants.COLOR_ONE);
                g.drawString("" + rangeExpGained, 97, 428);
                g.drawString("" + Time.format(System.currentTimeMillis() - startTime), 343, 507);
                g.drawString("" + charmsGained, 97, 454);
                g.drawString("" + fiendsKilled, 340, 456);
                g.drawString("" + status, 339, 479);

            }


            if (waterfiend != null && Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getHealthPercent() <= 100) {
                for (final Polygon p : Players.getLocal().getInteracting()
                        .getBounds()) {
                    final Color color1 = new Color(0, 153, 0); // green
                    g.setColor(color1);
                    g.fill(p);

                }
                if (waterfiend != null && Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getHealthPercent() <= 50) {
                    for (final Polygon x : Players.getLocal().getInteracting()
                            .getBounds()) {
                        final Color color2 = new Color(204, 204, 0); // yellow
                        g.setColor(color2);
                        g.fill(x);
                    }
                }
                if (waterfiend != null && Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getHealthPercent() <= 25) {
                    for (final Polygon w : Players.getLocal().getInteracting()
                            .getBounds()) {
                        final Color color3 = new Color(204, 0, 0, 123); // red
                        g.setColor(color3);
                        g.fill(w);
                    }
                }

            }

            if (hide) {
                g.drawImage(button, 484, 402, null);
            }


        }

    }


    public class username extends JFrame {

        public username() {
            initComponents();
        }

     public void button1ActionPerformed(ActionEvent e) throws NoSuchAlgorithmException, URISyntaxException, IOException {
         String username = formattedTextField1.getText();
         String password = JPasswordField1.getText();

         ScriptManager.queueInfo.put("Username", username);

         MessageDigest m = MessageDigest.getInstance("MD5");

         m.reset();
         m.update(password.getBytes());
         byte[] digest = m.digest();

         BigInteger bigInt = new BigInteger(1, digest);
         String hashedPassword = bigInt.toString(16);

         URL checkUsername = new URL("http://www.aeroscripts.x10.bz/getdata.php?Username=" + username + "&Password=" + hashedPassword);
         URLConnection connect = checkUsername.openConnection();

         connect.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
         connect.setDoInput(true);
         connect.setDoOutput(true);
         connect.setUseCaches(false);

         String line_1;
         String line_2;
         BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
         if((line_1 = in.readLine()) != null)  {
             lines.put(1, line_1);
             if(line_1.equals("true") || line_1.contains("true")){
                 JFrame frame = new JFrame();
                 JOptionPane.showMessageDialog(frame, "You Are Validated with AeroScripts.com!");
                 userIsValidated = true;
             } else {
                 JFrame frame_2 = new JFrame();
                 JOptionPane.showMessageDialog(frame_2, "Username or Password incorrect. \n Make sure you are logged in to AeroScrips.com");
             }

         }
         if((line_2 = in.readLine()) != null) {
            lines.put(2, line_2);
          String sessionID = lines.get(2);
         }

         in.close();
         gui.dispose();

        }


        private void initComponents() {
            button1 = new JButton();
            formattedTextField1 = new JFormattedTextField();
            label1 = new JLabel();
            label2 = new JLabel();
            JPasswordField1 = new JFormattedTextField();

            //======== this ========
            Container contentPane = getContentPane();

            //---- button1 ----
            button1.setText("Submit");
            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        button1ActionPerformed(e);
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            });

            //---- label1 ----
            label1.setText("Your AeroScripts Username");

            //---- label2 ----
            label2.setText("Your AeroScripts Password");

            GroupLayout contentPaneLayout = new GroupLayout(contentPane);
            contentPane.setLayout(contentPaneLayout);
            contentPaneLayout.setHorizontalGroup(
                    contentPaneLayout.createParallelGroup()
                            .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                    .addGap(0, 120, Short.MAX_VALUE)
                                                    .addComponent(button1)
                                                    .addGap(124, 124, 124))
                                            .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(label1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(formattedTextField1))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(JPasswordField1))
                                                    .addContainerGap())))
            );
            contentPaneLayout.setVerticalGroup(
                    contentPaneLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                    .addGap(13, 13, 13)
                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label1)
                                            .addComponent(label2))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(JPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(formattedTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(13, 13, 13)
                                    .addComponent(button1)
                                    .addContainerGap(20, Short.MAX_VALUE))
            );
            pack();
            setLocationRelativeTo(getOwner());

        }


        private JButton button1;
        private JFormattedTextField formattedTextField1;
        private JLabel label1;
        private JLabel label2;
        private JFormattedTextField JPasswordField1;

    }


}


