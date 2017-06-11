package com.microblogsentiment;

import java.util.ArrayList;
import java.util.List;

import com.microblogsentiment.tool.WordSegmentation;
import com.mircoblogsentiment.object.EmotionDictionary;
import com.mircoblogsentiment.object.Microblog;

public class SentimentText {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根				
		// 读取有标签的微博
		List<Microblog> labeledmicroblogs = new ArrayList<Microblog>();
		labeledmicroblogs.addAll(WordSegmentation
				.readPosMicroblogs("corpus/task3&4_pos.txt"));
		System.out.println("------"+labeledmicroblogs.size()+"--------");
		labeledmicroblogs.addAll(WordSegmentation
				.readNegMicroblogs("corpus/task3&4_neg.txt"));
		System.out.println("------"+labeledmicroblogs.size()+"--------");
		
		// 读取无标签的微博
		List<Microblog> unlabeledmicroblogs = new ArrayList<Microblog>();
		unlabeledmicroblogs.addAll(WordSegmentation
				.readUnLabeledMicroblogs("corpus/COAE2014-Data.txt"));
		
		//读取干扰标签微博
		List<Microblog> noisemicroblogs = new ArrayList<Microblog>();
		noisemicroblogs.addAll(WordSegmentation
				.readNoiseMicroblogs("corpus/noise.txt"));
		
		System.out.println(labeledmicroblogs.size()+" "+unlabeledmicroblogs.size()
				+" "+noisemicroblogs.size());
		
		// 感情词典的建立
		EmotionDictionary dictionary = new EmotionDictionary();
		dictionary.SetEmotionDictionary("sentimentdic/posj-cn.txt",
				"sentimentdic/poss-cn.txt", "sentimentdic/negj-cn.txt",
				"sentimentdic/negs-cn.txt", "sentimentdic/level-cn.txt",
				"sentimentdic/view-cn.txt");
		// 打印感情词典
		//dictionary.PrintEmotionDictionary();

		// 利用感情词典向量化，向量化后vector中每个词都有分数
		dictionary.EmotionVectorlization(labeledmicroblogs);
		dictionary.EmotionVectorlization(unlabeledmicroblogs);
		dictionary.EmotionVectorlization(noisemicroblogs);

		/*
		System.out.println("------Labeled---------");

		for (Microblog w : labeledmicroblogs) {
			System.out.println(w);
		}
		System.out.println("------UnLabeled---------");

		for (Microblog w : unlabeledmicroblogs) {
			System.out.println(w);
		}**/
		
		//把内容作为Doc2Vec的输入保存(三个部分写到一个文件)
		List<Microblog> inputmicroblogs=new ArrayList<Microblog>();
		inputmicroblogs.addAll(labeledmicroblogs);
		inputmicroblogs.addAll(unlabeledmicroblogs);
		inputmicroblogs.addAll(noisemicroblogs);
		WordSegmentation.WriteVectorToText(inputmicroblogs,"file/microbloginput.txt");
		
		System.out.println(labeledmicroblogs.size()+" "+unlabeledmicroblogs.size()
				+" "+noisemicroblogs.size());
		
		//提取基于词典的情感特征并保存(三个部分三个文件，知道各个部分有多少微博，标记微博前1003条是正感情)
		WordSegmentation.TurnVectorToSentimentFeature(labeledmicroblogs, "file/sentimentfeatures_an.txt");
		WordSegmentation.TurnVectorToSentimentFeature(unlabeledmicroblogs, "file/sentimentfeatures_un.txt");
		WordSegmentation.TurnVectorToSentimentFeature(noisemicroblogs, "file/sentimentfeatures_noise.txt");
	}

}
