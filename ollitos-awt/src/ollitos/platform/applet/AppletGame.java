package ollitos.platform.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ollitos.platform.BPlatform;



@SuppressWarnings("serial")
public class AppletGame extends Applet {

	@Override
	public void init() {
		setLayout( new BorderLayout() );
		Button button = new Button("Start");
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println( "Starting game frame..." );
				BPlatform.instance().game().restore(null);
			}
		});
		add( button, BorderLayout.CENTER );
	}
	
	@Override
	public void start() {
	}
}
