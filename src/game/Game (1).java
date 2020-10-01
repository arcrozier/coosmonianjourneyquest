package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

public class Game implements MouseListener {

	static boolean RUNNING = false;
	static String UP_KEY = "W";
	static String LEFT_KEY = "A";
	static String DOWN_KEY = "S";
	static String RIGHT_KEY = "D";
	static String SPRINT_KEY = "shift"; // Not how it works but whatever
	static int MOVE_SPEED = 3;
	static int ENEMY_SPEED = 0;
	static int GAME_SPEED = 24;
	static int boardWidth = 700;
	static int boardHeight = 700;
	static JFrame frame;
	static Entity enemy;
	static Entity player;
	static Timer game;
	static Board board;
	static JLayeredPane contentPane;
	static JLabel playerCoords;
	static JLabel enemyCoords;
	static JLabel mouseCoords;
	static JLabel pointCounter;
	static JLabel gameOver;
	static JLabel paused;
	static JLabel heart1;
	static JLabel heart2;
	static JLabel heart3;
	static JLabel heart4;
	static final ImageIcon FULL_HEART = new ImageIcon(new ImageIcon(Game.class.getResource("/images/heart.png"))
			.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
	static final ImageIcon EMPTY_HEART = new ImageIcon(new ImageIcon(Game.class.getResource("/images/heart_empty.png"))
			.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));

	/*
	static final Image GAME_ICON = new ImageIcon(Game.class.getResource("/images/icon.png")).getImage();
	static final Image GAME_ICON_PAUSED = new ImageIcon(Game.class.getResource("/images/paused_icon.png")).getImage();
	*/

	public static ArrayList<PowerUp> powerUps;

	static int genDelay = 5000;
	static long lastGen;
	static long tPaused;
	static long gameStart;

	static int points = 0;

	public Game() {

		contentPane = new JLayeredPane();
		contentPane.setLayout(null);

		/*
		 * playerCoords = new JLabel("Player: " + player.getX() + ", " + player.getY());
		 * playerCoords.setLocation(10, 10);
		 * playerCoords.setSize(playerCoords.getPreferredSize());
		 * playerCoords.setVisible(true); contentPane.add(playerCoords);
		 */

		/*
		 * enemyCoords = new JLabel("Enemy: " + enemy.getX() + ", " + enemy.getY());
		 * enemyCoords.setLocation(10, 30);
		 * enemyCoords.setSize(enemyCoords.getPreferredSize());
		 * contentPane.add(enemyCoords);
		 */

		/*
		 * mouseCoords = new JLabel("Mouse: " +
		 * MouseInfo.getPointerInfo().getLocation().x + ", " +
		 * MouseInfo.getPointerInfo().getLocation().y); mouseCoords.setLocation(10, 50);
		 * mouseCoords.setSize(mouseCoords.getPreferredSize());
		 * contentPane.add(mouseCoords);
		 */

		frame = new JFrame("Coosmonian Journey Quest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(contentPane);
		frame.setSize(boardWidth, boardHeight);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(new ImageIcon(Game.class.getResource("/images/icon.png")).getImage());
		frame.setVisible(true);

		enemy = new Entity(contentPane, Entity.ENEMY);

		player = new Entity(contentPane, Entity.PLAYER);

		Color pointColor = new Color(255, 204, 0);

		pointCounter = new JLabel("" + points);
		pointCounter.setFont(new Font(null, Font.BOLD, 30));
		pointCounter.setForeground(pointColor);
		pointCounter.setLocation(0, 0);
		pointCounter.setBorder(BorderFactory.createLineBorder(pointColor, 4));
		pointCounter.setSize(pointCounter.getPreferredSize());
		contentPane.add(pointCounter, new Integer(2));

		heart1 = new JLabel(FULL_HEART);
		heart1.setSize(heart1.getPreferredSize());
		heart1.setLocation(contentPane.getWidth() - heart1.getWidth() - 10, 10);
		contentPane.add(heart1, new Integer(2));

		heart2 = new JLabel(FULL_HEART);
		heart2.setSize(heart2.getPreferredSize());
		heart2.setLocation(contentPane.getWidth() - heart2.getWidth() - heart1.getWidth() - 11, 10);
		contentPane.add(heart2, new Integer(2));

		heart3 = new JLabel(FULL_HEART);
		heart3.setSize(heart2.getPreferredSize());
		heart3.setLocation(contentPane.getWidth() - heart3.getWidth() - heart2.getWidth() - heart1.getWidth() - 12, 10);
		contentPane.add(heart3, new Integer(2));

		heart4 = new JLabel(FULL_HEART);
		heart4.setSize(heart4.getPreferredSize());
		heart4.setLocation(contentPane.getWidth() - heart4.getWidth() - heart3.getWidth() - heart2.getWidth()
				- heart1.getWidth() - 13, 10);
		heart4.setVisible(false);
		contentPane.add(heart4, new Integer(2));

		gameOver = new JLabel("GAME OVER");
		gameOver.setVisible(false);
		gameOver.setForeground(Color.RED);
		gameOver.setFont(new Font("Buxton Sketch", Font.BOLD, 30));
		gameOver.setLocation(contentPane.getWidth() / 2 - gameOver.getPreferredSize().width / 2,
				contentPane.getHeight() / 2);
		gameOver.setSize(gameOver.getPreferredSize());
		contentPane.add(gameOver, new Integer(2));

		paused = new JLabel("Paused");
		paused.setVisible(false);
		paused.setForeground(Color.GRAY);
		paused.setFont(new Font("Arial Rounded", Font.BOLD, 30));
		paused.setLocation(contentPane.getWidth() / 2 - paused.getPreferredSize().width / 2,
				contentPane.getHeight() / 2);
		paused.setSize(paused.getPreferredSize());
		contentPane.add(paused, new Integer(2));

		board = new Board(contentPane);
		board.randomPopulate(8, contentPane.getWidth() - 32, contentPane.getHeight() - 32);

		game = new Timer(GAME_SPEED, mainLoop);
		frame.addWindowListener(detectFocus);

		powerUps = new ArrayList<PowerUp>();
	}

	public static void main(String[] args) {
		// new Game();
		/*
		 * String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().
		 * getAvailableFontFamilyNames(); for (String font : fonts) {
		 * System.out.println(font); }
		 */
		Game restart = new Game();
		RUNNING = true;
		contentPane.addMouseListener(restart);
		MouseAnimation animation = new MouseAnimation(contentPane, player, GAME_SPEED);
		animation.runGame(Game.RUNNING);
		game.start();
		gameStart = System.currentTimeMillis();
		lastGen = gameStart;
		// TODO add menu
		// TODO modes? Easy, Normal & Hard?
	}

	public static ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}

