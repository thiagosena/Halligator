package br.game.halligator.questions;

import java.util.ArrayList;

public class Game {
	String name;
	String type;
	ArrayList<Question> questions = new ArrayList<Question>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Game(String name){
		this.name = name;
	}
	
	
	/** Pushes the 0th question of array... null if doesn't have more questions to be returned
	 * 
	 * @return the question or null
	 */
	public Question pushQuestion(){
		Question q;
		try{
			q = questions.remove(0);
		} catch (Exception e){
			q = null;
		}
		return q;
	}
	public Question getQuestionById(int id){
		Question q;
		try{
			q = questions.get(id);
		} catch (Exception e){
			q = null;
		}
		return q;
	}
	public void addQuestion(Question q){
		questions.add(q);
	}
	public ArrayList<Question> getRandomQuestions(int size){
		ArrayList<Question> wQuestions = new ArrayList<Question>();
		for (int i = 0; i < size; i++){
			int index = (int) Math.round(questions.size()* Math.random());
			wQuestions.add(questions.get(index));
		}
		return wQuestions;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
}
