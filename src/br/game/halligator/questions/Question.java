package br.game.halligator.questions;

import java.util.ArrayList;
import java.util.Collections;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Question {
	int id;
	String content; //conteudo da pergunta
	String imageName;
	BitmapTextureAtlas image; //imagem da pergunta
	
	ArrayList<Awnser> awnsers = new ArrayList<Awnser>();

	/** Pushes the 0th awnser of array... null if doesn't have more awnsers to be returned
	 * 
	 * @return the awnser or null
	 */
	public Awnser pushAwnser(){
		Awnser a;
		try{
			a = awnsers.remove(0);
		} catch (Exception e){
			a = null;
		}
		return a;
	}
	/** Gets an awnser by id... should be 0,1,2,3! if you push before, then will sglovilis.
	 * 
	 * @param id should be 0,1,2,3
	 * @return the awnser
	 */
	public Awnser getAwnserByOrder(int order){
		Awnser a;
		try{
			a = awnsers.get(order);
		} catch (Exception e){
			a = null;
		}
		return a;
	}
	/** Gets the correct awnser... if you push before, then will sglovilis.
	 * 
	 * @return the awnser
	 */
	public Awnser getCorrectAwnser(){
		for (Awnser a : awnsers){	
			if (a.isCorrect()){
				return a;
			}
		}
		return null; //aqui é que nem vestibular, retorna nulo somente se nao tiver resposta correta! :)	
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageName() {
		return imageName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImageName(String imageName) { //aqui eh um bom lugar para instanciar o bitmaptextureatlas image!
		this.imageName = imageName;
	}
	public void addAwnser(Awnser a){
		awnsers.add(a);
	}
	public void shuffleAwnsers(){ //embaralhar respostas... 
		Collections.shuffle(awnsers);
	}
}
