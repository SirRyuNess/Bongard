package bongard.platform.andr;

import bongard.gui.game.BStartField;
import bongard.gui.game.BState;
import bongard.gui.game.IBGame;

public class AndrGame implements IBGame{

	private AndrCanvas _canvas;
	private AndrAnimator _animator;

	@Override
	public void run() {
		canvas().setDrawable( new BStartField() );
		//BGameModel.goToLevel(false,3,true);
	}

	@Override
	public AndrCanvas canvas() {
		if( _canvas == null ){
			_canvas = new AndrCanvas();
		}
		return _canvas;
	}

	@Override
	public AndrAnimator animator() {
		if (_animator == null) {
			_animator = new AndrAnimator();
		}
		return _animator;
	}

	@Override
	public void restore(BState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BState state() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
