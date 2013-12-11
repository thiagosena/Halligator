package br.game.halligator.texture;

import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;


public class TextureContainer<T extends BaseTextureRegion> {

	public final T texture;
	
	public final boolean pixels[][];
	
	public final String id;

	public TextureContainer(T texture, boolean[][] pixels, String id) {
		super();
		this.texture = texture;
		this.pixels = pixels;
		this.id = id;
	}

	@Override
	public TextureContainer<T> clone() {
		return new TextureContainer<T>(textureClone(), this.pixels, this.id);
	}
	
	@SuppressWarnings("unchecked")
	private T textureClone(){
		// because clone is public in child classes ...
		if (texture instanceof TextureRegion){
			return (T)((TextureRegion)texture).clone();
		}
		
		if (texture instanceof TiledTextureRegion){
			return (T)((TiledTextureRegion)texture).clone();
		}
		
		return null;
	}
}
