package com.mircoblogsentiment.object;

public class EmotionWord {
	private String context;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "[context=" + context + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		EmotionWord other = (EmotionWord) obj;
		if (context.equals(other.context))
			return true;
		return false;
	}

}
