package game;

import java.awt.Image;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class PowerUp extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int POINT = 0;
	public static final int FREEZE = 1;
	public static final int SPEED = 2;
	public static final int GHOST = 3;
	public static final int HEALTH = 4;

	private static final int SIZE = 20;

	public int x;
	public int y;

	static final ImageIcon POINT_ICON = new ImageIcon(new ImageIcon(PowerUp.class.getResource("/images/point.png"))
			.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
	static final ImageIcon FREEZE_ICON = new ImageIcon(new ImageIcon(PowerUp.class.getResource("/images/freeze.png"))
			.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
	static final ImageIcon SPEED_ICON = new ImageIcon(new ImageIcon(PowerUp.class.getResource("/images/speed.png"))
			.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
	static final ImageIcon GHOST_ICON = new ImageIcon(new ImageIcon(PowerUp.class.getResource("/images/ghost.png"))
			.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
	static final ImageIcon HEALTH_ICON = new ImageIcon(new ImageIcon(PowerUp.class.getResource("/images/health.png"))
			.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));

	private int type;

	/**
	 * Randomly generates a power-up on the provided JLayeredPane on the 1st layer
	 * (0)
	 * 
	 * @param container
	 *            - A JLayeredPane that the power-up will be added to
	 * @param maxX
	 *            - The maximum x value the power-up can have
	 * @param maxY
	 *            - The maximum y value the power-up can have
	 * @param type
	 *            - An integer that dictates what type of power-up it can be.
	 *            Accepts 0 - 3, the class constants
	 */
	public PowerUp(JLayeredPane container, int maxX, int maxY, int type) {
		this.type = type;

		ImageIcon icon;
		switch (type) {
		case 0:
			icon = POINT_ICON;
			break;
		case 5:
			this.type = 1;
		case 1:
			icon = FREEZE_ICON;
			break;
		case 6:
			this.type = 2;
		case 2:
			icon = SPEED_ICON;
			break;
		case 7:
			this.type = 3;
		case 3:
			icon = GHOST_ICON;
			break;
		case 4:
			icon = HEALTH_ICON;
			break;
		default:
			this.type = 0;
			icon = POINT_ICON;
		}
		setIcon(icon);
		Random rand = new Random();
		this.x = rand.nextInt(maxX);
		this.y = rand.nextInt(maxY);
		boolean overlap = true;
		boolean check = false;
		while (overlap) {
			for (ObjectOnBoard obj : Game.board.getObjects()) {
				if (getBounds().intersects(obj.getBounds())) {
					check = true;
				}
			}
			if (!check) {
				overlap = false;
			}
		}
		setLocation(this.x, this.y);
		setSize(getPreferredSize().width + 5, getPreferredSize().height + 5);
		setBorder(BorderFactory.createEtchedBorder());
		container.add(this, new Integer(0));
	}

	public int getType() {
		return type;
	}
}
