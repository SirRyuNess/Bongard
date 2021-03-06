package bongard.gui.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ollitos.animation.BAnimator;
import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BWaitForAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BCompoundTransformAnimation;
import ollitos.animation.transform.BRotateAnimation;
import ollitos.animation.transform.BScaleAnimation;
import ollitos.animation.transform.BTranslateAnimation;
import ollitos.animation.transform.IBTemporaryTransformAnimable;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BDelayedSprite;
import ollitos.gui.basic.BDrawable;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidablePage;
import ollitos.gui.event.BEventAdapter;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.state.BState;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BGameField extends BDrawableContainer implements IBSlidablePage, Serializable, BState.Stateful{
	
	private static final int FOCUS_DELAY = 100;
	private static final double FOCUS_ZOOM = 1.3;
	private static final double SPRITE_OVER_ZOOM = 1.15;
	private static final int TILE_SIZE = 105;
	private static final boolean SHOW_POINTER = false;
	

	private BProblem _problem;
	private boolean _badAnswer;
	private boolean _correctAnswer;

	private BGameModel _model;
	

	transient private BBox _icon;

	transient private BBox _correctIcon;

	transient private BBox _badIcon;
	transient private BSprite[] _set1Sprites;
	transient private BSprite[] _set2Sprites;
	transient private BSprite _questionSprite;
	transient private BSprite[] _allSprites;
	transient private IBRectangle _size;
	transient private BLabel _pointer;


	transient private IBAnimation _pickUpAnimation;
	transient private IBAnimation _dropAnimation;
	transient private IBAnimation _set1OverAnimation;
	transient private IBAnimation _set2OverAnimation;
	transient private BWaitForAnimation _set1DropAnimation;
	transient private BWaitForAnimation _set2DropAnimation;

	transient private BEventAdapter _adapter;
	


	public void autoSolve(){
		
		boolean isOfSet1 = _problem.isOfSet1(_questionSprite.rasterProvider());
		
		IBPoint orig = spritePosition(-1,1);
		IBPoint dest = spritePosition(isOfSet1?0:1, 2);
		dest = platform().point((dest.x()*2+orig.x())/3, dest.y()+20);
		
		IBAnimation animation = createPickUpAnimation();
		animation = new BConcatenateAnimation( animation,createDragAnimation(dest,900) );
		animation = new BConcatenateAnimation( animation,createOverSetAnimation( isOfSet1?_set1Sprites:_set2Sprites) );
		animation = new BConcatenateAnimation( animation,createDropAnimation(dest) );
		animation = new BConcatenateAnimation( animation,createOutSetAnimation(isOfSet1?_set1Sprites:_set2Sprites, null) );
		
		BAnimator a = platform().game().animator();
		a.finishAnimations();
		a.addAnimation(animation);
	}
	
	private BEventAdapter adapter(){
		if (_adapter == null) {
			_adapter = new MyEventAdapter();
		}
		return _adapter;
	}
	
	
	private class MyEventAdapter extends BEventAdapter{
		
		private boolean _dragQuestion = false;
		private boolean _set1Over;
		private boolean _set2Over;
		
		public MyEventAdapter(){
			super(null);
		}
		
		@Override
		public boolean pointerDown(IBPoint p) {
			boolean inside = _questionSprite.inside(p, null);
			if( inside ){
				_pickUpAnimation = createPickUpAnimation();
				animator().addAnimation( _pickUpAnimation );
				_dragQuestion = true;
				return true;
			}
			return false;
		}

		@Override
		public boolean pointerDrag(IBPoint p){
			if( _dragQuestion ){
				IBAnimation dragAnimation = createDragAnimation(p,50);
				animator().addAnimation( dragAnimation );
				checkSets();
				return true;
			}
			return false;
		}


		private void checkSets() {
			boolean set1Over = near( _questionSprite, _set1Sprites );
			if( !_set1Over &&  set1Over ){
				_set1OverAnimation = createOverSetAnimation(_set1Sprites);
				animator().addAnimation(_set1OverAnimation);
			}
			if( _set1Over && !set1Over ){
				animator().addAnimation( createOutSetAnimation(_set1Sprites,_set1OverAnimation) );
			}
			_set1Over = set1Over;
			
			boolean set2Over = near( _questionSprite, _set2Sprites );
			if( !_set2Over &&  set2Over ){
				_set2OverAnimation = createOverSetAnimation(_set2Sprites);
				animator().addAnimation(_set2OverAnimation);
			}
			if( _set2Over && !set2Over ){
				animator().addAnimation( createOutSetAnimation(_set2Sprites, _set2OverAnimation) );
			}
			_set2Over = set2Over;
		}


		private boolean near(BSprite questionSprite, BSprite[] sprites) {
            double insideFactor = 1.5;


			IBPoint p = questionSprite.temporaryPosition();

            IBTransform t = platform().identityTransform();
            t.scale(insideFactor, insideFactor);
			for( BSprite s: sprites ){
				if( s.inside(p, t) ){
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean pointerUp(IBPoint p) {
			if( _dragQuestion ){
				BPlatform f = BPlatform.instance();
				
				boolean correctAnswer = _set1Over && _problem.isOfSet1(_questionSprite.rasterProvider()) ||
										_set2Over && _problem.isOfSet2(_questionSprite.rasterProvider());
				
				boolean badAnswer =  _set2Over && _problem.isOfSet1(_questionSprite.rasterProvider()) ||
									 _set1Over && _problem.isOfSet2(_questionSprite.rasterProvider());
				
				if( correctAnswer && !_badAnswer ) setCorrectAnswer(true);
				if( badAnswer && !_correctAnswer ) setBadAnswer(true);
				
				IBPoint dest = f.point(105*2, 105*3);
				if( correctAnswer ){
					dest = _questionSprite.temporaryPosition();
				}
				
				_dropAnimation = createDropAnimation(dest);
				
				animator().addAnimation( _dropAnimation );
				
				if( _set1Over ){
					_set1DropAnimation = createOutSetAnimation(_set1Sprites,_set1OverAnimation);
					animator().addAnimation( _set1DropAnimation );
				}
				if( _set2Over ){
					_set2DropAnimation = createOutSetAnimation(_set2Sprites,_set2OverAnimation);
					animator().addAnimation( _set2DropAnimation );
				}
				
				if( correctAnswer ){
					BSprite[] sprites = _set1Over ? _set1Sprites : _set2Sprites;
					IBAnimation setDropAnimation = _set1Over ? _set1DropAnimation : _set2DropAnimation;
					animator().addAnimation( new BWaitForAnimation( new BRotateAnimation(2*Math.PI/400, 400, sprites), setDropAnimation ) );
					animator().addAnimation( new BWaitForAnimation( new BRotateAnimation(2*Math.PI/400, 400, _questionSprite), _dropAnimation ) );
				}

			}
			_set1Over = _set2Over = _dragQuestion = false;
			return false;
		}
	};


	/**
	 * 
	 * @param canvas
	 */
	public BGameField(){
		this(null,null);
	}
	

	protected void setBadAnswer(boolean b) {
		_badAnswer = b;
		model().answered(this);
	}

	protected void setCorrectAnswer(boolean b) {
		_correctAnswer = b;
		model().answered(this);
	}

	public BGameField(BProblem test,BGameModel model){
		super( BGameField.computeOriginalSize());
		setProblem(test);
		setModel(model);
		init();
	}


	private void init() {
		listener().addListener( adapter() );
		IBRectangle r = new BRectangle(0, 0, BSlidableContainer.ICON_SIZE, BSlidableContainer.ICON_SIZE);
		_icon = BPlatform.instance().box(r, BPlatform.COLOR_WHITE);
		_icon.setFilled(false);
		_correctIcon = BPlatform.instance().box(r, BPlatform.COLOR_WHITE);
		_badIcon = BPlatform.instance().box(r, BPlatform.COLOR_BLACK);
	}


	public static  BRectangle computeOriginalSize() {
		return new BRectangle(0, 0, TILE_SIZE*4, TILE_SIZE*6);
	}
	
	private void setModel(BGameModel model) {
		_model = model;
	}
	
	private BGameModel model(){
		return _model;
	}

	/**
	 * 
	 * @param problem
	 */
	public void setProblem( BProblem problem ){
		_problem = problem;
	}
	
	private void setProblem( BProblem problem, IBPoint questionPosition ){
		if( problem == null ){
			return;
		}
		BPlatform f = BPlatform.instance();
		_problem = problem;
        _problem.setSkipBorder(false);
		
		
		_set1Sprites = new BSprite[_problem.set1().length];
		for( int i = 0 ; i < _set1Sprites.length ; i++ ){
			//_set1Sprites[i] = f.sprite(_problem.set1()[i]);
			_set1Sprites[i] = new BDelayedSprite(_problem.set1()[i]);
		}
		_set2Sprites = new BSprite[_problem.set2().length];
		for( int i = 0 ; i < _set2Sprites.length ; i++ ){
			//_set2Sprites[i] = f.sprite(_problem.set2()[i]);
			_set2Sprites[i] = new BDelayedSprite(_problem.set2()[i]);
		}
		//_questionSprite = f.sprite( _problem.image1() );
		_questionSprite = new BDelayedSprite( _problem.image1());
		
		_allSprites = new BSprite[_set1Sprites.length+_set2Sprites.length+1];
		System.arraycopy(_set1Sprites, 0, _allSprites, 0, _set1Sprites.length );
		System.arraycopy(_set2Sprites, 0, _allSprites, _set1Sprites.length, _set2Sprites.length );
		_allSprites[_set1Sprites.length+_set2Sprites.length] = _questionSprite;
		
		for( BSprite s: _allSprites ){
			s.setAntialias(true);
		}
		
		alignSprites(400, questionPosition);
		
		for( BSprite s: _allSprites ){
			addDrawable(s);
		}

	}
	
	private static IBPoint spritePosition( int column, int row ){
		if( column < 0 || row < 0 ){
			return BPlatform.instance().point(TILE_SIZE*2,TILE_SIZE*3);
		}
		
		double colx = column == 0 ? .8 : 3.2;
		
		return BPlatform.instance().point(TILE_SIZE*colx, TILE_SIZE*(row+1) );
	}
	
	
	
	private void alignSprites(int millis, IBPoint questionPosition ){
		_questionSprite.transform().toIdentity().translate(questionPosition.x(), questionPosition.y());
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBPoint p = spritePosition(0,i);
			_set1Sprites[i].transform().toIdentity().translate(p.x(),p.y());
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBPoint p = spritePosition(1,i);
			_set2Sprites[i].transform().toIdentity().translate(p.x(),p.y());
		}
	}
		
		
	private void alignSprites_animation(int millis, IBPoint questionPosition ){	
		
		BAnimator animator = animator();

		
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBTemporaryTransformAnimable a = _set1Sprites[i];
			animator.addAnimation( new BTranslateAnimation( spritePosition(0,i), millis, a ) );
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBTemporaryTransformAnimable a = _set2Sprites[i];
			animator.addAnimation( new BTranslateAnimation( spritePosition(1,i), millis, a ) );
		}

		animator.addAnimation(
			new BCompoundTransformAnimation(
				new IBTemporaryTransformAnimable[]{_questionSprite },
				new BTranslateAnimation( questionPosition , millis ),
				new BRotateAnimation(2*Math.PI/millis, millis)
			)
		);
	}

	
	/**
	 * 
	 */
	@Override
	protected void draw_internal(IBCanvas canvas){
		super.draw_internal(canvas );
		if( SHOW_POINTER ){
			pointer().draw(canvas, canvasContext().transform() );
		}
	}
	
	private BDrawable pointer() {
		if (_pointer == null) {
			_pointer = platform().label("O");
		}
		return _pointer;
	}

	@Override
	public IBRectangle originalSize() {
		if (_size == null) {
			_size = computeOriginalSize();
		}
		return _size;
	}
	
	private BAnimator animator(){
		return platform().game().animator();
	}
	
	public boolean correctAnswer(){
		return _correctAnswer;
	}
	
	public boolean badAnswer(){
		return _badAnswer;
	}

	
	@Override
	public BBox icon(){
		if( correctAnswer() ){
			return _correctIcon;
		}
		if( badAnswer() ){
			return _badIcon;
		}
		return _icon;
	}


	private IBAnimation createPickUpAnimation() {
		return new BScaleAnimation(FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _questionSprite);
	}

	private IBAnimation createDragAnimation(IBPoint p, int millis) {
		return new BWaitForAnimation(new BTranslateAnimation(p, millis, _questionSprite ), _pickUpAnimation);
	}

	private BWaitForAnimation createOutSetAnimation( BSprite[] setSprites, IBAnimation setOverAnimation) {
		return new BWaitForAnimation( new BScaleAnimation(1/SPRITE_OVER_ZOOM,1/SPRITE_OVER_ZOOM,FOCUS_DELAY,setSprites), setOverAnimation);
	}

	private BScaleAnimation createOverSetAnimation( BSprite[] setSprites ) {
		return new BScaleAnimation( SPRITE_OVER_ZOOM, SPRITE_OVER_ZOOM, FOCUS_DELAY, setSprites );
	}

	private BWaitForAnimation createDropAnimation(IBPoint dest) {
		return new BWaitForAnimation( 
				new BCompoundTransformAnimation(
						new IBTemporaryTransformAnimable[]{ _questionSprite }, 
						new BScaleAnimation(1/FOCUS_ZOOM, 1/FOCUS_ZOOM, FOCUS_DELAY),
						new BTranslateAnimation( dest, FOCUS_DELAY)
				),
				_pickUpAnimation );
	}

	@SuppressWarnings("serial")
	private static class MyState extends BState{
		
		private BProblem _myProblem;
		private boolean _myBadAnswer;
		private boolean _myCorrectAnswer;
		private IBPoint _myPoint;
		private BGameModel _myModel;

		public MyState(BGameField gf) {
			_myProblem = gf._problem;
			_myBadAnswer = gf.badAnswer();
			_myCorrectAnswer = gf.correctAnswer();
			IBPoint p = spritePosition(-1,-1);
			if( gf._questionSprite != null){
				p = gf._questionSprite.position();
			}
			_myPoint = p;
			_myModel = gf._model;
		}

		@Override
		public BGameField create() {
			BGameField ret = new BGameField();
			ret.restore(this);
			return ret;
		}
	}
	
	@Override
	public BState<BGameField> save() {
		return new MyState(this);
	}
	
	private void restore(MyState state){
		init();
		_badAnswer = state._myBadAnswer;
		_correctAnswer = state._myCorrectAnswer;
		setModel(state._myModel);
		setProblem(state._myProblem,state._myPoint);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		MyState state = (MyState) stream.readObject();
		restore(state);
		dispose(); // TO SAVE MEMORY UNTIL DISPLAYED
	}

	private void writeObject(ObjectOutputStream stream)	throws IOException {
		stream.writeObject(save());
	}

	@Override
	public void dispose() {
		_problem.dispose();
		removeDrawables();
	}
	

	@Override
	public void setUp(){
		if( !disposed() ){
			return;
		}
		_problem.setUp();
		IBPoint position = spritePosition(-1, -1);
		if( _questionSprite != null ){
			position = _questionSprite.position();
		}
		setProblem(_problem, position);
		init();
	}

	@Override
	public boolean disposed() {
		return _problem.disposed();
	}

	
	private transient IBDrawable _drawable;
	
	@Override
	public IBDrawable drawable() {
		if (_drawable == null) {
			_drawable = new BZoomDrawable(this);
			
		}
		return _drawable;
	}
}
