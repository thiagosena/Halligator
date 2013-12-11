package br.game.halligator.sprite.hud;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Point extends Sprite {
	public final int value;

	private double points = 0;
	private int attempts = 0; //tentativas, ate acertar
	private final int mode;
	
	/*constants*/
	public static final int MODE_DEFAULT = 0;
	public static final int MODE_CUMULATIVE = 1; //modo de jogo que respostas erradas tambŽm valem pontos!
	
	public Point(final int value, final int mode, float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.value = value;
		this.mode = mode;
	}

	public void setCorrect(){ //seria bom receber como parametro o tempo que o cara demorou p responder

		if (mode == MODE_DEFAULT){
			points += value;
		}
		
		else if (mode == MODE_CUMULATIVE){
			points += 1/Math.pow(2, attempts) * value;// 1/(2öx) * value
		}
		
		attempts = 0;
	}
	
	public void setWrong(){
		attempts++; //aumenta o numero de tentativas: 1, 2, 3, 4..
	}
	
	public String getStringPoints(){
		return ""+Math.ceil(points);
	}

	
	
	
}
