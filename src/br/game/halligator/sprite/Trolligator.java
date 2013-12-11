package br.game.halligator.sprite;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class Trolligator extends AnimatedSprite {

	public Trolligator(float pX, float pY, float pWidth, float pHeight, 
			ITiledTextureRegion pTiledTextureRegion, 
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	}

}
