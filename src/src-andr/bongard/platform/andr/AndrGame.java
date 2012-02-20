package bongard.platform.andr;

import android.content.Context;
import bongard.animation.BAnimator;
import bongard.gui.basic.IBCanvas;
import bongard.gui.game.BStartField;
import bongard.gui.game.IBGame;

public class AndrGame implements IBGame{

	private AndrCanvas _canvas;
	private Context _context;

	
	public AndrGame( Context c ){
		_context =c;
	}
	
	public Context context(){
		return _context;
	}
	
	@Override
	public void run() {
		canvas().setDrawable( new BStartField() );
		
	}

	@Override
	public AndrCanvas canvas() {
		if( _canvas == null ){
			_canvas = new AndrCanvas( context() );
		}
		return _canvas;
	}

	@Override
	public BAnimator animator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
