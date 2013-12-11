package br.game.halligator.sprite;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.andengine.entity.sprite.AnimatedSprite;

public class Alligator extends Sprite {
	
	private AnimatedSprite pbowser;
	
	public Alligator(float pX, float pY, TextureRegion mBowserTextureRegion, AnimatedSprite bowser) {
		super(pX, pY, mBowserTextureRegion);
		this.pbowser = bowser;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
}