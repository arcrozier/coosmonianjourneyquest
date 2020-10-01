package game;

import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MouseAnimation implements ActionListener {

	private Player component;
	private Timer timer;

	public MouseAnimation(Container container, Player component, int delay) {
		this.component = component;

		timer = new Timer(delay, this);
		timer.setInitialDelay(0);
	}

	public void actionPerformed(ActionEvent arg0) {
		moveComponent();
	}

	private void moveComponent() {
		try {
			Point mousePos = MouseInfo.getPointerInfo().getLocation();
			component.getParent().getMousePosition();
			mousePos.setLocation(mousePos.x - component.getParent().getLocationOnScreen().x,
					mousePos.y - component.getParent().getLocationOnScreen().y);
			if (mousePos != null) {
				component.pathFinder(mousePos);
			}
		} catch (NullPointerException e) {
		}
		// Game.mouseCoords.setText("Mouse: " +
		// MouseInfo.getPointerInfo().getLocation().x + ", " +
		// MouseInfo.getPointerInfo().getLocation().y);
	}

	public void runGame(boolean running) {
		if (running)
			timer.start();
		if (!running)
			timer.stop();
	}
}
