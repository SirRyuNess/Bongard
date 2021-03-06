package ollitos.platform.awt;

import java.awt.*;
import java.awt.event.*;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.event.IBEvent;
import ollitos.gui.menu.IBMenu;
import ollitos.gui.menu.IBMenuItem;
import ollitos.platform.BPlatform;
import ollitos.platform.BScreen;
import ollitos.platform.IBCanvas;
import ollitos.platform.raster.BRasterProviderCache;



public class AWTScreen extends BScreen{


    private class KeyListenerImpl extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int c = e.getKeyCode();
			if( c == KeyEvent.VK_ESCAPE || c == KeyEvent.VK_BACK_SPACE ){
                listeners().handle( new IBEvent(IBEvent.Type.back) );
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar();
			c = Character.toLowerCase(c);
			listeners().handle( new IBEvent(IBEvent.Type.keyPressed,c));
		}
	}
	
	private class MouseListenerImpl extends MouseAdapter {
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int count = e.getClickCount();
			if( false ){
				if( count == 1 ){
					listeners().handle( event( IBEvent.Type.pointerClick, e ) );
				}
				if( count == 2 ){
					listeners().handle( event( IBEvent.Type.zoomIn, e ) );
				}
			}
			else{
				listeners().handle( event(IBEvent.Type.pointerClick, e) );
			}
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerDragged, e ) );
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if( e.getWheelRotation() < 0 ){
				listeners().handle( event( IBEvent.Type.zoomIn, e ) );
			}
			else{
				listeners().handle( event( IBEvent.Type.zoomOut, e ) );
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerDown, e ) );
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerUp, e ) );
		}
	}
	
	private AWTPoint pointInOriginalCoords(MouseEvent e){
		if( e == null ){
			return null;
		}
		
		Point p = e.getPoint();
		AWTPoint point = new AWTPoint(0, 0);
		inverseTransform().transform(p, point);
		return point;
	}
	

	private IBEvent event( IBEvent.Type t, MouseEvent e ){
		return new IBEvent( t, pointInOriginalCoords(e), originalSize() );
	}

	
	@SuppressWarnings("serial")
	private class CanvasImpl extends Canvas{
		{
			addComponentListener( new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					listeners().handle( event( IBEvent.Type.containerResized, null ) );
					AWTScreen c = AWTScreen.this;
					c.adjustTransformToSize();
				}
			});
			
			MouseListenerImpl l = new MouseListenerImpl();
			addMouseListener( l ); 
			addMouseMotionListener(l);
			addMouseWheelListener(l);
			addKeyListener( new KeyListenerImpl() );
		}

        @Override
        public Dimension getPreferredSize() {
            if( drawable() == null ){
                return super.getPreferredSize();
            }
            IBRectangle os = drawable().originalSize();
            return new Dimension( (int)os.w(), (int)os.h() );
        }

        @Override
		public void paint(Graphics g) {
			BPlatform f = BPlatform.instance();
			eraseBackground();
			if( drawable() != null ){
				drawable().draw(canvas(), transform());
			}
			Image i = getOffscreenImage();
			g.drawImage(i,0,0,null);
			AWTAnimator a = (AWTAnimator)f.game().animator();
			String msg = "Millis:" + a.lastStep();
			msg += "  rp:" + BRasterProviderCache.instance().setUpSize() + "/" + BRasterProviderCache.instance().size();
			msg += "  mem:" + Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().maxMemory(); 
			
			g.drawString(msg, 0, getHeight());
		}
		public void update(Graphics g){
			paint(g);
		}
	};

	private CanvasImpl _impl;
	private Image _image;
	
	/**
	 * 
	 * @param obj
	 */
	public AWTScreen() {
		_impl = new CanvasImpl();
	}
	
	public Component canvasImpl(){
		return _impl;
	}
	
	public Graphics2D getGraphics(){
		Image i = getOffscreenImage();
		Graphics2D g2d = (Graphics2D) i.getGraphics();
		
//		IBRectangle os = drawable().originalSize();
//		double pts[] = { os.x(), os.y(), os.x()+os.w(), os.y()+os.h() };
//		((AWTTransform)transform()).transform(pts, 0, pts, 0, 2);
//		
//		g2d.setClip((int)pts[0], (int)pts[1], (int)(pts[2]-pts[0]), (int)(pts[3]-pts[1]));
		
		return g2d;
	}
	
	@Override
	public void refresh() {
		canvasImpl().repaint();
	}
	

	/**
	 * 
	 */
	public void eraseBackground(){
		Image i = getOffscreenImage();
		Graphics graphics = i.getGraphics();
		graphics.setColor( (Color) backgroundColor() );
		graphics.fillRect(0, 0, i.getWidth(null), i.getHeight(null));
		graphics.dispose();
	}
	


	/**
	 * 
	 */
	public Image getOffscreenImage() {
		Component c = canvasImpl();
		Dimension d = c.getSize();
		if (_image == null || _image.getWidth(null) != d.width
				|| _image.getHeight(null) != d.height) {
			_image = c.createImage(d.width, d.height);
		}
		return _image;
	}

	@Override
	public IBRectangle originalSize() {
		Component c = canvasImpl();
		return new BRectangle( 0, 0, c.getWidth(), c.getHeight() );
	}

	public AWTTransform inverseTransform(){
		return (AWTTransform) transform().inverse();
	}


	
	@Override
	public IBCanvas canvas() {
		return new AWTCanvas(getGraphics());
	}


    @Override
    public void setMenu(IBMenu menu) {
        AWTGame g = (AWTGame) BPlatform.instance().game();
        Frame f = g.container();

        MenuBar menuBar = createMenuBar(menu);
        f.setMenuBar(menuBar);
    }

    private MenuBar createMenuBar(IBMenu menu) {
        MenuBar ret= new MenuBar();
        Menu m = new Menu("Menu");
        ret.add(m);

        for( final IBMenuItem i: menu.items() ){
            MenuItem mi = new MenuItem(i.text());
            mi.addActionListener( new ActionListener() {
                Runnable r;
                {
                    r = i.actionListener();
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                    r.run();
                }
            } );
            m.add(mi);
        }
        return ret;
    }

    @Override
    protected void removeMenu() {
        AWTGame g = (AWTGame) BPlatform.instance().game();
        Frame f = g.container();
        f.setMenuBar(null);
    }
}
