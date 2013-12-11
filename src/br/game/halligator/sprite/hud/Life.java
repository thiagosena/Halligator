package br.game.halligator.sprite.hud;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Life extends AnimatedSprite {

	public Life(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

	public void setAlive(){
		setCurrentTileIndex(0);
	}
	
	
	public void setDead(){
		setCurrentTileIndex(1);
	}
}
