package bongard.gui.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;
import ollitos.platform.BFactory;
import ollitos.platform.BResourceLocator;

import bongard.gui.container.BDrawableContainer;
import bongard.gui.container.IBDrawableContainer;

public class BGameHelp extends BDrawableContainer{

	private BHTMLDrawable _html;
	
	public static String read( InputStream i ) throws IOException{
		InputStreamReader r = new InputStreamReader(i);
		StringBuffer ret = new StringBuffer();
		int c = r.read();
		while( c != -1 ){
			ret.append((char)c);
			c = r.read();
		}
		r.close();
		return ret.toString();
	}
	
	public BHTMLDrawable html(){
		if (_html == null) {
			BResourceLocator l = new BResourceLocator("/images/examples/help.html" );
			
			String s = "Cant read resource:" + l.string();
			try{
				InputStream open = BFactory.instance().open(l);
				s = read( open );
			}
			catch( IOException e ){
			}
			
			_html = BFactory.instance().html(s);
			_html.setOriginalSize( originalSize() );
		}
		return _html;
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		html().draw(c, t);
	}

	private IBRectangle _rectangle = new BRectangle(0, 0, 200, 320 );
	
	@Override
	public IBRectangle originalSize() {
		return _rectangle;
	}
	
	
	@SuppressWarnings("serial")
	private static class MyState extends BState{
		@Override
		public IBDrawableContainer createDrawable() {
			return new BGameHelp();
		}
	}
	
	@Override
	public BState save() {
		return new MyState();
	}
}