package com.game.render.fbo.psProcess.program.userState;

import com.badlogic.gdx.Gdx;
import com.game.render.EscapyGdxCamera;
import com.game.render.fbo.psProcess.EscapyPostProcessed;
import com.game.render.fbo.psProcess.lights.vol.SimpleVolLight;
import com.game.render.fbo.psProcess.program.FBORenderProgram;
import com.game.render.fbo.userState.NormalMapFBO;
import com.game.render.shader.blend.userState.EscapyStdNrmlShader;

// TODO: Auto-generated Javadoc
/**
 * The Class FBOVolumeLightProgram.
 */
public class FBOVolumeLightProgram extends FBORenderProgram<NormalMapFBO> {

	private EscapyStdNrmlShader nrmlShader;
	
	/**
	 * Instantiates a new FBO volume light program.
	 *
	 * @param fboProgramTarget
	 *            the fbo program target
	 */
	public FBOVolumeLightProgram(NormalMapFBO fboProgramTarget) 
	{
		super(fboProgramTarget);
		this.nrmlShader = new EscapyStdNrmlShader();
	}
	

	/* (non-Javadoc)
	 * @see com.game.render.fbo.psProcess.program.FBORenderProgram#renderProgram(com.game.render.EscapyGdxCamera, com.game.render.fbo.psProcess.EscapyPostProcessed)
	 */
	@Override
	public void renderProgram(EscapyGdxCamera camera, EscapyPostProcessed ePP) 
	{
		this.nrmlShader.renderNrmLight(super.fbo.getTargetTextureRegion(), 
				super.fbo.getNormalMapTexureRegion(), 0, 0, Gdx.graphics.getWidth(), 
				Gdx.graphics.getHeight(), camera.getCamera(), (SimpleVolLight) ePP);

	}

}