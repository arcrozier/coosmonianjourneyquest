package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class KeyboardAnimation implements ActionListener {
	private final static String PRESSED = "pressed ";
	private final static String RELEASED = "released ";

	private JComponent component;
	private Timer timer;
	private Map<String, Point> pressedKeys = new HashMap<String, Point>();

	public KeyboardAnimation(JComponent component, int delay) {
		this.component = component;

		timer = new Timer(delay, this);
		timer.setInitialDelay(0);
	}

	/*
	 * &param keyStroke - see KeyStroke.getKeyStroke(String) for the format of
	 * of the String. Except the "pressed|released" keywords are not to be
	 * included in the string.
	 */
	public void addAction(String keyStroke, int deltaX, int deltaY) {
		// Separate the key identifier from the modifiers of the KeyStroke

		int offset = keyStroke.lastIndexOf(" ");
		// If offset == -1, key = keyStroke else key =
		// keyStroke.substring(offset + 1);
		String key = offset == -1 ? keyStroke : keyStroke.substring(offset + 1);
		String modifiers = keyStroke.replace(key, "");

		// Get the InputMap and ActionMap of the component

		InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = component.getActionMap();

		// Create Action and add binding for the pressed key

		Action pressedAction = new AnimationAction(key, new Point(deltaX, deltaY));
		String pressedKey = modifiers + PRESSED + key;
		KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
		inputMap.put(pressedKeyStroke, pressedKey);
		actionMap.put(pressedKey, pressedAction);

		// Create Action and add binding for the released key

		Action releasedAction = new AnimationAction(key, null);
		String releasedKey = modifiers + RELEASED + key;
		KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
		inputMap.put(releasedKeyStroke, releasedKey);
		actionMap.put(releasedKey, releasedAction);
	}

	// Invoked whenever a key is pressed or released

	private void handleKeyEvent(String key, Point moveDelta) {
		// Keep track of which keys are pressed

		if (moveDelta == null)
			pressedKeys.remove(key);
		else
			pressedKeys.put(key, moveDelta);

		// Start the Timer when the first key is pressed

		if (pressedKeys.size() == 1) {
			timer.start();
		}

		// Stop the Timer when all keys have been released

		if (pressedKeys.size() == 0) {
			timer.stop();
		}
	}

	// Invoked when the Timer fires

	public void actionPerformed(ActionEvent e) {
		moveComponent();
	}

	// Move the component to its new location

	private void moveComponent() {
		if (!Game.RUNNING)
			return;
		int componentWidth = component.getSize().width;
		int componentHeight = component.getSize().height;

		Dimension parentSize = component.getParent().getSize();
		int parentWidth = parentSize.width;
		int parentHeight = parentSize.height;

		// Calculate new move

		int deltaX = 0;
		int deltaY = 0;

		for (Point delta : pressedKeys.values()) {
			deltaX += delta.x;
			deltaY += delta.y;
		}

		// Determine next X position

		int nextX = Math.max(component.getLocation().x + deltaX, 0);

		if (nextX + componentWidth > parentWidth) {
			nextX = parentWidth - componentWidth;
		}

		// Determine next Y position

		int nextY = Math.max(component.getLocation().y + deltaY, 0);

		if (nextY + componentHeight > parentHeight) {
			nextY = parentHeight - componentHeight;
		}

		for (ObjectOnBoard obj : Game.board.getObjects()) { // TODO move
															// difference
															// instead of to an
															// absolute value?
			if (deltaX > 0 && Collision.collisionRight(new Rectangle(nextX, component.getY(), component.getWidth(), component.getHeight()),
					obj.getBounds())) {
				nextX = obj.getCoords().x - component.getWidth() - 1;
			} else if (deltaX < 0 && Collision.collisionLeft(
					new Rectangle(nextX, component.getY(), component.getWidth(), component.getHeight()), obj.getBounds())) {
				nextX = obj.getCoords().x + obj.getWidth() + 1;
			}

			if (deltaY > 0 && Collision.collisionBottom(new Rectangle(nextX, nextY, component.getWidth(), component.getHeight()),
					obj.getBounds())) {
				nextY = obj.getCoords().y - component.getHeight() - 1;
			} else if (deltaY < 0 && Collision.collisionTop( 
					new Rectangle(nextX, nextY, component.getWidth(), component.getHeight()), obj.getBounds())) {
				nextY = obj.getCoords().y + obj.getHeight() + 1;
			}
		}

		// Move the component

		component.setLocation(nextX, nextY);
		Game.playerCoords.setText("Player: " + Game.player.getX() + ", " + Game.player.getY());
	}

	public boolean withinX(Rectangle co1, Rectangle co2) {
		int ob1x1 = co1.x;
		int ob1x2 = co1.x + co1.width;
		int ob2x1 = co2.x;
		int ob2x2 = co2.x + co2.width;
		if ((ob1x1 >= ob2x1 && ob1x1 <= ob2x2) || (ob1x2 >= ob2x1 && ob1x2 <= ob2x2)
				|| (ob2x1 >= ob1x1 && ob2x1 <= ob1x1) || (ob2x2 >= ob1x1 && ob2x2 <= ob1x2)) {
			return true;
		}
		return false;
	}

	public boolean withinY(Rectangle co1, Rectangle co2) {
		int ob1y1 = co1.y;
		int ob1y2 = co1.y + co1.height;
		int ob2y1 = co2.y;
		int ob2y2 = co2.y + co2.height;
		if ((ob1y1 >= ob2y1 && ob1y1 <= ob2y2) || (ob1y2 >= ob2y1 && ob1y2 <= ob2y2)
				|| (ob2y1 >= ob1y1 && ob2y1 <= ob1y1) || (ob2y2 >= ob1y1 && ob2y2 <= ob1y2)) {
			return true;
		}
		return false;
	}

	public boolean collisionLeft(Rectangle co1, Rectangle co2) { 
		if (withinY(co1, co2)) {
			if (co1.x <= co2.x + co2.width && co1.x >= co2.x) {
				return true;
			}
		}
		return false;
	}

	public boolean collisionRight(Rectangle co1, Rectangle co2) {
		if (withinY(co1, co2)) {
			if (co1.x + co1.width < co2.x + co2.width && co1.x + co1.width > co2.x)
				return true;
		}
		return false;
	}

	public boolean collisionTop(Rectangle co1, Rectangle co2) {
		if (withinX(co1, co2)) {
			if (co1.y <= co2.y + co2.height && co1.y >= co2.y) {
				return true;
			}
		}
		return false;
	}

	public boolean collisionBottom(Rectangle co1, Rectangle co2) {
		if (withinX(co1, co2)) {
			if (co1.y + co1.height >= co2.y && co1.y + co1.height <= co2.y + co2.height) {
				return true;
			}
		}
		return false;
	}

	// Action to keep track of the key and a Point to represent the movement
	// of the component. A null Point is specified when the key is released.

	private class AnimationAction extends AbstractAction implements ActionListener {
		private Point moveDelta;

		public AnimationAction(String key, Point moveDelta) {
			super(key);

			this.moveDelta = moveDelta;
		}

		public void actionPerformed(ActionEvent e) {
			handleKeyEvent((String) getValue(NAME), moveDelta);
		}
	}
}