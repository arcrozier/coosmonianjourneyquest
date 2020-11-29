package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import game.Collision;
import game.ColorIcon;

public class Entity {
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	private int type;
	private JLabel entity;
	private String customIconPath = null;
	private int objectWidth;
	private int objectHeight;
	private double x;
	private double y;
	private double MOVE_SPEED;
	private int health;
	private boolean invulnerable;
	private long lastHit;
	private long tInvulnerability;
	private boolean ghost;
	private long ghostGiven;
	private long tGhost;
	private long tSpeed;
	private boolean frozen;
	private long frostGiven;
	private long tFrozen;
	
	private ArrayList<Long> speedEffects = new ArrayList<Long>();

	public Entity(JLayeredPane component, int type) {
		this.type = type;
		if (type == PLAYER) {
			objectWidth = 40;
			objectHeight = 40;
			x = 500;
			y = 500;
			MOVE_SPEED = 2.5;
			health = 3;
			invulnerable = false;
			lastHit = 0;
			tInvulnerability = 3000;
			ghost = false;
			ghostGiven = 0;
			tGhost = 10000;
			tSpeed = 5000;
			frozen = false;
			frostGiven = 0;
			tFrozen = 5000;
			entity = new JLabel(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
			entity.setSize(entity.getPreferredSize());
			entity.setLocation((int) x, (int) y);
			component.add(entity, Integer.valueOf(1));
		} else if (type == ENEMY) {
			objectWidth = 40;
			objectHeight = 40;
			x = 100;
			y = 100;
			MOVE_SPEED = 2.3;
			health = 3;
			invulnerable = false;
			lastHit = 0;
			tInvulnerability = 3000;
			ghost = false;
			ghostGiven = 0;
			tGhost = 10000;
			tSpeed = 5000;
			frozen = false;
			frostGiven = 0;
			tFrozen = 10000;
			entity = new JLabel(new ColorIcon(Color.RED, objectWidth, objectHeight));
			entity.setSize(entity.getPreferredSize());
			entity.setLocation((int) x, (int) y);
			component.add(entity, Integer.valueOf(1));
		}

		// MouseAnimation animation = new MouseAnimation(Game.contentPane,
		// player, Game.GAME_SPEED);
		// animation.runGame(Game.RUNNING);
		/*
		 * KeyboardAnimation animation = new KeyboardAnimation(player, GAME_SPEED);
		 * animation.addAction(UP_KEY, 0, -MOVE_SPEED); animation.addAction(LEFT_KEY,
		 * -MOVE_SPEED, 0); animation.addAction(DOWN_KEY, 0, MOVE_SPEED);
		 * animation.addAction(RIGHT_KEY, MOVE_SPEED, 0);
		 */

	}

	public boolean invulnerable() {
		updatePowerUps();
		return invulnerable || ghost;

	}

	public void updateHealth() {
		if (health == 4) {
			Game.heart4.setVisible(true);
			Game.heart3.setIcon(Game.FULL_HEART);
			Game.heart2.setIcon(Game.FULL_HEART);
			Game.heart1.setIcon(Game.FULL_HEART);
		}
		if (health == 3) {
			Game.heart4.setVisible(false);
			Game.heart3.setIcon(Game.FULL_HEART);
			Game.heart2.setIcon(Game.FULL_HEART);
			Game.heart1.setIcon(Game.FULL_HEART);
		} else if (health == 2) {
			Game.heart4.setVisible(false);
			Game.heart3.setIcon(Game.EMPTY_HEART);
			Game.heart2.setIcon(Game.FULL_HEART);
			Game.heart1.setIcon(Game.FULL_HEART);
		} else if (health == 1) {
			Game.heart4.setVisible(false);
			Game.heart3.setIcon(Game.EMPTY_HEART);
			Game.heart2.setIcon(Game.EMPTY_HEART);
			Game.heart1.setIcon(Game.FULL_HEART);
		} else if (health == 0) {
			Game.heart4.setVisible(false);
			Game.heart3.setIcon(Game.EMPTY_HEART);
			Game.heart2.setIcon(Game.EMPTY_HEART);
			Game.heart1.setIcon(Game.EMPTY_HEART);
			entity.setIcon(new ColorIcon(Color.GRAY, 40, 40));
		}
	}

	public void freeze() {
		frozen = true;
		frostGiven = System.currentTimeMillis();
		entity.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
	}

	public void updatePowerUps() {
		if (invulnerable && System.currentTimeMillis() - tInvulnerability > lastHit) {
			entity.setBorder(null);
			MOVE_SPEED -= 1.5;
			invulnerable = false;
		}
		if (ghost && System.currentTimeMillis() - tGhost > ghostGiven) {
			ghost = false;
			entity.setIcon(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
			entity.setBorder(null);
		}
		if (ghost && System.currentTimeMillis() - tGhost + 2000 > ghostGiven) {
			entity.setIcon(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
			entity.setBorder(BorderFactory.createLineBorder(new Color(94, 108, 157), 5));
		}
		if (!speedEffects.isEmpty() && System.currentTimeMillis() - tSpeed > speedEffects.get(0)) {
			MOVE_SPEED -= 3;
			speedEffects.remove(0);
		}
	}

	public void setColorIcon(Color color, int width, int height) {
		entity.setIcon(new ColorIcon(color, width, height));
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
		this.entity.setIcon(img);
		return true;
	}

	public void setGhost() {
		ghostGiven = System.currentTimeMillis();
		ghost = true;
		entity.setIcon(new ColorIcon(new Color(94, 108, 157), objectWidth, objectHeight));
	}

	public void giveSpeed() {
		speedEffects.add(System.currentTimeMillis());
		MOVE_SPEED += 3;
	}

	public int getHealth() {
		return health;
	}

	public Container getParent() {
		return entity.getParent();
	}

	/**
	 * Returns the coordinates of the object as a point
	 */
	public Point getCoords() {
		return new Point((int) this.x, (int) this.y);
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
		return new Point(getX(), getY());
	}

	public Point getCenterLocation() {
		return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public int getX() {
		return entity.getX();
	}

	public int getY() {
		return entity.getY();
	}

	/**
	 * Returns the coordinates of the object in the form (x, y)
	 * 
	 * @return - The coordinates of the object (x, y)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	// TODO some kind of damage indicator to the player (screen flashing red,
	// player turning red)
	public boolean collisionEnemy(Rectangle targetRect) {
		Rectangle thisRect = new Rectangle(getX(), getY(), this.getWidth(), this.getHeight());
		if (thisRect.intersects(targetRect)) {
			health--;
			updateHealth();
			if (health != 0) {
				invulnerable = true;
				MOVE_SPEED += 1.5;
				entity.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 255), 3));
			}
			lastHit = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public void pathFinder(Point target) {
		double deltaX = this.x + this.getWidth() / 2 - target.x;
		double deltaY = this.y + this.getHeight() / 2 - target.y;
		double distance = Math.hypot(deltaX, deltaY);
		double ratio = this.MOVE_SPEED / distance;

		double moveX;
		double moveY;

		if (ratio >= 1) {
			moveX = deltaX;
			moveY = deltaY;
		} else {
			moveX = deltaX * ratio;
			moveY = deltaY * ratio;
		}

		move(-moveX, -moveY);

	}

	private void move(double deltaX, double deltaY) {
		if (!Game.RUNNING) {
			return;
		}

		if (frozen) {
			checkFreeze();
			return;
		}
		
		if (entity.getParent() == null) {
			return;
		}
		// calculate next location
		/*
		 * double testX = this.x; double testY = this.y; testX += deltaX; testY +=
		 * deltaY; System.out.print((int) (testX - this.x) + ", " + (int) (testY -
		 * this.y) + " : "); player.setLocation((int) this.x, (int) this.y);
		 * Game.playerCoords.setText("Player: " + this.x + ", " + this.y);
		 */

		int componentWidth = entity.getSize().width;
		int componentHeight = entity.getSize().height;

		Dimension parentSize = entity.getParent().getSize();
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

		if (!ghost) {
			for (ObjectOnBoard obj : Game.getObstacles()) {
				// System.out.println(obj.toString());
				if (deltaX > 0 && Collision.collisionRight(
						new Rectangle((int) Math.ceil(nextX), entity.getY(), entity.getWidth(), entity.getHeight()),
						obj.getBounds())) {
					nextX = obj.getLocation().x - entity.getWidth() - 1;
					// System.out.println("Collision right");
				} else if (deltaX < 0 && Collision.collisionLeft(
						new Rectangle((int) nextX, entity.getY(), entity.getWidth(), entity.getHeight()),
						obj.getBounds())) {
					nextX = obj.getLocation().x + obj.getWidth() + 1;
					// System.out.println("Collision left");
				}

				if (deltaY > 0 && Collision.collisionBottom(
						new Rectangle((int) nextX, (int) Math.ceil(nextY), entity.getWidth(), entity.getHeight()),
						obj.getBounds())) {
					nextY = obj.getLocation().y - entity.getHeight() - 1;
				} else if (deltaY < 0 && Collision.collisionTop(
						new Rectangle((int) nextX, (int) nextY, entity.getWidth(), entity.getHeight()),
						obj.getBounds())) {
					// System.out.print((int) nextX + ", " + (int) nextY + ", "
					// +
					// player.getWidth() + ", " + player.getHeight()
					// + ", " + obj.getBounds().toString() + " ");
					nextY = obj.getLocation().y + obj.getHeight() + 1;
					// System.out.println("Collision top");
					// health = 0;
				}
			}
		}

		// Move the component

		entity.setLocation((int) nextX, (int) nextY);
		this.x = nextX;
		this.y = nextY;

		if (type == PLAYER)
			checkPowerUp();
		// Game.playerCoords.setText("Player: " + this.x + ", " + this.y);
		// Game.playerCoords.setSize(Game.playerCoords.getPreferredSize());

	}

	private void checkFreeze() {
		if (System.currentTimeMillis() - tFrozen > frostGiven) {
			frozen = false;
			entity.setBorder(null);
		}
	}

	public boolean collision(Rectangle targetRect) {
		if (this.getBounds().intersects(targetRect))
			return true;
		return false;
	}

	private void checkPowerUp() {
		for (int i = 0; i < Game.getPowerUps().size(); i++) {
			PowerUp pu = Game.getPowerUps().get(i);
			if (collision(pu.getBounds())) {
				switch (pu.getType()) {
				case PowerUp.POINT:
					Game.addPoint();
					break;
				case PowerUp.FREEZE:
					Game.enemy.freeze();
					break;
				case PowerUp.GHOST:
					this.setGhost();
					break;
				case PowerUp.SPEED:
					this.giveSpeed();
					break;
				case PowerUp.HEALTH:
					if (health < 4) {
						this.health++;
						this.updateHealth();
					}
				}
				this.getParent().remove(pu);
				this.getParent().repaint();
				Game.removePowerUp(pu);
				i--;
			}
		}
	}

	public void reset() {
		if (type == PLAYER) {
			objectWidth = 40;
			objectHeight = 40;
			x = 500;
			y = 500;
			MOVE_SPEED = 2.5;
			health = 3;
			invulnerable = false;
			lastHit = 0;
			tInvulnerability = 3000;
			ghost = false;
			ghostGiven = 0;
			tGhost = 10000;
			tSpeed = 5000;
			entity.setIcon(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
			entity.setSize(entity.getPreferredSize());
			entity.setLocation((int) x, (int) y);
			this.updateHealth();
		}
		if (type == ENEMY) {
			objectWidth = 40;
			objectHeight = 40;
			x = 100;
			y = 100;
			MOVE_SPEED = 2;
			health = 3;
			invulnerable = false;
			lastHit = 0;
			tInvulnerability = 3000;
			ghost = false;
			ghostGiven = 0;
			tGhost = 10000;
			tSpeed = 5000;
			frozen = false;
			frostGiven = 0;
			tFrozen = 5000;
		}
	}

}
