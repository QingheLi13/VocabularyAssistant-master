package com.limi.andorid.vocabularyassistant.helper;

import android.util.Log;

import com.limi.andorid.vocabularyassistant.data.Word;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by limi on 16/4/27.
 */
public class WordImportHandler {

    public static ArrayList<Word> systemWordBaseArrayList = new ArrayList<>();


    public static void getDataFromXml(InputStream is, String wordBase) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            if (wordBase.equalsIgnoreCase("IETLS") || wordBase.equalsIgnoreCase("TOEFL")) {
                Word.setLastID();
            }
            NodeList wordList = doc.getElementsByTagName("item");
            Log.d("Word nodes:", String.valueOf(wordList.getLength()));
            for (int i = 0; i < wordList.getLength(); i++) {

                String wordName = null;
                String trans = "";
                String tags = null;
                String phonetic = null;


                Node aWord = wordList.item(i);
                Element elem = (Element) aWord;

                for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling()) {
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        String name = node.getNodeName();
                        if (name.equals("word")) {
                            wordName = node.getFirstChild().getNodeValue();
                        } else if (name.equals("trans")) {
                            if (wordBase.equalsIgnoreCase("GRE threek Words")) {
                                String s = node.getFirstChild().getNodeValue();
                                String transArray[] = s.split("\n");
                                for (String sss : transArray) {
                                    if (sss.charAt(0) >= '0' && sss.charAt(0) <= '9') {
                                        if (!trans.equals("")) {
                                            trans += "\n";
                                        }
                                        trans += sss;
                                    }
                                }
                            }
                            if (wordBase.equalsIgnoreCase("IETLS")) {
                                trans = node.getFirstChild().getNodeValue();
                            }

                            if (wordBase.equalsIgnoreCase("TOEFL")) {
                                trans = node.getFirstChild().getNodeValue();
                            }

                        } else if (name.equals("phonetic")) {
                            phonetic = node.getFirstChild().getNodeValue();
                        }
                    }
                }
                Word word = new Word(wordName, trans, wordBase, phonetic);
                systemWordBaseArrayList.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
