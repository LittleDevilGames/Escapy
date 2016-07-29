package com.game.render.fbo.psProcess.cont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.render.EscapyGdxCamera;
import com.game.render.fbo.EscapyFBO;
import com.game.render.fbo.EscapyMultiFBO;
import com.game.render.fbo.StandartFBO;
import com.game.render.fbo.StandartMultiFBO;
import com.game.render.fbo.excp.EscapyFBOtypeException;
import com.game.render.fbo.psProcess.lights.stdLS.AbsStdLight;
import com.game.render.fbo.psProcess.program.FBORenderProgram;
import com.game.render.fbo.psProcess.program.FBOStdBlendProgramFactory;
import com.game.render.fbo.psRender.EscapyPostExec;
import com.game.render.fbo.psRender.EscapyPostIterative;
import com.game.render.fbo.psRender.EscapyPostRenderable;
import com.game.render.shader.blend.EscapyBlendRenderer;
import com.game.utils.absContainer.EscapyAbsContainer;
import com.game.utils.translationVec.TransVec;

public class LightContainer extends EscapyAbsContainer<AbsStdLight>
	implements EscapyPostExec <EscapyMultiFBO>, EscapyPostIterative, EscapyFBOContainer {

//	public final static FBOStdBlendProgramFactory light = new FBOStdBlendProgramFactory();
	
	public final static class light extends FBOStdBlendProgramFactory {};
	
	private EscapyGdxCamera postRenderCamera;
	private EscapyMultiFBO lightFBO;
	private EscapyFBO ortoFBO;
	
	private final static String VERTEX ="shaders\\blend\\colorMix\\colorMix.vert";
	private final static String FRAGMENT ="shaders\\blend\\colorMix\\colorMix.frag";
	
	private EscapyBlendRenderer blender;
	
	private Batch batch;
	
	public LightContainer() {
	}
	public LightContainer(EscapyFBO mutliFBO) {
		this.setPostRenderFBO(mutliFBO);
		this.setRenderProgram(FBOStdBlendProgramFactory.softLight(lightFBO));
	}
	public LightContainer(EscapyFBO mutliFBO, FBORenderProgram<EscapyMultiFBO> program) {
		this.setPostRenderFBO(mutliFBO);
		this.setRenderProgram(program);
	}
	public LightContainer(EscapyFBO mutliFBO, EscapyGdxCamera postRenderCamera) {
		this.setPostRenderFBO(mutliFBO);
		this.setPostRenderCamera(postRenderCamera);
		this.setRenderProgram(FBOStdBlendProgramFactory.softLight(lightFBO));
	}
	public LightContainer(EscapyFBO mutliFBO, EscapyGdxCamera postRenderCamera, 
			FBORenderProgram<EscapyMultiFBO> program) {
		this.setPostRenderFBO(mutliFBO);
		this.setPostRenderCamera(postRenderCamera);
		this.setRenderProgram(program);
		
	}
	
	
	@Override
	protected void initContainer() {
		super.initContainer();
		this.postRenderCamera = new EscapyGdxCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.lightFBO = new StandartMultiFBO();
		this.batch = new SpriteBatch();
		this.blender = new EscapyBlendRenderer(VERTEX, FRAGMENT, "targetMap", "blendMap");
		this.ortoFBO = new StandartFBO();
	}
	
	
	
	
	@Override
	public LightContainer mergeContainedFBO() {	
		return this.mergeContainedFBO(this.postRenderCamera);
	}
	public LightContainer mergeContainedFBO(int iterations) {	
		return this.mergeContainedFBO(this.postRenderCamera, iterations);
	}
	@Override
	public LightContainer mergeContainedFBO(EscapyGdxCamera camera) {
		
		EscapyFBO temp = renderContainedFBO(camera);
		lightFBO.forceRenderToFBO(temp).endMergedBuffer();
		return this;
	}
	public LightContainer mergeContainedFBO(EscapyGdxCamera camera, int iterations) {
		
		EscapyFBO temp = renderContainedFBO(camera);
		lightFBO.begin();
		while (iterations > 0) {
			lightFBO.renderToFBO(temp);
			iterations -=1;
		}
		lightFBO.endMergedBuffer();
		return this;
	}
	
	public EscapyFBO renderContainedFBO(EscapyGdxCamera camera) {
		
		super.targetsList.forEach( light -> light.preRender(camera));
		
		this.ortoFBO.begin().wipeFBO();
		
		this.targetsList.forEach( light -> {
		
			this.blender.renderBlended(
					ortoFBO.getTextureRegion(), 
					light.getFBO().getTextureRegion(),0,0, 
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 
					postRenderCamera.getCamera());
		
		
		});
		this.ortoFBO.end();
		return ortoFBO;
	}
	
	
	
	public LightContainer mergeContainedFBO_ALT() {
		return this.mergeContainedFBO_ALT(postRenderCamera);
	}
	public LightContainer mergeContainedFBO_ALT(EscapyGdxCamera camera) {
		
		this.lightFBO.begin();
		this.renderContainedFBO_ALT(camera);
		this.lightFBO.end().mergeBuffer();
		
		return this;
	}
	public LightContainer mergeContainedFBO_ALT(EscapyGdxCamera camera, EscapyFBO fbo) {
		this.lightFBO.begin();
		fbo.renderFBO();
		this.renderContainedFBO_ALT(camera);
		this.lightFBO.end().mergeBuffer();
		
		return this;
	}
	
	public LightContainer mergeALT(EscapyGdxCamera camera) {
		super.targetsList.forEach( light -> light.preRender(camera));
		this.lightFBO.begin();
		batch.setProjectionMatrix(camera.combined());
		batch.begin();
		super.targetsList.forEach(
				light -> batch.draw(light.getFBO().getTextureRegion(), 0, 0, 
						Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
				);
		batch.end();
		this.lightFBO.endMergedBuffer();
		return this;
	}
	
	public LightContainer renderContainedFBO_ALT(EscapyGdxCamera camera) {
		
		
		
		int dst = batch.getBlendDstFunc();
		int scr = batch.getBlendSrcFunc();
		batch.setProjectionMatrix(camera.combined());
		
		batch.begin();
		batch.enableBlending();
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
			super.targetsList.forEach(
					light -> batch.draw(light.getFBO().getTextureRegion(), 0, 0, 
							Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
					);
		
			batch.setBlendFunction(scr, dst);
		batch.disableBlending();
		batch.end();
		
		return this;
	}
	
	@Override
	public EscapyFBO postRender(EscapyFBO fbo, TransVec translationVec, int times) {
		fbo.begin();
		while (times > 0) {
			this.postRender(translationVec);
			times -=1;
		}	fbo.end().mergeBuffer();
		return fbo;
	}
	
	@Override
	public EscapyFBO postRender(EscapyFBO fbo, TransVec translationVec) {

		return this.postRender(fbo, translationVec, 1);
	}

	@Override
	public void postRender(TransVec translationVec) {
		this.lightFBO.renderFBO(postRenderCamera);
	}

	@Override
	public <T extends EscapyFBO> EscapyPostRenderable setPostRenderFBO(T postRednerFBO) throws EscapyFBOtypeException {
		if (postRednerFBO instanceof EscapyMultiFBO) 
			this.lightFBO = (EscapyMultiFBO) postRednerFBO;
		else throw new EscapyFBOtypeException();
		return this;
	}

	@Override
	public EscapyMultiFBO getPostRenderFBO() {
		return this.lightFBO;
	}

	@Override
	public EscapyPostRenderable setPostRenderCamera(EscapyGdxCamera camera) {
		this.postRenderCamera = camera;
		return this;
	}
	@Override
	public EscapyPostRenderable setRenderProgram(FBORenderProgram<EscapyMultiFBO> program) {
		if (program.getFBOTarget() != this.lightFBO)
			program.setFBOTarget(lightFBO);
		this.lightFBO.setRenderProgram(program);
		return this;
	}
	
	

	


	

}
