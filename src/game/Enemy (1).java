package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Enemy {
	private JLabel enemy;
	private String customIconPath = null;
	private int objectWidth = 40;
	private int objectHeight = 40;
	private double x = 100;
	private double y = 100;
	private int MOVE_SPEED = 2;
	private boolean frozen = false;
	private long frostGiven = 0;
	private long tFrozen = 5000;

	public Enemy(JLayeredPane component) {
		enemy = new JLabel(new ColorIcon(Color.RED, objectWidth, objectHeight));
		enemy.setSize(enemy.getPreferredSize());
		enemy.setLocation((int) x, (int) y);
		component.add(enemy, new Integer(1));
	}

	/**
	 * Attempts to set the image of the object.
	 * 
	 * @param -
	 *            imagePath The path to the image that should be displayed
	 * @return - True if the path is valid, if not returns false
	 */
	public boolean setImageIcon(String imagePath) {
		this.customIconPath = imagePath;
		ImageIcon img = new ImageIcon(new ImageIcon(customIconPath).getImage().getScaledInstance(objectWidth,
				objectHeight, Image.SCALE_DEFAULT));
		this.enemy.setIcon(img);
		return true;
	}

	public void freeze() {
		frozen = true;
		frostGiven = System.currentTimeMillis();
	}

	/**
	 * Moves the object on the board
	 * 
	 * @param x
	 *            - The amount left or right the object should be moved
	 * @param y
	 *            - The amount up or down the object should be moved
	 */
	public void move(double deltaX, double deltaY) {
		if (frozen) {
			checkFreeze();
			return;
		}
		int componentWidth = enemy.getSize().width;
		int componentHeight = enemy.getSize().height;

		Dimension parentSize = enemy.getParent().getSize();
		int parentWidth = parentSize.width;
		int parentHeight = parentSize.height;

		// Calculate new move

		// Determine next X position

		double nextX = Math.max(this.x + deltaX, 0);

		if (nextX + componentWidth > parentWidth) {
			nextX = parentWidth - componentWidth;
		}

		// Determine next Y position

		double nextY = Math.max(this.y + deltaY, 0);

		if (nextY + componentHeight > parentHeight) {
			nextY = parentHeight - componentHeight;
		}

		for (ObjectOnBoard obj : Game.getObstacles()) {
			if (deltaX > 0 && Collision.collisionRight(
					new Rectangle((int) Math.ceil(nextX), enemy.getY(), enemy.getWidth(), enemy.getHeight()),
					obj.getBounds())) {
				nextX = obj.getLocation().x - enemy.getWidth() - 1;
			} else if (deltaX < 0 && Collision.collisionLeft(
					new Rectangle((int) nextX, enemy.getY(), enemy.getWidth(), enemy.getHeight()), obj.getBounds())) {
				nextX = obj.getLocation().x + obj.getWidth() + 1;
			}

			if (deltaY > 0 && Collision.collisionBottom(
					new Rectangle((int) nextX, (int) Math.ceil(nextY), enemy.getWidth(), enemy.getHeight()),
					obj.getBounds())) {
				nextY = obj.getLocation().y - enemy.getHeight() - 1;
			} else if (deltaY < 0 && Collision.collisionTop(
					new Rectangle((int) nextX, (int) nextY, enemy.getWidth(), enemy.getHeight()), obj.getBounds())) {
				nextY = obj.getLocation().y + obj.getHeight() + 1;
			}
		}

		// Move the component
		enemy.setLocation((int) nextX, (int) nextY);
		this.x = nextX;
		this.y = nextY;
	}

	/**
	 * Returns the coordinates of the object as a point
	 */
	public Point getCoords() {
		Point coords = new Point((int) this.x, (int) this.y);
		return coords;
	}

	/**
	 * Get the width of the object
	 * 
	 * @return - Returns the width of the object in pixels
	 */
	public int getWidth() {
		return this.objectWidth;
	}

	/**
	 * Get the height of the object
	 * 
	 * @return - Returns the height of the object in pixels
	 */
	public int getHeight() {
		return this.objectHeight;
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public Point getLocation() {
		return new Point((int) this.x, (int) this.y);
	}

	public int getX() {
		return enemy.getX();
	}

	public int getY() {
		return enemy.getY();
	}

	/**
	 * Returns the coordinates of the object in the form (x, y)
	 * 
	 * @return - The coordinates of the object (x, y)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	public void pathFinder(Point target) {
		double deltaX = this.x + this.getWidth() / 2 - target.x;
		double deltaY = this.y + this.getHeight() / 2 - target.y;
		double distance = Math.hypot(deltaX, deltaY);
		float ratio = this.MOVE_SPEED / (float) distance;

		double moveX = deltaX * ratio;
		double moveY = deltaY * ratio;

		move(-moveX, -moveY);

	}

	public boolean collision(Rectangle targetRect) {
		Rectangle thisRect = new Rectangle((int) this.x, (int) this.y, this.getWidth(), this.getHeight());
		if (thisRect.intersects(targetRect))
			return true;
		return false;
	}

	private void checkFreeze() {
		if (System.currentTimeMillis() - tFrozen > frostGiven) {
			frozen = false;
		}
	}

	public void reset() {
		objectWidth = 40;
		objectHeight = 40;
		x = 100;
		y = 100;
		MOVE_SPEED = 2;
		frozen = false;
		frostGiven = 0;
		tFrozen = 5000;
		enemy.setIcon(new ColorIcon(Color.RED, objectWidth, objectHeight));
		enemy.setSize(enemy.getPreferredSize());
		enemy.setLocation((int) x, (int) y);
	}

}