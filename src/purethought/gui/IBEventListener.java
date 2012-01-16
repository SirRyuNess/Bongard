package purethought.gui;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;

public interface IBEventListener {
	boolean handle(IBEvent e);
	IBEventSource source();
}
