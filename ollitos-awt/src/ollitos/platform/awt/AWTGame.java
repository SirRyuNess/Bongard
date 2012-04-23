package ollitos.platform.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import ollitos.gui.basic.BGame;
import ollitos.gui.basic.BState;
import ollitos.platform.BPlatform;
import bongard.gui.game.BStartField;


public class AWTGame extends BGame{

	private Container _c;
	
	private AWTCanvas _canvas;

	@Override
	public AWTCanvas canvas(){
		if (_canvas == null) {
			_canvas = new AWTCanvas();
		}

		return _canvas;
	}
	
	
	private Frame createFrame(){
		final Frame f = new Frame( "Bongard" );
		f.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				try{
					save( state() );
				}
				catch( Throwable se ){
					se.printStackTrace();
				}
				try{
					System.exit(0);
				}
				catch( SecurityException se){
					se.printStackTrace();
				}
			}
		});
		Component canvas = canvas().canvasImpl();
		f.add(canvas);
		
		f.setSize( 320, 480 );
		return f;
	}
	
	private static void save(BState s){
		if( s == null ){
			return;
		}
		Preferences ur = preferences();
		ur.putByteArray("state", s.bytes() );
	}

	private static BState load(){
		Preferences ur = preferences();
		byte[] ba = ur.getByteArray("state", null);
		return BState.fromBytes(ba);
	}


	private static Preferences preferences() {
		Preferences ur = Preferences.userRoot();
		ur = ur.node(AWTGame.class.getName().toLowerCase());
		return ur;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				f().game().setDefaultDrawable( new BStartField() );
				BState state = null;
				try{
					state = load();
				}
				catch( Throwable e ){
					e.printStackTrace();
				}
				f().game().restore(state);
			}
		} );
	}

	/**
	 * 
	 */
	@Override
	public void restore(BState state) {
		super.restore(state);
		Container c = container();
		c.setVisible(true);
		canvas().canvasImpl().requestFocusInWindow();
	}

	private Container container() {
		if( _c == null ){
			_c = createFrame();
		}
		return _c;
	}
	

	private static BPlatform f() {
		return BPlatform.instance();
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
