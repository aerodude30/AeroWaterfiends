
package Aerowaterfiends;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;

import java.awt.*;

public class Utilities {

	public static boolean waitFor(final Condition c, final long timeout) {
		final Timer t = new Timer(timeout);
		while (t.isRunning() && !c.validate()) {
			Task.sleep(20);
		} 
		return c.validate();
	}
	public static boolean waitForCondition(final Condition c) {
		if (!c.validate()) {
			Task.sleep(20);
		} 
		return c.validate();
	}
	
	public static boolean sleepWhile(final Condition c, final long timeout) {
        final Timer t = new Timer(timeout);
		while(c.validate() && t.isRunning() && c != null) {
			Task.sleep(20);
		}
		return !c.validate();
	}
	//i applied it with drawTile3d(Players.getLocal().getLocation, g, -1000) // a positive height will send it into the ground 
	public void drawTile3D(Tile tile, Graphics2D g, int height) {
	    g.setColor(Color.RED);
	    Point[] pointsGround = {tile.getPoint(0,0,0), tile.getPoint(1,0,0), tile.getPoint(1,1,0), tile.getPoint(0,1,0), tile.getPoint(0,0,0)};
	    Point[] pointsHeight = {tile.getPoint(0,0,height), tile.getPoint(1,0,height), tile.getPoint(1,1,height), tile.getPoint(0,1,height), tile.getPoint(0,0,height)};
	    int[] xPointsGround = {pointsGround[0].x,pointsGround[1].x,pointsGround[2].x,pointsGround[3].x,pointsGround[4].x};
	    int[] yPointsGround = {pointsGround[0].y,pointsGround[1].y,pointsGround[2].y,pointsGround[3].y,pointsGround[4].y};
	    int[] xPointsHeight = {pointsHeight[0].x,pointsHeight[1].x,pointsHeight[2].x,pointsHeight[3].x,pointsHeight[4].x};
	    int[] yPointsHeight = {pointsHeight[0].y,pointsHeight[1].y,pointsHeight[2].y,pointsHeight[3].y,pointsHeight[4].y};
	    g.drawPolyline(xPointsGround, yPointsGround, xPointsGround.length);
	    g.drawPolyline(xPointsHeight, yPointsHeight, xPointsHeight.length);
	    for (int i = 0; i < pointsGround.length; i ++) {
		    g.drawLine(xPointsGround[i], yPointsGround[i], xPointsHeight[i], yPointsHeight[i]);
	    }
    }
}