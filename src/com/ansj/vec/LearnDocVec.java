package com.ansj.vec;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import love.cq.util.MapCount;

import com.ansj.vec.domain.HiddenNeuron;
import com.ansj.vec.domain.Neuron;
import com.ansj.vec.domain.WordNeuron;
import com.ansj.vec.util.Haffman;

/**
 * @author Dell
 *
 */

public class LearnDocVec {

	private Map<String, Neuron> wordMap = new HashMap<>();

	/**
	 * 训练多少个特征
	 */
	private int layerSize = 200;

	/**
	 * 上下文窗口大小
	 */
	private int window = 5;

	private double sample = 1e-3;
	private double alpha = 0.025;
	private double startingAlpha = alpha;

	public int EXP_TABLE_SIZE = 1000;

	private Boolean isCbow = false;

	private double[] expTable = new double[EXP_TABLE_SIZE];

	private int trainWordsCount = 0;

	private int MAX_EXP = 6;

	// private int freqThresold = 5;
	// word2vec已过滤

	/**
	 * 文本向量
	 */
	Map<Integer, float[]> doc_vector = new HashMap<>();

	public LearnDocVec(Boolean isCbow, Integer layerSize, Integer window,
			Double alpha, Double sample, Map<String, Neuron> wordMap) {

		this.wordMap = wordMap;
		createExpTable();
		if (isCbow != null) {
			this.isCbow = isCbow;
		}
		if (layerSize != null)
			this.layerSize = layerSize;
		if (window != null)
			this.window = window;
		if (alpha != null)
			this.alpha = alpha;
		if (sample != null)
			this.sample = sample;
	}

	public LearnDocVec(Map<String, Neuron> wordMap) throws IOException {

		this.wordMap = wordMap;
		createExpTable();

	}

	/**
	 * trainModel
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void trainModel(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			String temp = null;
			long nextRandom = 5;
			int wordCount = 0;
			int lastWordCount = 0;
			int wordCountActual = 0;

			int sent_no = 0;

			while ((temp = br.readLine()) != null) {
				if (wordCount - lastWordCount > 10000) {
					System.out.println("alpha:"
							+ alpha
							+ "\tProgress: "
							+ (int) (wordCountActual
									/ (double) (trainWordsCount + 1) * 100)
							+ "%");
					wordCountActual += wordCount - lastWordCount;
					lastWordCount = wordCount;
					alpha = startingAlpha
							* (1 - wordCountActual
									/ (double) (trainWordsCount + 1));
					if (alpha < startingAlpha * 0.0001) {
						alpha = startingAlpha * 0.0001;
					}
				}
				String[] strs = temp.split(" ");
				wordCount += strs.length;
				List<WordNeuron> sentence = new ArrayList<WordNeuron>();
				for (int i = 0; i < strs.length; i++) {
					Neuron entry = wordMap.get(strs[i]);
					if (entry == null) {
						continue;
					}
					// The subsampling randomly discards frequent words while
					// keeping the ranking same
					if (sample > 0) {
						double ran = (Math.sqrt(entry.freq
								/ (sample * trainWordsCount)) + 1)
								* (sample * trainWordsCount) / entry.freq;
						nextRandom = nextRandom * 25214903917L + 11;
						if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
							continue;
						}
					}
					sentence.add((WordNeuron) entry);
				}

				for (int index = 0; index < sentence.size(); index++) {
					nextRandom = nextRandom * 25214903917L + 11;
					if (isCbow) {
						cbowGram(index, sent_no, sentence, (int) nextRandom
								% window);
					} else {
						skipGram(index, sent_no, sentence, (int) nextRandom
								% window);
					}
				}

				sent_no++;

			}

			br.close();

			System.out.println("Vocab size: " + wordMap.size());
			System.out.println("Words in train file: " + trainWordsCount);
			System.out.println("sucess train over!");
		}
	}

	/**
	 * skip gram 模型训练
	 * 
	 * @param sentence
	 * @param neu1
	 */
	private void skipGram(int index, int sent_no, List<WordNeuron> sentence,
			int b) {
		// TODO Auto-generated method stub
		WordNeuron word = sentence.get(index);
		int a, c = 0;
		for (a = b; a < window * 2 + 1 - b; a++) {
			if (a == window) {
				continue;
			}
			c = index - window + a;
			if (c < 0 || c >= sentence.size()) {
				continue;
			}

			double[] neu1e = new double[layerSize];// 误差项
			// HIERARCHICAL SOFTMAX
			List<Neuron> neurons = word.neurons;
			// WordNeuron we = sentence.get(c);
			// 不是中间词向量，而是文本向量

			float[] doc_vec = doc_vector.get(sent_no);

			for (int i = 0; i < neurons.size(); i++) {
				HiddenNeuron out = (HiddenNeuron) neurons.get(i);
				double f = 0;
				// Propagate hidden -> output
				for (int j = 0; j < layerSize; j++) {
					// f += we.syn0[j] * out.syn1[j];
					f += doc_vec[j] * out.syn1[j];
				}
				if (f <= -MAX_EXP || f >= MAX_EXP) {
					continue;
				} else {
					f = (f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2);
					f = expTable[(int) f];
				}
				// 'g' is the gradient multiplied by the learning rate
				double g = (1 - word.codeArr[i] - f) * alpha;
				// Propagate errors output -> hidden
				for (c = 0; c < layerSize; c++) {
					neu1e[c] += g * out.syn1[c];
				}
				// Learn weights hidden -> output
				// for (c = 0; c < layerSize; c++) {
				// out.syn1[c] += g * we.syn0[c];
				//
				// }
				// 不改变预测的中间词的向量
			}

			// Learn weights input -> hidden
			for (int j = 0; j < layerSize; j++) {
				// we.syn0[j] += neu1e[j];

				doc_vec[j] += neu1e[j];
				// 更新句子（文本）向量，不更新词向量
			}
		}

	}