	public static ArrayList<ObjectOnBoard> getObstacles() {
		return board.getObjects();
	}

	public static void addPoint() {
		points++;
		pointCounter.setText("" + points);
		pointCounter.setSize(pointCounter.getPreferredSize());
	}

	public static void removePowerUp(PowerUp pu) {
		powerUps.remove(pu);
	}

	static ActionListener mainLoop = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			enemy.pathFinder(player.getCenterLocation());
			if (!player.invulnerable()) {
				player.collisionEnemy(enemy.getBounds());
			}
			if (player.getHealth() <= 0) {
				RUNNING = false;
				game.stop();
				gameOver.setVisible(true);

			}
			Random rand = new Random();
			if (System.currentTimeMillis() - genDelay > lastGen) {
				genDelay = rand.nextInt(8000) + 2000;
				powerUps.add(new PowerUp(contentPane, contentPane.getWidth() - 20, contentPane.getHeight() - 20,
						rand.nextInt(10)));
				lastGen = System.currentTimeMillis();
			}
		}
	};

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (!RUNNING) {
			player.reset();
			enemy.reset();
			for (PowerUp pu : powerUps) {
				contentPane.remove(pu);
			}
			powerUps.clear();
			RUNNING = true;
			gameOver.setVisible(false);
			points = 0;
			pointCounter.setText("" + points);
			game.start();
			contentPane.repaint();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	static WindowListener detectFocus = new WindowListener() {

		@Override
		public void windowActivated(WindowEvent arg0) {
			if (!game.equals(null) && !game.isRunning()) {
				pause(false);
			}
		}

		@Override
		public void windowClosed(WindowEvent arg0) {

		}

		@Override
		public void windowClosing(WindowEvent arg0) {

		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			if (game.isRunning()) {
				pause(true);
			}

		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {

		}

		@Override
		public void windowIconified(WindowEvent arg0) {

		}

		@Override
		public void windowOpened(WindowEvent arg0) {

		}

	};

	static void pause(boolean pause) {
		RUNNING = !pause;
		paused.setVisible(pause);
		if (pause) {
			game.stop();
			tPaused = System.currentTimeMillis();
			frame.setIconImage(new ImageIcon(Game.class.getResource("/images/paused_icon.png")).getImage());
		} else if (!pause) {
			game.start();
			lastGen += System.currentTimeMillis() - tPaused;
			frame.setIconImage(new ImageIcon(Game.class.getResource("/images/icon.png")).getImage());

		}
	}
}