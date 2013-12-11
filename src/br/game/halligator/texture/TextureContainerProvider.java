package br.game.halligator.texture;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import br.game.halligator.activity.HalligatorActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class TextureContainerProvider {

	private static final boolean ALPHA = false;
	
	private static final boolean PIXEL = true;
	
	public static TextureContainer<TiledTextureRegion> produce(final Texture texture, final Context context, final String assetPath, final int texturePositionX, final int texturePositionY, final int tileColumns, final int tileRows){
		return new TextureContainer<TiledTextureRegion>(
				TextureRegionFactory.createTiledFromAsset(texture, context, assetPath, texturePositionX, texturePositionY, tileColumns, tileRows), 
				getPixels(assetPath, context, tileColumns, tileRows),
				assetPath
		);
	}
	
	public static TextureContainer<TextureRegion> produce(final Texture texture, final Context context, final String assetPath, final int texturePositionX, final int texturePositionY){
		return new TextureContainer<TextureRegion>(
				TextureRegionFactory.createFromAsset(texture, context, assetPath, texturePositionX, texturePositionY), 
				getPixels(assetPath, context, 1, 1),
				assetPath
		);
	}
	
	private static boolean[][] getPixels(final String assetPath, final Context context, final int tileColumns, final int tileRows){
		// get bitmap
		final Bitmap bitmap = getBitmap(assetPath, context);
		// check if there anything to do
		if (bitmap == null){
			return null;
		}
		// perform calculation
		final int xOffset = bitmap.getWidth() / tileColumns;
		final int yOffset = bitmap.getHeight() / tileRows; 
		final boolean[][] pixels = new boolean[xOffset][yOffset];
		// set pixels array
		for (int x = 0; x < xOffset; x++) {
			for (int y = 0; y < yOffset; y++) {
				pixels[x][y] = bitmap.getPixel(x, y) == Color.TRANSPARENT ? ALPHA : PIXEL;
			}
		}
		// end 
		return pixels;
	}
	
	private static Bitmap getBitmap(final String assetPath, final Context context){
		// prepare variables
		InputStream stream = null;
		Bitmap bitmap = null;
		// get bitmap;
        try {
            stream = context.getAssets().open(HalligatorActivity.GRAPHIC_ASSETS_PATH + assetPath);
            bitmap = BitmapFactory.decodeStream(stream);
            
        } catch (IOException e) {
        	Log.e("TextureContainerProvider", "Exception", e);
        } finally {
        	try {
        		if (stream != null){
        			stream.close();
        		}
			} catch (IOException e) {
				Log.e("TextureContainerProvider", "Exception", e);
			}
        }
        // done
        return bitmap;
	}
	
}
