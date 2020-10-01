package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import game.Collision;
import game.ColorIcon;

public class Player {
	private JLabel player;
	private String customIconPath = null;
	private int objectWidth = 40;
	private int objectHeight = 40;
	private double x = 500;
	private double y = 500;
	private double MOVE_SPEED = 2.5;
	private int health = 3;
	private boolean invulnerable = false;
	private long lastHit = 0;
	private long tInvulnerability = 3000;
	private boolean ghost = false;
	private long ghostGiven = 0;
	private long tGhost = 10000;
	private boolean speed = false;
	private long speedGiven = 0;
	private long tSpeed = 5000;

	public Player(JLayeredPane component, boolean defaultValues) {
		if (defaultValues) {
			player = new JLabel(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
			player.setSize(player.getPreferredSize());
			player.setLocation((int) x, (int) y);
			component.add(player, new Integer(1));

			// MouseAnimation animation = new MouseAnimation(Game.contentPane,
			// player, Game.GAME_SPEED);
			// animation.runGame(Game.RUNNING);
			/*
			 * KeyboardAnimation animation = new KeyboardAnimation(player,
			 * GAME_SPEED); animation.addAction(UP_KEY, 0, -MOVE_SPEED);
			 * animation.addAction(LEFT_KEY, -MOVE_SPEED, 0);
			 * animation.addAction(DOWN_KEY, 0, MOVE_SPEED);
			 * animation.addAction(RIGHT_KEY, MOVE_SPEED, 0);
			 */
		} else if (!defaultValues) {
			JLabel player = new JLabel();
			component.add(player, new Integer(1));
		}
	}

	public boolean invulnerable() {
		updatePowerUps();
		if (!invulnerable && !ghost) {
			return false;
		}
		return true;

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
			player.setIcon(new ColorIcon(Color.GRAY, 40, 40));
		}
	}

	public void updatePowerUps() {
		if (invulnerable && System.currentTimeMillis() - tInvulnerability > lastHit) {
			player.setBorder(null);
			MOVE_SPEED -= 1.5;
			invulnerable = false;
		}
		if (ghost && System.currentTimeMillis() - tGhost > ghostGiven) {
			ghost = false;
		}
		if (speed && System.currentTimeMillis() - tSpeed > speedGiven) {
			MOVE_SPEED -= 3;
			speed = false;
		}
	}

	public void setColorIcon(Color color, int width, int height) {
		player.setIcon(new ColorIcon(color, width, height));
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
		this.player.setIcon(img);
		return true;
	}

	public void setGhost() {
		ghostGiven = System.currentTimeMillis();
		ghost = true;
	}

	public void giveSpeed() {
		speedGiven = System.currentTimeMillis();
		MOVE_SPEED += 3;
		speed = true;
		//TODO When multiple speed effects are applied at the same time, only one is removed
	}

	public int getHealth() {
		return health;
	}

	public Container getParent() {
		return player.getParent();
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
		return new Point(getX(), getY());
	}

	public Point getCenterLocation() {
		return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public int getX() {
		return player.getX();
	}

	public int getY() {
		return player.getY();
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
				player.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 255), 3));
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
		float ratio = (float) (this.MOVE_SPEED / distance);

		double moveX = deltaX * ratio;
		double moveY = deltaY * ratio;

		move(-moveX, -moveY);
		//TODO vibrates when at mouse
		// move(deltaX > 0 ? -speed : speed, deltaY > 0 ? -speed : speed);

	}

	private void move(double deltaX, double deltaY) {
		if (!Game.RUNNING) {
			return;
		}
		// calculate next location
		/*
		 * double testX = this.x; double testY = this.y; testX += deltaX; testY
		 * += deltaY; System.out.print((int) (testX - this.x) + ", " + (int)
		 * (testY - this.y) + " : "); player.setLocation((int) this.x, (int)
		 * this.y); Game.playerCoords.setText("Player: " + this.x + ", " +
		 * this.y);
		 */

		int componentWidth = player.getSize().width;
		int componentHeight = player.getSize().height;

		Dimension parentSize = player.getParent().getSize();
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
						new Rectangle((int) Math.ceil(nextX), player.getY(), player.getWidth(), player.getHeight()),
						obj.getBounds())) {
					nextX = obj.getCoords().x - player.getWidth() - 1;
					// System.out.println("Collision right");
				} else if (deltaX < 0 && Collision.collisionLeft(
						new Rectangle((int) nextX, player.getY(), player.getWidth(), player.getHeight()),
						obj.getBounds())) {
					nextX = obj.getCoords().x + obj.getWidth() + 1;
					// System.out.println("Collision left");
				}

				if (deltaY > 0 && Collision.collisionBottom(
						new Rectangle((int) nextX, (int) Math.ceil(nextY), player.getWidth(), player.getHeight()),
						obj.getBounds())) {
					nextY = obj.getCoords().y - player.getHeight() - 1;
				} else if (deltaY < 0 && Collision.collisionTop(
						new Rectangle((int) nextX, (int) nextY, player.getWidth(), player.getHeight()),
						obj.getBounds())) {
					// System.out.print((int) nextX + ", " + (int) nextY + ", "
					// +
					// player.getWidth() + ", " + player.getHeight()
					// + ", " + obj.getBounds().toString() + " ");
					nextY = obj.getCoords().y + obj.getHeight() + 1;
					// System.out.println("Collision top");
					// health = 0;
				}
			}
		}

		// Move the component

		player.setLocation((int) nextX, (int) nextY);
		this.x = nextX;
		this.y = nextY;

		checkPowerUp();
		// Game.playerCoords.setText("Player: " + this.x + ", " + this.y);
		// Game.playerCoords.setSize(Game.playerCoords.getPreferredSize());

	}

	private void checkPowerUp() {
		for (int i = 0; i < Game.getPowerUps().size(); i++) {
			PowerUp pu = Game.getPowerUps().get(i);
			if (this.getBounds().intersects(pu.getBounds())) {
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
		speed = false;
		speedGiven = 0;
		tSpeed = 5000;
		player.setIcon(new ColorIcon(Color.BLUE, objectWidth, objectHeight));
		player.setSize(player.getPreferredSize());
		player.setLocation((int) x, (int) y);
		this.updateHealth();
	}

}
