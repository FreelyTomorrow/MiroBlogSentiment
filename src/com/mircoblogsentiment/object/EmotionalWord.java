package com.mircoblogsentiment.object;

public class EmotionalWord extends EmotionWord {

	private double emotionpoint;

	public double getEmotionpoint() {
		return emotionpoint;
	}

	public void setEmotionpoint(double emotionpoint) {
		this.emotionpoint = emotionpoint;
	}

	@Override
	public String toString() {
		return "EmotionalWord [emotionpoint=" + emotionpoint + ","
				+ super.toString() + "]";
	}

}