	/**
	 * 词袋模型
	 * 
	 * @param index
	 * @param sentence
	 * @param b
	 */
	private void cbowGram(int index, int sent_no, List<WordNeuron> sentence,
			int b) {
		WordNeuron word = sentence.get(index);
		int a, c = 0;

		float[] doc_vec = doc_vector.get(sent_no);

		List<Neuron> neurons = word.neurons;
		double[] neu1e = new double[layerSize];// 误差项
		double[] neu1 = new double[layerSize];// 误差项
		WordNeuron last_word;

		for (a = b; a < window * 2 + 1 - b; a++) {

			if (a != window) {
				c = index - window + a;
				if (c < 0)
					continue;
				if (c >= sentence.size())
					continue;
				last_word = sentence.get(c);
				if (last_word == null)
					continue;
				for (c = 0; c < layerSize; c++)
					neu1[c] += last_word.syn0[c];
			}

			for (c = 0; c < layerSize; c++)
				neu1[c] += doc_vec[c];
			// 将文本的向量也作为输入

		}

		// HIERARCHICAL SOFTMAX
		for (int d = 0; d < neurons.size(); d++) {
			HiddenNeuron out = (HiddenNeuron) neurons.get(d);
			double f = 0;
			// Propagate hidden -> output
			for (c = 0; c < layerSize; c++)
				f += neu1[c] * out.syn1[c];
			if (f <= -MAX_EXP)
				continue;
			else if (f >= MAX_EXP)
				continue;
			else
				f = expTable[(int) ((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
			// 'g' is the gradient multiplied by the learning rate
			// double g = (1 - word.codeArr[d] - f) * alpha;
			// double g = f*(1-f)*( word.codeArr[i] - f) * alpha;
			double g = f * (1 - f) * (word.codeArr[d] - f) * alpha;
			//
			for (c = 0; c < layerSize; c++) {
				neu1e[c] += g * out.syn1[c];
			}
			// Learn weights hidden -> output
			// for (c = 0; c < layerSize; c++) {
			// out.syn1[c] += g * neu1[c];
			// }
			// 不改变预测的中间词的向量

		}
		for (a = b; a < window * 2 + 1 - b; a++) {
			if (a != window) {
				c = index - window + a;
				if (c < 0)
					continue;
				if (c >= sentence.size())
					continue;
				last_word = sentence.get(c);
				if (last_word == null)
					continue;
				for (c = 0; c < layerSize; c++) {

					// last_word.syn0[c] += neu1e[c];
					doc_vec[c] += neu1e[c];
					// 更新句子（文本）向量，不更新词向量

				}

			}

		}
	}

	/**
	 * Precompute the exp() table f(x) = x / (x + 1)
	 */
	private void createExpTable() {
		for (int i = 0; i < EXP_TABLE_SIZE; i++) {
			expTable[i] = Math
					.exp(((i / (double) EXP_TABLE_SIZE * 2 - 1) * MAX_EXP));
			expTable[i] = expTable[i] / (expTable[i] + 1);
		}
	}

	/**
	 * 初始化文本向量
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void InitializeDocVec(File file) throws IOException {
		MapCount<String> mc = new MapCount<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			String temp = null;

			int sent_no = 0;
			while ((temp = br.readLine()) != null) {
				String[] split = temp.split(" ");
				trainWordsCount += split.length;
				for (String string : split) {
					mc.add(string);
				}
				float[] vector = new float[layerSize];

				Random random = new Random();

				for (int i = 0; i < vector.length; i++)
					vector[i] = (float) ((random.nextDouble() - 0.5) / layerSize);

				doc_vector.put(sent_no, vector);

				sent_no++;
			}

			br.close();
		}

	}

	/**
	 * 根据文件学习
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void learnFile(File file) throws IOException {

		InitializeDocVec(file);
		new Haffman(layerSize).make(wordMap.values());

		// 查找每个神经元
		for (Neuron neuron : wordMap.values()) {
			((WordNeuron) neuron).makeNeurons();
		}

		trainModel(file);
	}

	/**
	 * 保存模型
	 */
	public void saveModel(File file) {
		// TODO Auto-generated method stub

		try (DataOutputStream dataOutputStream = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(file)))) {
			dataOutputStream.writeInt(wordMap.size());
			dataOutputStream.writeInt(layerSize);
			double[] syn0 = null;
			for (Entry<String, Neuron> element : wordMap.entrySet()) {
				dataOutputStream.writeUTF(element.getKey());
				syn0 = ((WordNeuron) element.getValue()).syn0;
				for (double d : syn0) {
					dataOutputStream.writeFloat(((Double) d).floatValue());
				}
			}
			dataOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<Integer, float[]> getDocVector() {

		return doc_vector;
	}

	public int getLayerSize() {
		return layerSize;
	}

	public void setLayerSize(int layerSize) {
		this.layerSize = layerSize;
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public double getSample() {
		return sample;
	}

	public void setSample(double sample) {
		this.sample = sample;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
		this.startingAlpha = alpha;
	}

	public Boolean getIsCbow() {
		return isCbow;
	}

	public void setIsCbow(Boolean isCbow) {
		this.isCbow = isCbow;
	}

}
