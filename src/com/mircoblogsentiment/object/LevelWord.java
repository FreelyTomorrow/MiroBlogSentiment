package com.mircoblogsentiment.object;

public class LevelWord extends EmotionWord {

	private double levelpoint;

	public double getLevelpoint() {
		return levelpoint;
	}

	public void setLevelpoint(double levelpoint) {
		this.levelpoint = levelpoint;
	}

	@Override
	public String toString() {
		return "LevelWord [levelpoint=" + levelpoint + "," + super.toString()
				+ "]";
	}

}
