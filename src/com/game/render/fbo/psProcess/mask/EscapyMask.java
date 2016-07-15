package com.game.render.fbo.psProcess.mask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.game.render.EscapyGdxCamera;
import com.game.render.fbo.EscapyFBO;
import com.game.render.fbo.EscapyMultiFBO;
import com.game.render.fbo.psProcess.cont.LightMaskContainer;
import com.game.render.fbo.psProcess.program.FBORenderProgram;
import com.game.render.fbo.psProcess.program.userState.FBOMultiplyMaskProgram;
import com.game.render.fbo.psRender.EscapyPostRenderable;


// TODO: Auto-generated Javadoc
/**
 * GL based lightmask superclass.
 *
 * @author Henry
 * @see LightMaskContainer
 */
public abstract class EscapyMask implements EscapyPostRenderable {

	/** The Constant MULTIPLY. */
	public final static int MULTIPLY = 0;
	
	/** The Constant COLOR_DODGE. */
	public final static int COLOR_DODGE = 1;
	
	/** The height. */
	protected float WIDTH, HEIGHT;
	
	/** The start Y. */
	protected float startX, startY;
	
	/** The color. */
	protected Color COLOR = new Color();
	
	/** The mask batch. */
	protected Batch maskBatch;
	
	/** The post render camera. */
	protected EscapyGdxCamera postRenderCamera;

	private EscapyMultiFBO linkedFBO;
	
	/**
	 * Instantiates a new escapy mask.
	 */
	public EscapyMask() {
		this.maskBatch = new SpriteBatch();
		this.postRenderCamera = new EscapyGdxCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.linkedFBO = this.initMaskFBO();
	}
	
	/**
	 * Instantiates a new escapy mask.
	 *
	 * @param postRenderCamera
	 *            the post render camera
	 */
	public EscapyMask(EscapyGdxCamera postRenderCamera) {
		this.maskBatch = new SpriteBatch();
		this.postRenderCamera = postRenderCamera;
		this.linkedFBO = this.initMaskFBO();
	}
	
	/**
	 * Inits the mask.
	 *
	 * @return the escapy multi FBO
	 */
	protected abstract EscapyMultiFBO initMaskFBO();
	
	/**
	 * Adds the mask target.
	 *
	 * @param targetBuffer
	 *            the target buffer
	 * @return the escapy mask
	 */
	public abstract EscapyMask addMaskTarget(FrameBuffer targetBuffer);
	
	/**
	 * Sets the mask position.
	 *
	 * @param sx
	 *            - start x point
	 * @param sy
	 *            - start y point
	 * @param width
	 *            - Width
	 * @param height
	 *            - Height
	 * @return mask.
	 */
	public EscapyMask setMaskPosition(float sx, float sy, float width, float height)
	{
		this.startX = sx;
		this.startY = sy;
		this.WIDTH = width;
		this.HEIGHT = height;
		return this;
	}

	/**
	 * Sets the color.
	 *
	 * @param color
	 *            the color
	 * @return the escapy mask
	 */
	public EscapyMask setColor(Color color) {
		COLOR = color;
		System.out.println(COLOR.r);
		System.out.println(COLOR.g);
		System.out.println(COLOR.b);
		System.out.println(COLOR.a);
		return this;
	}
	
	/**
	 * Set blend mode.
	 * @param mode - blend mode
	 * @return mask
	 */
	public EscapyMask setMode(int mode) {
		this.linkedFBO.setRenderProgram(modeType(mode, linkedFBO));
		return this;
	}
	
	/**
	 * Sets the batch.
	 *
	 * @param maskBatch
	 *            - {@link Batch} cannot be null.
	 * @return mask.
	 */
	public EscapyMask setBatch(Batch maskBatch) {
		this.maskBatch = maskBatch;
		return this;
	}
	
	/**
	 * Restore standart Batch.
	 * @return mask
	 */
	public EscapyMask setDefaultBatch() {
		this.maskBatch = new SpriteBatch();
		return this;
	}
	
	/**
	 * Apply color.
	 *
	 * @param <MASKFBO>
	 *            the generic type
	 * @param maskFBO
	 *            the mask FBO
	 * @return the maskfbo
	 */
	protected <MASKFBO extends EscapyFBO> MASKFBO applyColor(MASKFBO maskFBO) {
		
		maskFBO.begin();
		Gdx.gl.glClearColor(this.COLOR.r, this.COLOR.g, this.COLOR.b, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		maskFBO.end();
		return maskFBO;
	}
	
	private FBORenderProgram<EscapyMultiFBO> modeType(int mode, EscapyMultiFBO fbo)
	{	
		if (mode == MULTIPLY)
			return (new FBOMultiplyMaskProgram(fbo));
		if (mode == COLOR_DODGE)
			return (new FBOMultiplyMaskProgram(fbo));
		return (new FBOMultiplyMaskProgram(fbo));
	}
	
	/* (non-Javadoc)
	 * @see com.game.render.fbo.psRender.EscapyPostRenderable#setPostRenderCamera(com.game.render.EscapyGdxCamera)
	 */
	@Override
	public EscapyPostRenderable setPostRenderCamera(EscapyGdxCamera camera) {
		this.postRenderCamera = camera;
		return this;
	}
	
	
}
