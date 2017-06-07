package com.mircoblogsentiment.object;

public class ViewWord extends EmotionWord {
	private double viewpoint;

	public double getViewpoint() {
		return viewpoint;
	}

	public void setViewpoint(double viewpoint) {
		this.viewpoint = viewpoint;
	}

	@Override
	public String toString() {
		return "ViewWord [viewpoint=" + viewpoint + "," + super.toString()
				+ "]";
	}

}
