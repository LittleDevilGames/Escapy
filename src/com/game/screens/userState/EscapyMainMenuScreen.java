package com.game.screens.userState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.GameEnter;
import com.game.render.EscapyGdxCamera;
import com.game.render.fbo.EscapyFBO;
import com.game.render.fbo.StandartFBO;
//import com.game.render.fbo.psProcess.lights.vol.AbsVolLight;
import com.game.render.fbo.psProcess.lights.vol.VolumeLightsExecutor;
import com.game.render.fbo.userState.NormalMapFBO;
import com.game.screens.EscapyScreenState;

// TODO: Auto-generated Javadoc
/**
 * The Class EscapyMainMenuScreen.
 */
public class EscapyMainMenuScreen extends EscapyScreenState {

	private Sprite testShaderSprite;
	private Sprite testNrmlSprite;

	private Texture nrmlMapTex;

	private float[] mpos, screen;
	private float dist, intencity;
	
	
	
	private VolumeLightsExecutor volumeLights;
	private int[] lightsID;
	private int mouseLight;
	private EscapyFBO nrmlFBO;
	private EscapyFBO stdFBO;
	private SpriteBatch batcher;
	private EscapyGdxCamera privGdxCamera;
	
	
	
	
	/**
	 * Instantiates a new escapy main menu screen.
	 *
	 * @param escapyCamera
	 *            the escapy camera
	 * @param game
	 *            the game
	 */
	public EscapyMainMenuScreen(EscapyGdxCamera escapyCamera, GameEnter game) 
	{
		super(escapyCamera, game);
		
		
		this.privGdxCamera = new EscapyGdxCamera(super.SCREEN_WIDTH, super.SCREEN_HEIGHT);
		
		
		this.testShaderSprite = new Sprite(new Texture("img\\splash\\BgLogo.png"));
		this.testShaderSprite.setSize(super.settings.getFrameWIDHT(), super.settings.getFrameWIDHT());
		this.testShaderSprite.flip(false, true);

		this.nrmlMapTex = new Texture("img\\splash\\LogoMap.png");
		this.testNrmlSprite = new Sprite(nrmlMapTex);
		this.testNrmlSprite.setSize(super.settings.getFrameWIDHT(), super.settings.getFrameWIDHT());
		this.testNrmlSprite.flip(false, true);
		
		
		

//		for (int i = 0; i < lights.length; i++) {
//			lights[i] = new SimpleVolLight(new float[] { 15, 15 }, new float[] { i * 200, i * 150 }, colors[i], 0.9f, 5f);
//		}

		this.mpos = new float[2];
		this.screen = new float[] { 60, 60 };
		this.intencity = 0.9f;
		this.dist = 5f;
	
		/**FIXME TEST FIXME**/
		this.stdFBO = new StandartFBO();
		this.nrmlFBO = new NormalMapFBO(stdFBO.getFrameBuffer());
		
		this.batcher = new SpriteBatch();
		this.volumeLights = new VolumeLightsExecutor(nrmlFBO);
	//	this.lightsID = new int[lights.length];
		
		//for (int i = 0; i < lightsID.length; i++)
		//	lightsID[i] = volumeLights.addSource(lights[i]);
		
		//this.mouseLight = volumeLights.addSource(new SimpleVolLight(new float[] { 60, 60 }, new float[] { 200, 150 },
		//		new float[] { 0.1f, 1f, 0.1f }, 0.35f, 5f));
		
		//this.volumeLights.setPostRenderFBO(nrmlFBO);
		/**FIXME TEST FIXME**/
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {

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
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			this.screen[0] -= 5;

		if (Gdx.input.isKeyJustPressed(Input.Keys.E))
			this.screen[1] += 5;
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			this.screen[1] -= 5;
		if (Gdx.input.isKeyPressed(Input.Keys.C))
			this.intencity += 0.05f;
		if (Gdx.input.isKeyPressed(Input.Keys.Z))
			this.intencity -= 0.05f;

		this.mpos[0] = Gdx.input.getX();
		this.mpos[1] = (Gdx.graphics.getWidth() - Gdx.input.getY());
		
		System.out.println(" ");
		System.out.println("DIST: " + dist);
		System.out.println("intencity: " + intencity);
		System.out.println("LightPos: " + mpos[0] + " : " + mpos[1]);
		System.out.println("ScreenPos: " + screen[0] + " : " + screen[1]);

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) 
	{	
		
		/**
		 * > 
		 * TODO render MENU 
		 * >
		 **/
		
		this.privGdxCamera.clear();
	
		this.stdFBO.begin().wipeFBO();
		{
			this.privGdxCamera.getCamera().update();
			this.batcher.setProjectionMatrix(privGdxCamera.getCamera().combined);
			this.batcher.begin();
				this.testShaderSprite.draw(batcher);
			this.batcher.end();
		}
		this.stdFBO.end();
		
		this.nrmlFBO.begin().wipeFBO();
		{
			this.privGdxCamera.getCamera().update();
			this.batcher.setProjectionMatrix(privGdxCamera.getCamera().combined);
			
			this.batcher.begin();
				this.testNrmlSprite.draw(batcher);
			this.batcher.end();
		}
		this.nrmlFBO.end().mergeBuffer();
		this.privGdxCamera.clear();

		this.privGdxCamera.getCamera().update();
//		this.volumeLights.postRender(stdFBO, privGdxCamera.getTranslationVec());
		this.stdFBO.renderFBO();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) /** Back to game **/
		{
			super.gameState.setScreen(super.gameState.getStatesContainer().getGameScreen().getScreen());
			super.gameState.getStatesContainer().getGameScreen().getScreen().resume();
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
	}

}
