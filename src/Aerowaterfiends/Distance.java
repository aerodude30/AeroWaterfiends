
package Aerowaterfiends;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.wrappers.Tile;

public class Distance {

public static double distanceTo(Tile playerTile,  Tile NpcTile) {
	double hypotenuse; 
	double distanceOne = Math.pow(Math.abs(NpcTile.getY()) - Math.abs(playerTile.getY()), 2);
	double distanceTwo = Math.pow(Math.abs(playerTile.getX()) - Math.abs(NpcTile.getX()), 2);
	hypotenuse = distanceOne + distanceTwo;

    if(playerTile != null && NpcTile != null) {

	return Math.round(Math.sqrt(hypotenuse));

    } else {
        System.out.println("Player Tile or Object tile has received a null value");
        return Double.parseDouble(null);
    }
}


    /**
     *
     * @param playerTile  current location of the player
     * @param objectTile  current location of the object
     * @param i compare's the distance from the playerTile to the Object Tile to the passed parameter
     * @return true when the distance is less than the parameter i else returns false
     */

public static boolean distanceTo(Tile playerTile, Tile objectTile, int i) {

    double hypotenuse;
    double distanceOne = Math.pow(Math.abs(objectTile.getY()) - Math.abs(playerTile.getY()), 2);
    double distanceTwo = Math.pow(Math.abs(playerTile.getX()) - Math.abs(objectTile.getX()), 2);
    hypotenuse = distanceOne + distanceTwo;
    double result =  Math.round(Math.sqrt(hypotenuse));
    if(playerTile != null && objectTile != null) {
        if(result < i)
        return true;
    } else {
        if(result > i) {
        System.out.println("Player Tile or Object tile has received a null value or the result is greater than the parameter input");
       Double.parseDouble(null);
        return false;
        }
    }
    return false;
}


public static boolean walkTo(Tile npcTile) {
    int npcX = npcTile.getX() - 1;
    int npcY = npcTile.getY() - 1;
    Tile t = new Tile(npcX, npcY, 0);
    return  Walking.walk(t);

	}
}
