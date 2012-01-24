package purethought.gui.game;

import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.container.IBFlippableModel;
import purethought.platform.BImageLocator;

public class BGameModel implements IBFlippableModel{
	
	
	private BImageLocator[] _problems;
	private BGameField[] _drawables;

	public BGameModel( BImageLocator[] problems ){
		setProblems(problems);
	}

	private void setProblems(BImageLocator[] problems) {
		_problems = problems;
		_drawables = new BGameField[problems.length];
		for (int i = 0; i < problems.length; i++) {
			BImageLocator l = problems[i];
			_drawables[i] = new BGameField(l);
		}
	}

	@Override
	public IBFlippableDrawable drawable(int x) {
		return _drawables[x];
	}

	@Override
	public int width() {
		return _drawables.length;
	}

}
