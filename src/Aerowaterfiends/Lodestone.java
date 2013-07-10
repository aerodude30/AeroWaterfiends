package Aerowaterfiends;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Lodestone {


    public final static WidgetChild MAGIC_TAB_ON_ABIL = new Widget(275).getChild(33);
    public final static WidgetChild TELEPORT_TAB_ON_ABIL = new Widget(275).getChild(47);
    public final static WidgetChild HOME_TELEPORT_ON_ABIL = new Widget(275).getChild(18).getChild(155);
    public final static WidgetChild TELEPORTATION_INTERFACE = new Widget(1092).getChild(8);



    public final static WidgetChild LUNAR_ISLE = new Widget(1092).getChild(39);
    public final static WidgetChild BANDIT_CAMP = new Widget(1092).getChild(7);
    public final static WidgetChild AL_KHARID = new Widget(1092).getChild(40);
    public final static WidgetChild ARDOUGNE = new Widget(1092).getChild(41);
    public final static WidgetChild BURTHROPE = new Widget(1092).getChild(42);
    public final static WidgetChild CATHERBY = new Widget(1092).getChild(43);
    public final static WidgetChild DRAYNOR = new Widget(1092).getChild(44);
    public final static WidgetChild EDGEVILLE = new Widget(1092).getChild(45);
    public final static WidgetChild FALADOR = new Widget(1092).getChild(46);
    public final static WidgetChild LUMBRIDGE = new Widget(1092).getChild(47);
    public final static WidgetChild PORT_SARIM = new Widget(1092).getChild(48);
    public final static WidgetChild SEERS_VILLAGE = new Widget(1092).getChild(49);
    public final static WidgetChild TAVERLY = new Widget(1092).getChild(50);
    public final static WidgetChild VARROCK = new Widget(1092).getChild(51);
    public final static WidgetChild YANILLE = new Widget(1092).getChild(52);


    public static final String db = "[DEBUG]";
    public static final String inf = "[INFO]";


    public static void logIt(String type, String message) {
        System.out.println(type + message);
    }


    public static boolean isPlayerTeleporting() {
        return Players.getLocal().getAnimation() == 16385;
    }


    public static boolean teleportTo(WidgetChild location) {
        Tabs.ABILITY_BOOK.open();


        if (!isPlayerTeleporting()) {
            if (!TELEPORT_TAB_ON_ABIL.validate()) {
                logIt(db, "Teleport tab on our ability bar not visible, opening magic tab.");
                Mouse.click(MAGIC_TAB_ON_ABIL.getCentralPoint(), true);
            } else {
                if (!HOME_TELEPORT_ON_ABIL.validate()) {
                    logIt(db, "Home teleport spell(s) not visible, opening teleports tab in ability book.");
                    Mouse.click(TELEPORT_TAB_ON_ABIL.getCentralPoint(), true);
                }
                if (!TELEPORTATION_INTERFACE.validate()) {
                    logIt(db, "Teleportation interface not visible, opening");
                    Mouse.click(HOME_TELEPORT_ON_ABIL.getCentralPoint(), true);
                } else {
                    if (!isPlayerTeleporting()) {
                        logIt(db, "Trying to click on teleport");
                        Mouse.click(location.getCentralPoint(), true);
                        Task.sleep(1500);
                    }
                }
            }
        }


        return isPlayerTeleporting();
    }

}
 