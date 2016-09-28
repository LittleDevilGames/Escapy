package com.game.render.shader.volumeLight;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.game.render.shader.EscapyShaderRender;
import com.game.utils.translationVec.TransVec;

public class EscapyVolumeRenderer extends EscapyShaderRender {

	private ShaderProgram volShader;
	private static final String colorMap = "colorMap", lightMap = "lightMap", normalMap = "normalMap",
								fieldSize = "fieldSize", ambientIntensity = "ambientIntensity",
								directIntensity = "directIntensity", shadowIntensity = "shadowIntensity",
								spriteSize = "spriteSize", luminance = "luminance";
	
	private String FRAGMENT_NAME = "_";
	
	public EscapyVolumeRenderer(int ID, String VERTEX, String FRAGMENT) {
		super(ID);
		this.initShaderProgram(VERTEX, FRAGMENT);
	}
	
	public EscapyVolumeRenderer(String VERTEX, String FRAGMENT) {
		super();
		this.initShaderProgram(VERTEX, FRAGMENT);
	}
	
	

	@Override
	public EscapyShaderRender initShaderProgram(String VERTEX, String FRAGMENT) {
		ShaderProgram.pedantic = false;
		this.volShader = new ShaderProgram(new FileHandle(VERTEX), new FileHandle(FRAGMENT));
		this.FRAGMENT_NAME = super.removeFRAG(FRAGMENT);
		super.checkStatus(volShader);
		return this;
	}
	
	
	public void renderVolumeLights(float x, float y, Texture cMap, Texture nMap, TransVec dim,
			float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		this.renderVolumeLights(cMap, x, y, cMap, nMap, dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, camera);
	}
	
	
	public void renderVolumeLights(Sprite cMap, Sprite nMap, TransVec dim,
			float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		this.renderVolumeLights(cMap, cMap, nMap, dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, camera);
	}
	
	public void renderVolumeLights(float x, float y, TextureRegion cMap, TextureRegion nMap,
								   TransVec dim, float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		this.renderVolumeLights(cMap, x, y, cMap, nMap, dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, camera);
	}
	
	
	public void renderVolumeLights(Texture target, float x, float y, Texture cMap, Texture nMap, TransVec dim,
			float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		
		this.volShader = initShader(
				target, cMap, nMap, dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, volShader);
		super.drawTexture(target, camera, x, y);
	}
	
	
	public void renderVolumeLights(Sprite target, Sprite cMap, Sprite nMap, TransVec dim,
			float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		this.volShader = initShader(
				target.getTexture(), cMap.getTexture(), nMap.getTexture(),
				dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, volShader);
		super.drawSprite(target, camera);
	}
	
	public void renderVolumeLights(TextureRegion target, float x, float y, TextureRegion cMap, TextureRegion nMap,
								   TransVec dim, float amIntensity, float dirIntensity, float shdIntensity, float sprtSize, float lum, OrthographicCamera camera) {
		
		this.volShader = initShader(
				target.getTexture(), cMap.getTexture(), nMap.getTexture(),
				dim, amIntensity, dirIntensity, shdIntensity, sprtSize, lum, volShader);
		super.drawTextureRegion(target, camera, x, y, dim.x, dim.y);
	}
	
	
	private ShaderProgram initShader(Texture tMap, Texture cMap, Texture nMap, TransVec dim,
			float amIntensity, float dirIntensity, float shadIntensity, float sprtSize, float lum, ShaderProgram shader) {
		shader.begin();
		{
			//lMap.bind(2);
			nMap.bind(1);
			cMap.bind(0);
			super.batcher.setShader(shader);
			//shader.setUniformi(lightMap, 2);
			shader.setUniformi(normalMap, 1);
			shader.setUniformi(colorMap, 0);
			shader.setUniformf(fieldSize, dim.x, dim.y);
			shader.setUniformf(ambientIntensity, amIntensity);
			shader.setUniformf(directIntensity, dirIntensity);
			shader.setUniformf(shadowIntensity, shadIntensity);
			shader.setUniformf(spriteSize, sprtSize);
			shader.setUniformf(luminance, lum);
		}
		shader.end();
		return shader;
	}
	
	@Override
	public String toString() {
		return "EscapyVolumeRenderer"+"_"+this.FRAGMENT_NAME+"_"+super.id;
	}




}
