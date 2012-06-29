package bongard.gui.game;

import ollitos.animation.BRunnableAnimation;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidableModel;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BGameModel implements IBSlidableModel{
	
	public static final int MAX_WIDTH = 12;
	private static final int INITIAL_WIDTH = 2;
	private static final int WIDTH_INCREMENT = 1;
	transient private BResourceLocator _background;
	private BGameField[] _drawables;
	private boolean _demo;
	private BResourceLocator[] _problems;
	private int _seed;

	public BGameModel( boolean demo, BResourceLocator[] problems ){
		this( demo, problems, 0 );
	}
		
	public BGameModel( boolean demo, BResourceLocator[] problems, long seed ){		
		setProblems(problems);
		_demo = demo;
		_seed = 0;
	}

	private void setProblems(BResourceLocator[] problems) {
		_problems = problems;
		_drawables = null;
	}

	private BGameField[] drawables(){
		if( _drawables == null ){
			_drawables = new BGameField[_problems.length];
			for (int i = 0; i < _problems.length; i++) {
				BResourceLocator l = _problems[i];
				BProblem p = new BProblem(l, _seed);
				_drawables[i] = new BGameField(p,this);
			}
		}
		return _drawables;
	}
	
	@Override
	public BGameField page(int x) {
		return drawables()[x];
	}

	@Override
	public int width() {
		return drawables().length;
	}

	@Override
	public BResourceLocator background(){
		if( _background == null ){
			_background = new BResourceLocator( "/images/backgrounds/arrecibo.png" );
		}
		return _background;
	}

	public void answered(BGameField bGameField) {
		if( _demo ){
			return;
		}
		if( allAnswered() ){
			int width = width()+WIDTH_INCREMENT;
			if( !allCorrectAnswered() ){
				width = INITIAL_WIDTH;
			}
			final int goTo = width;
			BPlatform.instance().game().animator().addAnimation( new BRunnableAnimation(1000, new Runnable(){
				@Override
				public void run() {
					IBDrawable d = goToLevel(_demo,goTo,true);
					BPlatform.instance().game().screen().setDrawable(d);
				}
			}));
		}
	}
	
	private static IBDrawable goToLevel(boolean demo, int width, boolean limitDificulty) {
		
		if( width > MAX_WIDTH ){
			width = MAX_WIDTH;
		}
		
		BResourceLocator[] problems = null;
		if( limitDificulty ){
			if( width <= 3 ){
				problems = BCardExtractor.randomProblems(width,10);
			}
			else if( width <= 5 ){
				problems = BCardExtractor.randomProblems(width,30);
			}
		}
		
		if( problems == null ){
			problems = BCardExtractor.randomProblems(width);
		}
		return goToProblems(demo,problems);
	}

	private static IBDrawable goToProblems(boolean demo, BResourceLocator[] problems) {
		BGameModel m = new BGameModel(demo,problems );
		BSlidableContainer d = new BSlidableContainer( BGameField.computeOriginalSize(), m );
		return d;
	}

	private boolean allAnswered(){
		for( int i = 0 ; i < width() ; i++ ){
			BGameField d = page(i);
			if( !d.badAnswer() && !d.correctAnswer() ){
				return false;
			}
		}
		return true;
	}

	private boolean allCorrectAnswered(){
		for( int i = 0 ; i < width() ; i++ ){
			BGameField d = page(i);
			if( !d.correctAnswer() ){
				return false;
			}
		}
		return true;
	}

	public static IBDrawable goToInitialLevel() {
		return goToLevel(false,INITIAL_WIDTH,true);
	}
	
	@Override
	public void dispose(int x) {
		page(x).dispose();
	}
}
