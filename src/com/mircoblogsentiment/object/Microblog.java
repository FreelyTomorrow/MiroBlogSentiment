package com.mircoblogsentiment.object;

import java.util.ArrayList;
import java.util.List;

public class Microblog {

	private String content;

	// ������
	private List<EmotionWord> contentvector = new ArrayList<EmotionWord>();

	public List<EmotionWord> getContentvector() {
		return contentvector;
	}

	public void setContentvector(List<EmotionWord> contentvector) {
		this.contentvector = contentvector;
	}

	// ����1
	private double featureone;

	// ����� -1 0δ��� 1����� 2����΢��
	private int label;

	public double getFeatureone() {
		return featureone;
	}

	public void setFeatureone(double featureone) {
		this.featureone = featureone;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	@Override
	public String toString() {
		String tempcontent = "";
		for (EmotionWord s : this.contentvector) {
			tempcontent += s.toString() + " ";
		}

		return "��ǩ:" + this.label + " " + tempcontent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
