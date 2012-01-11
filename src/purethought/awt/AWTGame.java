package purethought.awt;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import purethought.gui.BGameField;
import purethought.gui.IBGame;
import purethought.gui.IBTransform;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class AWTGame implements IBGame, Runnable{

	private JFrame _f;
	
	public Component _canvas;
	
	public BGameField _field;
	
	/**
	 * 
	 * @return
	 */
	public Component canvas(){
		return _canvas;
	}
	
	@SuppressWarnings("serial")
	private JFrame createFrame(){
		JFrame f = new JFrame( "Bongard" );
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_canvas = new AWTCanvas.Canvas();
		f.add(_canvas);
		
		f.setSize( 480, 640 );
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
		_f = createFrame();
		_f.setVisible(true);
		
		BProblemLocator loc = f().randomProblem();
		
		f().field().setProblem(loc);
		IBTransform t = f().identityTransform();
		t.scale(.5, .5);
		f().canvas().setTransform(t);
	}

	private static BFactory f() {
		return BFactory.instance();
	}
	
}
