package com.game.screens.userState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.game.GameEnter;
import com.game.animator.EscapyAnimatorBase;
import com.game.characters.InitCharacters;
import com.game.controlls.PlayerControl;
import com.game.map.InitMap;
import com.game.physics_temp.EscapyPhysicsBase;
import com.game.render.EscapyGdxCamera;
import com.game.render.camera.program.CameraProgramFactory;
import com.game.render.extra.container.ExtraRenderContainer;
import com.game.render.extra.lightMap.EscapyLightMapRenderer;
import com.game.render.extra.lightMap.LightMapRenderer;
import com.game.render.extra.normalMap.EscapyNormalMapRender;
import com.game.render.extra.normalMap.NormalRenderer;
import com.game.render.extra.std.StdRenderer;
import com.game.render.fbo.EscapyFBO;
import com.game.render.fbo.StandartFBO;
import com.game.render.fbo.StandartMultiFBO;
import com.game.render.fbo.psProcess.cont.LightContainer;
import com.game.render.fbo.psProcess.cont.LightMaskContainer;
import com.game.render.fbo.psProcess.cont.VolumeLightsContainer;
import com.game.render.fbo.psProcess.lights.stdLS.userState.SimpleStdLight;
import com.game.render.fbo.psProcess.lights.vol.userState.SimpleVolLight;
import com.game.render.fbo.psProcess.mask.EscapyMask;
import com.game.render.fbo.psProcess.program.FBOStdBlendProgramFactory;
import com.game.render.fbo.psProcess.program.std.FBOStdMultiProgram;
import com.game.render.fbo.userState.NormalMapFBO;
import com.game.screens.EscapyMainState;
import com.game.screens.EscapyScreenState;
import com.game.update_loop.Updatable;
import com.game.utils.translationVec.TransVec;

// TODO: Auto-generated Javadoc
/**
 * The Class EscapyGameScreen.
 */
public class EscapyGameScreen extends EscapyScreenState implements Updatable, EscapyMainState {

	private InitMap mapContainer;
	private InitCharacters charactersContainer;
	private PlayerControl controlls;
	private EscapyPhysicsBase physics;
	private EscapyAnimatorBase animator;
	
	/** The player camera program ID. */
	protected int playerCameraProgramID;
	

	private int mouseLight;
	private int testLight;
	
	private float[] mpos, screen;
	private float dist, intencity;
	
	private EscapyFBO stdFBO, nrmlFBO, bgrFBO, lightBuffFBO,
						lightMapFBO, lightStdFBO, MAINFBO;
	private EscapyMask mask, bgrMask;
	
	private LightMaskContainer lightMask;
	private LightContainer stdLights;
	private VolumeLightsContainer volumeLights;
	private ExtraRenderContainer stdContainer, normalsContainer, bgrContainer, lightsMapContainer;
	
	private TransVec otherTranslationVec;
	
	
	/**
	 * Instantiates a new escapy game screen.
	 *
	 * @param escapyCamera
	 *            the escapy camera
	 * @param gameState
	 *            the game state
	 */
	public EscapyGameScreen(EscapyGdxCamera escapyCamera, GameEnter gameState) {
		super(escapyCamera, gameState);
		System.out.println("@constructor");

		return;
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		System.out.println("@show");
		if (!super.initializationEnded)
			this.initState();
	}

	
	
