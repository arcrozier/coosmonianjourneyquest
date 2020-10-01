package game;

import java.awt.Rectangle;

public class Collision {


	public static boolean withinX(Rectangle co1, Rectangle co2) {
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

	public static boolean withinY(Rectangle co1, Rectangle co2) {
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

	public static boolean collisionLeft(Rectangle co1, Rectangle co2) {
		if (withinY(co1, co2)) {
			if (co1.x <= co2.x + co2.width && co1.x >= co2.x) {
				return true;
			}
		}
		return false;
	}

	public static boolean collisionRight(Rectangle co1, Rectangle co2) {
		if (withinY(co1, co2)) {
			if (co1.x + co1.width <= co2.x + co2.width && co1.x + co1.width > co2.x)
				return true;
		}
		return false;
	}

	public static boolean collisionTop(Rectangle co1, Rectangle co2) {
		//System.out.println(co1.y <= co2.y + co2.height && co1.y > co2.y ? "within y" : "not within y");
		if (withinX(co1, co2)) {
			//System.out.println("within x");
			if (co1.y <= co2.y + co2.height && co1.y > co2.y) {
				return true;
			}
		}
		return false;
	}

	public static boolean collisionBottom(Rectangle co1, Rectangle co2) {
		if (withinX(co1, co2)) {
			if (co1.y + co1.height > co2.y && co1.y + co1.height < co2.y + co2.height) {
				return true;
			}
		}
		return false;
	}
}
