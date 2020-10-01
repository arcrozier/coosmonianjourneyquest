package game;

import java.awt.Container;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A general board object that allows easy control over many objects that would could be added to any sort of game with a game board.
 * <p>
 * Primarily for being able to move the board around, either as a player explores or for other reasons. At some point it will be able to
 * handle interactions between objects, like checking whether a player has collided with any objects, for example, or having
 * an object move towards another object.
 * <p>
 * It may also, at some point, handle player objects, and allow control keys, etc, but that is a longer term road map that may not
 * ever materialize, and certainly not any time soon.
 * <p>
 * Just a note, the object is only compatible with JFrames and java swing GUIs because they are objectively better.
 * 
 * @author Aragorn
 * @version 1.0
 *
 */
public class Board {
	
	private ArrayList<ObjectOnBoard> objects;
	private Container frame;
	private int objectDensity = 10000; /*1 object per <objectDensity> pixels*/
	private int maxExploredX;
	private int maxExploredY;
	private int minExploredX;
	private int minExploredY;
	private int currentX = 0;
	private int currentY = 0;
	private boolean generateTerrain = true;

	/**
	 * Initializes the board object with an empty list that can be added to
	 * 
	 * @param frame	- JFrame that corresponds to the window.
	 */
	public Board(Container frame) {
		objects = new ArrayList<ObjectOnBoard>();
		new HashMap<String,ObjectOnBoard>();
		this.frame = frame;
		maxExploredX = frame.getWidth();
		maxExploredY = frame.getHeight();
		minExploredX = 0;
		minExploredY = 0;
	}
	
	/**
	 * Initializes the board object with a list passed as a parameter
	 */
	
	public Board(ArrayList<ObjectOnBoard> objects) {
		this.objects = objects;
	}
	
	/**
	 * Moves the board north, or the viewer south, depending on what I decide
	 */
	public void moveNorth(int value) {
		currentY -= value;
		System.out.println(currentY);
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.move(0, value);
		}
	}
	
	/**
	 * Moves the board west
	 */
	public void moveWest(int value) {
		currentX -= value;
		updateExplored();
		System.out.println(currentX);
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.move(value, 0);
		}
	}
	
	/**
	 * Moves the board south
	 */
	public void moveSouth(int value) {
		currentY += value;
		updateExplored();
		System.out.println(currentY);
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.move(0, -value);
		}
	}
	
	/**
	 * Moves the board east
	 */
	public void moveEast(int value) {
		currentX += value;
		updateExplored();
		System.out.println(currentX);
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.move(-value, 0);
		}
	}
	
	private void updateExplored() {
		if (generateTerrain) {
			if (currentX + frame.getWidth() > maxExploredX) {
				int totalHeight = maxExploredY + minExploredY;
				int totalWidth = (currentX + frame.getWidth()) - maxExploredX;
				System.out.println("count = " + totalWidth * totalHeight / objectDensity + " totalHeight = " + totalHeight + " totalWidth = " + totalWidth);
				this.randomPopulate(totalWidth * totalHeight / objectDensity, frame.getWidth() - totalWidth, minExploredY, frame.getWidth(), maxExploredY);
				maxExploredX = currentX + frame.getWidth();
			}
			if (currentY + frame.getHeight() > maxExploredY) {
				maxExploredY = currentY;
			}
			if (currentX < minExploredX) {
				minExploredX = currentX;
			}
			if (currentY < minExploredY) {
				minExploredY = currentY;
			}
		}
	}
	
	/**
	 * Resizes all objects to equal dimensions.
	 * @param x	- The new width of all the objects
	 * @param y	- The new height of all the objects
	 */
	public void resizeAll(int x, int y) {
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.setSize(x, y);
		}
	}
	
	public void resizeAllRelative(int x, int y) {
		for (ObjectOnBoard thisObject : this.objects) {
			thisObject.setSize(thisObject.getWidth() + x, thisObject.getHeight() + y);
		}
	}
	
	/**
	 * Creates a specified number of objects and adds them to the list of objects
	 * 
	 * @param count		- The number of objects to create
	 * @param boundX	- The maximum x value a created object can have
	 * @param boundY	- The maximum y value a created object can have
	 */
	public void randomPopulate(int count, int boundX, int boundY) {
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			this.objects.add(new ObjectOnBoard(frame, rand.nextInt(boundX), rand.nextInt(boundY)));
		}
	}
	
	/**
	 * Creates a specified number of objects and adds them to the list of objects
	 * 
	 * @param count		- The number of objects to create
	 * @param boundX1	- The minimum x value a created object can have
	 * @param boundY1	- The minimum y value a created object can have
	 * @param boundX2	- The maximum x value a created object can have
	 * @param boundY2	- The maximum y value a created object can have
	 */
	public void randomPopulate(int count, int boundX1, int boundY1, int boundX2, int boundY2) {
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			this.objects.add(new ObjectOnBoard(frame, rand.nextInt(boundX2 + boundX1) - boundX1, rand.nextInt(boundY2 + boundY1) - boundY1));
			System.out.println(objects.get(i).toString());
		}
	}
	
	public void add(ObjectOnBoard object1) {
		this.objects.add(object1);
	}
	
	/**
	 * Moves the user to Canada, or crashes if that doesn't work
	 */
	public void moveToCanada() {
		try {
			Desktop.getDesktop().browse(new URI("http://www.cic.gc.ca/english/"));
		} catch (IOException | URISyntaxException e) {}
		System.exit(0);
	}

	public ArrayList<ObjectOnBoard> getObjects() {
		return this.objects;
	}
}
