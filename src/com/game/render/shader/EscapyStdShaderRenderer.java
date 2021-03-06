package com.game.render.shader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

// TODO: Auto-generated Javadoc
/**
 * The Class EscapyStdShaderRenderer.
 */
public class EscapyStdShaderRenderer extends EscapyShaderRender {


	/** The std program. */
	protected ShaderProgram stdProgram;
	
	
	/**
	 * Instantiates a new escapy std shader renderer.
	 */
	public EscapyStdShaderRenderer() {
		super();
		ShaderProgram.pedantic = false;
		this.stdProgram = SpriteBatch.createDefaultShader();

	}
	
	/**
	 * Instantiates a new escapy std shader renderer.
	 *
	 * @param id
	 *            the id
	 */
	public EscapyStdShaderRenderer(int id) {
		super(id);
		ShaderProgram.pedantic = false;
		this.stdProgram = SpriteBatch.createDefaultShader();

	}
	
	

	/**
	 * Gets the std program.
	 *
	 * @return the std program
	 */
	public ShaderProgram getStdProgram() {
		return stdProgram;
	}


	@Override
	public String toString() {
		return "EscapyStdShader_"+super.id;
	}


	@Override
	public EscapyShaderRender initShaderProgram(String VERTEX, String FRAGMENT) {
		ShaderProgram.pedantic = false;
		this.stdProgram = new ShaderProgram(new FileHandle(VERTEX), new FileHandle(FRAGMENT));
		super.checkStatus(stdProgram);
		return this;
	}

	
	
}
