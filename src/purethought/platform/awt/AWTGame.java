package purethought.platform.awt;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import purethought.gui.container.BFlippableContainer;
import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.game.BBongardTestField;
import purethought.gui.game.BGameField;
import purethought.gui.game.BGameModel;
import purethought.gui.game.IBGame;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class AWTGame implements IBGame, Runnable{

	private Container _c;
	
	private AWTCanvas _canvas;

	@Override
	public AWTCanvas canvas(){
		if (_canvas == null) {
			_canvas = new AWTCanvas();
		}

		return _canvas;
	}
	
	
	private JFrame createFrame(){
		JFrame f = new JFrame( "Bongard" );
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Component canvas = canvas().canvasImpl();
		f.add(canvas);
		
		f.setSize( 320, 480 );
		return f;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater( f().game() );
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		Container c = container();
		BProblemLocator[] problems = f().cardExtractor().randomProblems(6);
		canvas().setDrawable( new BFlippableContainer( new BGameModel(problems ) ) );
		c.setVisible(true);
	}

	private Container container() {
		if( _c == null ){
			_c = createFrame();
		}
		return _c;
	}
	

	private static BFactory f() {
		return BFactory.instance();
	}

	private AWTAnimator _animator;

	@Override
	public AWTAnimator animator() {
		if (_animator == null) {
			_animator = new AWTAnimator();
		}

		return _animator;
	}
}
