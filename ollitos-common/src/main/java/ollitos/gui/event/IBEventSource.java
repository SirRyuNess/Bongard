package ollitos.gui.event;

import ollitos.geom.IBTransformHolder;


public interface IBEventSource{
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
	boolean preHandleEvent(IBEvent e);
	boolean postHandleEvent(IBEvent e);
}
