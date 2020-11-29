package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Used as a base for creating simple objects to move and display in JFrame
 * <p>
 * Used in the Movable view. Contains methods for moving and creating the object
 * without having to directly program to java swing. Works alongside the Board
 * object.
 * 
 * @author Aragorn
 * @version 1.0
 */
public class ObjectOnBoard {

	public int OBJECT_TYPE;
	private int x;
	private int y;
	private JLabel object = new JLabel();
	private Container board;
	private ImageIcon defaultIcon = new ImageIcon(
			new ImageIcon(ObjectOnBoard.class.getResource("/images/tree_icon.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	private String customIconPath = null;
	private boolean usingCustomIcon = false;
	private int objectWidth = 32;
	private int objectHeight = 32;

	/**
	 * Initializes the object with default settings
	 * 
	 * @param frame
	 *            the frame that the object should be placed on
	 */
	public ObjectOnBoard(JFrame frame) {
		this.OBJECT_TYPE = this.OBJECT;
		this.object.setIcon(defaultIcon);
		this.board = frame;
		this.object.setVisible(true);
		this.board.add(this.object);
		System.out.println("running");
	}
	
	public ObjectOnBoard() {
		this.OBJECT_TYPE = this.OBJECT;
		this.object.setIcon(defaultIcon);
		this.object.setVisible(true);
		this.board.add(this.object);
		System.out.println("running");
	}

	/**
	 * Initializes the object with starting coordinates
	 * 
	 * @param container
	 *            - The frame that the object should be placed on
	 * @param x
	 *            - The x coordinate that the object will be put on
	 * @param y
	 *            - The y coordinate that the object will be put on
	 */
	public ObjectOnBoard(Container container, int x, int y) {
		this.x = x;
		this.y = y;
		this.OBJECT_TYPE = this.OBJECT;
		this.board = container;
		this.object.setIcon(defaultIcon);
		this.object.setSize(objectWidth, objectHeight);
		this.object.setLocation(x, y);
		this.object.setVisible(true);
		this.board.add(this.object);
		this.board.repaint();
	}

	/**
	 * Moves the object on the board
	 * 
	 * @param x
	 *            - The amount left or right the object should be moved
	 * @param y
	 *            - The amount up or down the object should be moved
	 */
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
		this.object.setLocation(this.x, this.y);
	}

	/**
	 * Attempts to set the image of the object.
	 * 
	 * @param	- imagePath
	 *            The path to the image that should be displayed
	 * @return	- True if the path is valid, if not returns false
	 */
	public boolean setImage(String imagePath) {
		this.customIconPath = imagePath;
		ImageIcon img = new ImageIcon(
				new ImageIcon(customIconPath).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.object.setIcon(img);
		return true;
	}

	/**
	 * Returns the coordinates of the object as a point
	 */
	public Point getLocation() {
		return new Point(this.x, this.y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	/**
	 * Get the width of the object
	 * 
	 * @return	- Returns the width of the object in pixels
	 */
	public int getWidth() {
		return this.objectWidth;
	}

	/**
	 * Get the height of the object
	 * 
	 * @return	- Returns the height of the object in pixels
	 */
	public int getHeight() {
		return this.objectHeight;
	}

	/**
	 * Returns the coordinates of the object in the form (x, y)
	 * 
	 * @return	- The coordinates of the object (x, y)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	/**
	 * Sets the type that the object is. Depending on the type (OBJECT_TYPE) the
	 * object can behave differently when interactions occur 
	 * 
	 * @param objectType
	 *            - An integer value specifying the type of object it is.
	 */
	public void setObjectType(int objectType) {
		if (objectType > 1 || objectType < 0) {
			return;
		}
		this.OBJECT_TYPE = objectType;
	}

	/**
	 * Sets the height and width of the object
	 * @param x	The width of the object
	 * @param y	The height of the object
	 */
	public void setSize(int x, int y) {
		if (x < 1 || y < 1) return;
		objectWidth = x;
		objectHeight = y;
		this.object.setSize(objectWidth, objectHeight);
		if (!usingCustomIcon) {
			defaultIcon.setImage(new ImageIcon("src/images/object.png").getImage().getScaledInstance(objectWidth, objectHeight, Image.SCALE_SMOOTH));
			this.object.setIcon(defaultIcon);
		} else if (usingCustomIcon) {
			ImageIcon img = new ImageIcon(
					new ImageIcon(customIconPath).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
			this.object.setIcon(img);
		}
		
	}
	
	public void setLocation(int x, int y) {
		object.setLocation(x, y);
	}

	/**
	 * Returns the bounds of the object. Can be used for testing collisions or
	 * other size-dependent tasks
	 * 
	 * @return Rectangle - The rectangle has the location of the point as its
	 *         (x1, y1) and the farthest extent from that point as (x2, y2)
	 */
	public Rectangle getBounds() {
		Rectangle bounds = new Rectangle(x, y, objectWidth, objectHeight);
		return bounds;
	}

	/**
	 * Tests whether one object has collided with another object
	 * 
	 * @param object1
	 *            - The other object that there is a potential collision with
	 * @return - True if the two objects have collided or intersect; False if
	 *         the two objects do not intersect
	 */
	public boolean testCollision(ObjectOnBoard object1) {
		return (this.getBounds().intersects(object1.getBounds()));
	}

	/**
	 * Primarily intended for debugging. Will create a border around the object
	 * that will not interfere with the function or any custom image that has
	 * been set.
	 * 
	 * @param display
	 *            - Whether or not to display the border (true = display, false
	 *            = hide)
	 */
	public void displayBounds(boolean display) {
		if (display) {
			this.object.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 1));
		}
		if (!display) {
			this.object.setBorder(null);
		}
	}
	
	public JComponent getComponent() {
		return object;
	}

	/**
	 * Destroys the object. Uncertain if it works or not (it SHOULD work).
	 * <p>
	 * Development may continue at some point if the need arises, 
	 * however, at this point, it is unnecessary
	 * 
	 * @deprecated	- No longer supported. Use at your own risk
	 */
	public void destroy() {
		this.object.remove(object);
	}

	/**
	 * Default object type.
	 * 
	 */
	public final int OBJECT = 0;
	/**
	 * An example of another possible object type. Currently unused
	 * 
	 * @deprecated
	 */
	public final int OBSTACLE = 1; // Not used, just as an example of what might
									// be included in this class
}