	/* (non-Javadoc)
	 * @see com.game.screens.EscapyMainState#initState()
	 */
	@Override
	public Screen initState() {
		System.out.println("@init state");

		
		this.animator = EscapyAnimatorBase.createAnimator().initAnimator().startAnimator();
		this.controlls = PlayerControl.playerController();
		this.mapContainer = new InitMap(super.settings.Location(), super.settings.getFrameWIDHT(),
				super.settings.getFrameHEIGHT(), super.settings.scaleRatio());
		this.charactersContainer = new InitCharacters();
		this.physics = new EscapyPhysicsBase(mapContainer.map()).startPhysics();
		this.charactersContainer.player().getPhysicalBody().setPosition(new float[] { 400, 10 });
		this.playerCameraProgramID = super.escapyCamera.getCameraProgramHolder()
				.addCameraProgram(CameraProgramFactory.stdCharacterProgram(this.charactersContainer.player()));
		
		
		this.lightBuffFBO = new StandartFBO();
		this.MAINFBO = new StandartFBO();
		this.bgrFBO = new StandartFBO(); 
		this.stdFBO = new StandartFBO();
		this.nrmlFBO = new NormalMapFBO(lightBuffFBO.getFrameBuffer());
		this.lightStdFBO = new StandartMultiFBO(stdFBO.getFrameBuffer());
		this.lightMapFBO = new StandartFBO();
		this.lightMask = new LightMaskContainer();
		this.bgrContainer = new ExtraRenderContainer(); 
		
		this.stdContainer = new ExtraRenderContainer();
		this.normalsContainer = new ExtraRenderContainer();
		this.lightsMapContainer = new ExtraRenderContainer();
		this.volumeLights = new VolumeLightsContainer(nrmlFBO, lightMapFBO);
		this.stdLights = new LightContainer(lightStdFBO, LightContainer.light.softLight());

		
	
		this.testLight = this.stdLights.addSource(new SimpleStdLight().scale(3.3f)
				.setPosition(400, 450).setColor(205, 107, 107));
		
		this.stdLights.addSource(new SimpleStdLight().scale(3.3f)
				.setPosition(400, 450).setColor(10, 50, 250));
		
		
		this.mouseLight = this.volumeLights.addSource(new SimpleVolLight(new float[] { 60, 60 }, 
				new float[] { 200, 150 }, new float[] { 1f, 1f, 1f }, 0.25f, 5f));
		
		this.mask = lightMask.standartMask().setMaskPosition(0, 0, Gdx.graphics.getWidth(), 
				Gdx.graphics.getHeight()).setMode(EscapyMask.MULTIPLY).addMaskTarget(stdFBO.getFrameBuffer());
		this.mask.setColor(new Color((40f/255f), (40f/255f), (40f/255f), 1f));
		
		this.bgrMask = lightMask.standartMask().setMaskPosition(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.bgrMask.setMode(EscapyMask.MULTIPLY).addMaskTarget(bgrFBO.getFrameBuffer());
		//XXX
		
		this.mpos = new float[2];
		this.screen = new float[] { 10, 10 };
		this.intencity = 0.35f;
		this.dist = 5f;
		
		this.otherTranslationVec = new TransVec();

		this.bgrContainer.addSource(new StdRenderer(mapContainer.backGround())); //XXX
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < mapContainer.objectSize()[mapContainer.indexTab()[i]]; j++) {
				this.stdContainer.addSource(new StdRenderer(mapContainer.gameObjects()[mapContainer.indexTab()[i]][j]));
				if (mapContainer.gameObjects()[mapContainer.indexTab()[i]][j] instanceof EscapyNormalMapRender)
					this.normalsContainer.addSource(new NormalRenderer((EscapyNormalMapRender) mapContainer.gameObjects()[mapContainer.indexTab()[i]][j]));
				if (mapContainer.gameObjects()[mapContainer.indexTab()[i]][j] instanceof EscapyLightMapRenderer)
					this.lightsMapContainer.addSource(new LightMapRenderer((EscapyLightMapRenderer) mapContainer.gameObjects()[mapContainer.indexTab()[i]][j]));
			}
		for (int i = 0; i < this.charactersContainer.getNpc().length; i++)
			this.stdContainer.addSource(new StdRenderer(charactersContainer.getNpc()[i]));
		
		this.stdContainer.addSource(new StdRenderer(charactersContainer.player()));
		this.normalsContainer.addSource(new NormalRenderer(charactersContainer.player()));
		
		for (int i = 0; i < mapContainer.objectSize()[mapContainer.indexTab()[4]]; i++) /** FRONT PARALLAX **/
			this.stdContainer.addSource(new StdRenderer(mapContainer.gameObjects()[mapContainer.indexTab()[4]][i]).setTranslationVec(otherTranslationVec.getTransVecArray()));
		//XXX
		
		super.initializationEnded = true;

		System.gc();	
		return this;
	}

	
	
	/**
	 * Upd dist.
	 */
	protected void updDist() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			this.dist += 5;
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			this.dist -= 5;

