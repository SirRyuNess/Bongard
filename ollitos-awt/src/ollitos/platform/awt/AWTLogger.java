package ollitos.platform.awt;

import ollitos.gui.container.BFlippableContainer;
import ollitos.platform.IBLogger;

public class AWTLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		log( null, msg );
	}

	@Override
	public void log(Object sender, Object msg) {
		if( BFlippableContainer.LOG_EVENTS ){
			return;
		}
		String s = sender != null ? sender.getClass().getName() : "-"; 
		s = s.substring( s.lastIndexOf(".")+1, s.length() );
		System.out.println(s + ": " + msg);
	}

}