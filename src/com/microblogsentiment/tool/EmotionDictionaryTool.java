package com.microblogsentiment.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.mircoblogsentiment.object.EmotionalWord;
import com.mircoblogsentiment.object.LevelWord;
import com.mircoblogsentiment.object.ViewWord;

public class EmotionDictionaryTool {

	public static Map<String, Double> SetPosEmotionalWords(String path) {
		File file = new File(path);
		Map<String, Double> poswords = new HashMap<String, Double>();
		String text = null;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.equals(""))
					continue;

				if (!text.contains("中文正面")) {
					EmotionalWord word = new EmotionalWord();
					text = text.substring(0, text.length() - 1);
					// System.out.println(text+":"+text.length());
					word.setContext(text);
					word.setEmotionpoint(1);
					poswords.put(text, word.getEmotionpoint());
				}
			}
			br1.close();
			text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return poswords;
	}

	public static Map<String, Double> SetNegEmotionalWords(String path) {
		File file = new File(path);
		Map<String, Double> negwords = new HashMap<String, Double>();
		String text = null;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.equals(""))
					continue;

				if (!text.contains("中文负面")) {
					EmotionalWord word = new EmotionalWord();
					text = text.substring(0, text.length() - 1);
					word.setContext(text);
					word.setEmotionpoint(-1);
					negwords.put(text, word.getEmotionpoint());
				}
			}
			br1.close();
			text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return negwords;
	}

	public static Map<String, Double> SetLevelWords(String path) {
		File file = new File(path);
		Map<String, Double> levelwords = new HashMap<String, Double>();
		String text = null;
		double point = 0;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.equals(""))
					continue;

				if (text.contains("中文程度级别词语")) {
					continue;
				} else {

					if (text.length() >= 2 && text.charAt(1) == '.') {
						String temp = text.substring(text.length() - 2);
						point = Double.parseDouble(temp);
						continue;
					}

					LevelWord word = new LevelWord();
					word.setContext(text);
					word.setLevelpoint(point);
					levelwords.put(text, word.getLevelpoint());
				}
			}
			br1.close();
			text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(text);
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(text);
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(text);
			System.out.println(e);
		}
		return levelwords;
	}

	public static Map<String, Double> SetViewWords(String path) {
		File file = new File(path);
		Map<String, Double> viewwords = new HashMap<String, Double>();
		String text = null;
		double point = 0;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.equals(""))
					continue;

				if (text.contains("中文主张词语")) {
					continue;
				} else {
					if (text.length() >= 2 && text.charAt(1) == '.') {
						String temp = text.substring(text.length() - 2);
						point = Double.parseDouble(temp);
						continue;
					}
					ViewWord word = new ViewWord();
					word.setContext(text);
					word.setViewpoint(point);
					viewwords.put(text, word.getViewpoint());
				}
			}
			br1.close();
			text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return viewwords;
	}

}
