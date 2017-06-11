package com.microblogsentiment;

import java.util.ArrayList;
import java.util.List;

import com.microblogsentiment.tool.WordSegmentation;
import com.mircoblogsentiment.object.EmotionDictionary;
import com.mircoblogsentiment.object.Microblog;

public class SentimentText {

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������				
		// ��ȡ�б�ǩ��΢��
		List<Microblog> labeledmicroblogs = new ArrayList<Microblog>();
		labeledmicroblogs.addAll(WordSegmentation
				.readPosMicroblogs("corpus/task3&4_pos.txt"));
		System.out.println("------"+labeledmicroblogs.size()+"--------");
		labeledmicroblogs.addAll(WordSegmentation
				.readNegMicroblogs("corpus/task3&4_neg.txt"));
		System.out.println("------"+labeledmicroblogs.size()+"--------");
		
		// ��ȡ�ޱ�ǩ��΢��
		List<Microblog> unlabeledmicroblogs = new ArrayList<Microblog>();
		unlabeledmicroblogs.addAll(WordSegmentation
				.readUnLabeledMicroblogs("corpus/COAE2014-Data.txt"));
		
		//��ȡ���ű�ǩ΢��
		List<Microblog> noisemicroblogs = new ArrayList<Microblog>();
		noisemicroblogs.addAll(WordSegmentation
				.readNoiseMicroblogs("corpus/noise.txt"));
		
		System.out.println(labeledmicroblogs.size()+" "+unlabeledmicroblogs.size()
				+" "+noisemicroblogs.size());
		
		// ����ʵ�Ľ���
		EmotionDictionary dictionary = new EmotionDictionary();
		dictionary.SetEmotionDictionary("sentimentdic/posj-cn.txt",
				"sentimentdic/poss-cn.txt", "sentimentdic/negj-cn.txt",
				"sentimentdic/negs-cn.txt", "sentimentdic/level-cn.txt",
				"sentimentdic/view-cn.txt");
		// ��ӡ����ʵ�
		//dictionary.PrintEmotionDictionary();

		// ���ø���ʵ�����������������vector��ÿ���ʶ��з���
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
		
		//��������ΪDoc2Vec�����뱣��(��������д��һ���ļ�)
		List<Microblog> inputmicroblogs=new ArrayList<Microblog>();
		inputmicroblogs.addAll(labeledmicroblogs);
		inputmicroblogs.addAll(unlabeledmicroblogs);
		inputmicroblogs.addAll(noisemicroblogs);
		WordSegmentation.WriteVectorToText(inputmicroblogs,"file/microbloginput.txt");
		
		System.out.println(labeledmicroblogs.size()+" "+unlabeledmicroblogs.size()
				+" "+noisemicroblogs.size());
		
		//��ȡ���ڴʵ���������������(�������������ļ���֪�����������ж���΢�������΢��ǰ1003����������)
		WordSegmentation.TurnVectorToSentimentFeature(labeledmicroblogs, "file/sentimentfeatures_an.txt");
		WordSegmentation.TurnVectorToSentimentFeature(unlabeledmicroblogs, "file/sentimentfeatures_un.txt");
		WordSegmentation.TurnVectorToSentimentFeature(noisemicroblogs, "file/sentimentfeatures_noise.txt");
	}

}
