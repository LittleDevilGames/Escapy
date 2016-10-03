package com.game.map.objectsAlt.layers;

import com.game.render.fbo.EscapyFBO;
import com.game.render.fbo.StandartFBO;
import com.game.utils.arrContainer.EscapyArrContainer;

/**
 * @author Henry on 02/10/16.
 */
public class LayerContainer extends EscapyArrContainer <ObjectLayer> {

	public EscapyFBO layerFBO;

	public LayerContainer() {
		super(ObjectLayer.class);
	}
	public LayerContainer(int[] dimension, String ... name) {
		super(ObjectLayer.class);
		this.initFBO(dimension, name);
	}

	public LayerContainer initFBO(int[] dim, String ... name) {
		if (dim.length == 2) this.layerFBO = new StandartFBO(new int[]{0, 0, dim[0], dim[1]}, name);
		else if (dim.length == 4) this.layerFBO = new StandartFBO(dim, name);
		return this;
	}


}
