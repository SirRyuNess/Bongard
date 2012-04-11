package ollitos.platform;

import java.io.InputStream;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBColor;
import ollitos.gui.basic.IBRaster;

import bongard.gui.game.IBGame;
import bongard.problem.BCardExtractor;


public abstract class BFactory {
	
	public static final IBColor COLOR_WHITE = instance().color("ffffff");
	public static final IBColor COLOR_BLACK = instance().color("000000");
	public static final IBColor COLOR_DARKGRAY = instance().color("888888");
	public static final IBColor COLOR_GRAY = instance().color("999999");
	
	private static BFactory _instance;
	
	public static BFactory instance(){
		if (_instance == null) {
			_instance = createInstance();
		}
		return _instance;
	}


	private static BFactory createInstance() {
		Class<?> c = null;
		try {
			c = Class.forName("ollitos.platform.awt.AWTFactory");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		try {
			c = Class.forName("ollitos.platform.andr.AndrFactory");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		if( c == null ){
			return null;
		}
		
		try{
			return (BFactory) c.newInstance();
		}
		catch( Throwable e ){
			//e.printStackTrace();
		}
		return null;
	}

	

	protected BFactory() {
	}
	
	public abstract IBTransform identityTransform();
	public abstract IBPoint point(double x, double y);
	public abstract BCardExtractor cardExtractor();
	public abstract IBGame game();
	public abstract BSprite sprite( IBRaster raster );
	public abstract BLabel label( String text );
	public abstract BBox box( IBRectangle r, IBColor color );
	public abstract IBRaster raster(BResourceLocator test, boolean transparent);
	public abstract InputStream open(BResourceLocator r);
	public abstract IBColor color(String c);
	public abstract IBLogger logger();
	public abstract BHTMLDrawable html(String string);
	
	public BSprite sprite( BResourceLocator l ){
		return sprite( raster( l, true ) );
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.getProperties().save(System.out, "");
	}


}