		if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
			this.screen[0] += 5;
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT))
			this.screen[0] -= 5;

		if (Gdx.input.isKeyJustPressed(Input.Keys.E))
			this.screen[1] += 5;
		if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT))
			this.screen[1] -= 5;
		if (Gdx.input.isKeyPressed(Input.Keys.C))
			this.intencity += 0.05f;
		if (Gdx.input.isKeyPressed(Input.Keys.Z))
			this.intencity -= 0.05f;

		this.mpos[0] = Gdx.input.getX();
		this.mpos[1] = (Gdx.graphics.getHeight() - Gdx.input.getY());
		
		if (Gdx.input.isKeyPressed(Input.Keys.C)) {
			this.stdLights.getSourceByID(this.testLight).setPosition(this.charactersContainer.player().getPhysicalBody().getBodyPosition());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F)|| Gdx.input.isTouched(0)) {
			this.stdLights.getSourceByID(this.testLight).setPosition(Gdx.input.getX() 
					-escapyCamera.getTranslationVector().x, Gdx.input.getY()-escapyCamera.getTranslationVector().y);
		}
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see com.game.update_loop.Updatable#update()
	 */
	@Override
	public void update() {
		
		
		this.volumeLights.getSourceByID(mouseLight).
			setPosition(new Vector2(mpos[0], mpos[1])).
			setDistance(dist).setIntencity(intencity - 0.4f);
		
		((SimpleVolLight) this.volumeLights.getSourceByID(mouseLight)).
		setDimension(new Vector2(screen[0], screen[1]));
		
		this.controlls.baseKeyboard_upd();

		this.charactersContainer.player().updateControlls(controlls.down_A(),controlls.down_D(),
				controlls.down_SPACE(), controlls.down_KEY_LSHIFT(), controlls.IS_MOVING(), false);
	}

	
	
	/* (non-Javadoc)
	 * @see com.game.screens.EscapyMainState#renderGameObjects(com.game.render.EscapyGdxCamera)
	 */
	@Override
	public void renderGameObjects(EscapyGdxCamera escapyCamera) {

		this.bgrFBO.begin().wipeFBO();
			this.bgrContainer.renderGraphic(escapyCamera);
		this.bgrFBO.end(); 
		
		this.stdFBO.begin().wipeFBO();
			this.stdContainer.renderGraphic(escapyCamera);
		this.stdFBO.end();
		
		this.lightMapFBO.begin().wipeFBO();
			this.lightsMapContainer.renderGraphic(escapyCamera);
		this.lightMapFBO.end();
	
		this.nrmlFBO.begin().wipeFBO();
		((NormalMapFBO) this.nrmlFBO).maskNormal();
			this.normalsContainer.renderGraphic(escapyCamera);
		this.nrmlFBO.end().mergeBuffer();
	}

	
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		this.updDist();
		super.escapyCamera.clear();
		this.MAINFBO.forceWipeFBO();
		this.lightStdFBO.forceWipeFBO();
		this.lightBuffFBO.forceWipeFBO();
		
		super.escapyCamera.holdCamera();
		
		this.renderGameObjects(escapyCamera);
		
		this.bgrMask.postRender(MAINFBO, escapyCamera.getTranslationVec());
		this.mask.postRender(MAINFBO, escapyCamera.getTranslationVec()); 
		
		
//	/*	
		this.stdLights.mergeContainedFBO(escapyCamera, 5);

		this.stdLights.postRender(lightBuffFBO, escapyCamera.getTranslationVec(), 4);
//		this.stdLights.postRender(MAINFBO, escapyCamera.getTranslationVec());

		this.volumeLights.postRender(MAINFBO, escapyCamera.getTranslationVec());		

//		lightBuffFBO.renderFBO();
		this.MAINFBO.forceRenderToFBO(lightBuffFBO).renderFBO();
//		this.MAINFBO.renderFBO();
//	*/	
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			this.pause();
			super.gameState.setScreen(super.gameState.getStatesContainer().getMenuScreen());
		} 
			
	} 

	
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		
		this.animator.closeAnimator();
		this.physics.closePhysic();

		/** TEST **/
		// super.escapyCamera.getCameraProgramHolder().removeCameraProgram(playerCameraProgramID);

		super.gameState.getStatesContainer().getUpdLoopedQueue().removeFromUpdQueueLast();
	}

	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		this.animator.initAnimator().startAnimator();
		this.physics.reInit().startPhysics();

		super.gameState.getStatesContainer().getUpdLoopedQueue().addToUpdQueue(this);
	}

		
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		try {
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
