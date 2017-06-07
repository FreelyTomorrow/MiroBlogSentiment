package com.mircoblogsentiment.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.microblogsentiment.tool.EmotionDictionaryTool;

public class EmotionDictionary {

	private Map<String, Double> poswords = new HashMap<String, Double>();

	public Map<String, Double> getPoswords() {
		return poswords;
	}

	public void setPoswords(Map<String, Double> poswords) {
		this.poswords = poswords;
	}

	public Map<String, Double> getNegwords() {
		return negwords;
	}

	public void setNegwords(Map<String, Double> negwords) {
		this.negwords = negwords;
	}

	public Map<String, Double> getLevelwords() {
		return levelwords;
	}

	public void setLevelwords(Map<String, Double> levelwords) {
		this.levelwords = levelwords;
	}

	public Map<String, Double> getViewwords() {
		return viewwords;
	}

	public void setViewwords(Map<String, Double> viewwords) {
		this.viewwords = viewwords;
	}

	private Map<String, Double> negwords = new HashMap<String, Double>();

	private Map<String, Double> levelwords = new HashMap<String, Double>();

	private Map<String, Double> viewwords = new HashMap<String, Double>();

	@Override
	public String toString() {
		return "EmotionDictionary [poswords=" + poswords + ", negwords="
				+ negwords + ", levelwords=" + levelwords + ", viewwords="
				+ viewwords + ", toString()=" + super.toString() + "]";
	}

	public void SetEmotionDictionary(String pospath1, String pospath2,
			String negpath1, String negpath2, String levelpath, String viewpath) {
		this.poswords.putAll(EmotionDictionaryTool
				.SetPosEmotionalWords(pospath1));
		this.poswords.putAll(EmotionDictionaryTool
				.SetPosEmotionalWords(pospath2));

		this.negwords.putAll(EmotionDictionaryTool
				.SetNegEmotionalWords(negpath1));
		this.negwords.putAll(EmotionDictionaryTool
				.SetNegEmotionalWords(negpath2));

		this.levelwords.putAll(EmotionDictionaryTool.SetLevelWords(levelpath));
		this.viewwords.putAll(EmotionDictionaryTool.SetViewWords(viewpath));

	}

	// ¥Ú”°∏–«È¥ µ‰
	public void PrintEmotionDictionary() {
		System.out.println("------Pos----------");

		if (!this.poswords.isEmpty()) {
			Iterator<Entry<String, Double>> iter = this.poswords.entrySet()
					.iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println(key + ":" + value);
			}
		}

		System.out.println("------Neg------------");

		if (!this.negwords.isEmpty()) {
			Iterator<Entry<String, Double>> iter = this.negwords.entrySet()
					.iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println(key + ":" + value);
			}
		}

		System.out.println("------Level---------");

		if (!this.levelwords.isEmpty()) {
			Iterator<Entry<String, Double>> iter = this.levelwords.entrySet()
					.iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println(key + ":" + value);
			}
		}

		System.out.println("------View-------");

		if (!this.viewwords.isEmpty()) {
			Iterator<Entry<String, Double>> iter = this.viewwords.entrySet()
					.iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println(key + ":" + value);
			}
		}
	}

	public void EmotionVectorlization(List<Microblog> microblogs) {
		for (Microblog m : microblogs) {
			List<EmotionWord> emotionwords = new ArrayList<EmotionWord>();
			for (EmotionWord w : m.getContentvector()) {
				if (this.poswords.containsKey(w.getContext())) {
					EmotionalWord em = new EmotionalWord();
					em.setContext(w.getContext());
					em.setEmotionpoint(this.poswords.get(w.getContext()));
					emotionwords.add(em);
					continue;
				}
				if (this.negwords.containsKey(w.getContext())) {
					EmotionalWord em = new EmotionalWord();
					em.setContext(w.getContext());
					em.setEmotionpoint(this.negwords.get(w.getContext()));
					emotionwords.add(em);
					continue;
				}

				if (this.levelwords.containsKey(w.getContext())) {
					LevelWord em = new LevelWord();
					em.setContext(w.getContext());
					em.setLevelpoint(this.levelwords.get(w.getContext()));
					emotionwords.add(em);
					continue;
				}
				if (this.viewwords.containsKey(w.getContext())) {
					ViewWord em = new ViewWord();
					em.setContext(w.getContext());
					em.setViewpoint(this.viewwords.get(w.getContext()));
					emotionwords.add(em);
					continue;
				}
			}
			m.setContentvector(emotionwords);
		}

	}

}
