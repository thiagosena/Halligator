package br.game.halligator.xmlparser;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import br.game.halligator.questions.Awnser;
import br.game.halligator.questions.Game;
import br.game.halligator.questions.Question;

import android.util.Log;
 
public class GameXMLParser extends DefaultHandler
{
        // ===========================================================
        // Fields
        // ===========================================================
		Game game;
		public ArrayList <Question> questions = new ArrayList<Question>();
		Question currentQuestion;
        boolean isCorrectGame;
        InputStream xmlFile;
		public GameXMLParser(Game game, InputStream xmlFile){
			this.game = game;
			this.xmlFile = xmlFile;
			try{
	            final SAXParserFactory spf = SAXParserFactory.newInstance();
	            final SAXParser sp = spf.newSAXParser();
	           
	            final XMLReader xmlReader = sp.getXMLReader();
	            xmlReader.setContentHandler(this);
                xmlReader.parse(new InputSource(new BufferedInputStream(xmlFile)));
			} catch(Exception e){
				Log.i("errroooo",e.toString());
				e.printStackTrace();
			}
		}
       
        private final StringBuilder mStringBuilder = new StringBuilder();
       

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================
       
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
        	if (qName.equals("game")){
               	if (game.getName().equals(getStringAttribute(attributes,"name"))){
        			isCorrectGame = true;
               	} else {
               		isCorrectGame = false;
               	}
        	}
        	if (isCorrectGame){

	        	if (qName.equals("question")){
	        		currentQuestion = new Question();
	                currentQuestion.setContent(getStringAttribute(attributes,"content"));
	                currentQuestion.setImageName(getStringAttribute(attributes,"image"));
	                currentQuestion.setId(getIntegerAttribute(attributes,"id"));
	         	}
	        	if (qName.equals("awnser")){
	        		Awnser a = new Awnser();
	        		a.setContent(getStringAttribute(attributes,"content"));
	        		a.setCorrect(getBooleanAttribute(attributes,"correct"));
	        		currentQuestion.addAwnser(a);
	        	}
       		}
        }
       
        @Override
        public void characters(final char[] pCharacters, final int pStart, final int pLength) throws SAXException
        {
                this.mStringBuilder.append(pCharacters, pStart, pLength);
        }
       
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {              
        	if (isCorrectGame){
	        	if (qName.equals("question")){
	        		game.addQuestion(currentQuestion);
	        	}
        	}
        	if (qName.equals("game")){
        		isCorrectGame = false;
        	}

        }
        final protected String getStringAttribute(Attributes attributes, String name) {
            int index = attributes.getIndex("", name);
            if (index >= 0) {
                    return attributes.getValue(index);
            }
            return null;
    }

    /**
     * Returns an Integer Attribute of the specified name
     * @param name Attribute Name
     * @return Integer Attribute
     */
    final protected Integer getIntegerAttribute(Attributes attributes, String name) {
            int index = attributes.getIndex("", name);
            if (index >= 0) {
                    Integer result = null;
                    try { 
                            result = Integer.parseInt(attributes.getValue(index));
                    } catch (NumberFormatException e) {
                    }
                    return result;
            }
            return null;
    }

    /**
     * Returns an Boolean Attribute of the specified name
     * @param name Attribute Name
     * @return Boolean Attribute
     */
    final protected Boolean getBooleanAttribute(Attributes attributes, String name) {
            int index = attributes.getIndex("", name);
            if (index >= 0) {
                    return Boolean.parseBoolean(attributes.getValue(index));
            }
            return null;
    }
}
 

