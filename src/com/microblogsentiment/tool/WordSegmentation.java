package com.microblogsentiment.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import com.mircoblogsentiment.object.EmotionWord;
import com.mircoblogsentiment.object.EmotionalWord;
import com.mircoblogsentiment.object.LevelWord;
import com.mircoblogsentiment.object.Microblog;
import com.mircoblogsentiment.object.ViewWord;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class WordSegmentation {

	// 微博分词
	private static List<EmotionWord> SentimentMicroLog(String microblog) {
		List<EmotionWord> emotionwords = new ArrayList<EmotionWord>();
		Analyzer analyzer = new PaodingAnalyzer();
		String docText = microblog;
		TokenStream tokenStream = analyzer.tokenStream(docText,
				new StringReader(docText));

		try {
			Token t;
			while ((t = tokenStream.next()) != null) {
				EmotionWord m = new EmotionWord();
				m.setContext(t.termText());
				emotionwords.add(m);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return emotionwords;
	}

	// /读取正微博内容
	public static List<Microblog> readPosMicroblogs(String path) {
		File file = new File(path);
		List<Microblog> microblogs = new ArrayList<Microblog>();
		String text = null;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			// StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.charAt(0) != '<') {
					Microblog m = new Microblog();
					m.setContent(text);
					m.setContentvector(SentimentMicroLog(m.getContent()));
					m.setLabel(1);
					microblogs.add(m);
				}

			}
			br1.close();
			// text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return microblogs;
	}

	// /读取负微博内容
	public static List<Microblog> readNegMicroblogs(String path) {
		File file = new File(path);
		List<Microblog> microblogs = new ArrayList<Microblog>();
		String text = null;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			// StringBuffer buff1 = new StringBuffer();
			while ((text = br1.readLine()) != null) {
				if (text.charAt(0) != '<') {
					Microblog m = new Microblog();
					m.setContent(text);
					m.setContentvector(SentimentMicroLog(m.getContent()));
					m.setLabel(-1);
					microblogs.add(m);
				}

			}
			br1.close();
			// text = buff1.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return microblogs;
	}

	// /读取未标记微博内容
	public static List<Microblog> readUnLabeledMicroblogs(String path) {
		File file = new File(path);
		List<Microblog> microblogs = new ArrayList<Microblog>();
		String text = null;
		try {
			InputStreamReader read1 = new InputStreamReader(
					new FileInputStream(file), "GBK");
			BufferedReader br1 = new BufferedReader(read1);
			while ((text = br1.readLine()) != null) {
				text=text.replaceAll("<", " ");
				text=text.replaceAll(">", " ");
				//System.out.println(text);
				String[] content=text.split(" ");
				Microblog m = new Microblog();
				m.setContent(content[2]);
				m.setContentvector(SentimentMicroLog(m.getContent()));
				m.setLabel(0);
				microblogs.add(m);
			}
			br1.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return microblogs;
	}
	
	// /读取未标记微博内容
		public static List<Microblog> readNoiseMicroblogs(String path) {
			File file = new File(path);
			List<Microblog> microblogs = new ArrayList<Microblog>();
			String text = null;
			try {
				InputStreamReader read1 = new InputStreamReader(
						new FileInputStream(file), "GBK");
				BufferedReader br1 = new BufferedReader(read1);
				while ((text = br1.readLine()) != null) {
					text=text.replaceAll("<", " ");
					text=text.replaceAll(">", " ");
					//System.out.println(text);
					String[] content=text.split(" ");
					Microblog m = new Microblog();
					m.setContent(content[2]);
					m.setContentvector(SentimentMicroLog(m.getContent()));
					m.setLabel(2);
					microblogs.add(m);
				}
				br1.close();
			} catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
			return microblogs;
		}

	public static String getMatchText(String compile, String content) {
		Pattern p = Pattern.compile(compile);
		Matcher m = p.matcher(content);
		return m.group(0);
	}
	
	public static void WriteVectorToText(List<Microblog> MicroVectors,String path)
	{
		try{
		     
			File file =new File(path);
		     
		     //if file doesnt exists, then create it
		     if(!file.exists()){
		       file.createNewFile();
		      }
		     System.out.println(file.getName());
		     FileWriter fileWritter = new FileWriter(file.getPath(),false);
		    
		     
		     for (Microblog w : MicroVectors) {
		    	 //System.out.println(w.getContent());	
		    	 fileWritter.write(w.getContent()+"\n");
		    	 fileWritter.flush();
			 }
		     fileWritter.close();
		     System.out.println("Done");

		  }catch(IOException e){
		      e.printStackTrace();
		 }
	}
	
	public static void TurnVectorToSentimentFeature(List<Microblog> MicroVectors,String path)
	{
		try{
		     
			File file =new File(path);
		     
		     //if file doesnt exists, then create it
		     if(!file.exists()){
		       file.createNewFile();
		      }
		     System.out.println(file.getName());
		     FileWriter fileWritter = new FileWriter(file.getPath(),false);
		     
		     for (Microblog w : MicroVectors) {
		    	 //System.out.println(w.getContent());
		    	 List<EmotionWord> list=w.getContentvector();
		    	 StringBuilder temp=new StringBuilder();
		    	 for(int i=0;i<list.size();i++)
		    	 {
		    		 try
		    		 {
		    			 ViewWord vword=(ViewWord)list.get(i);
		    			 temp.append(vword.getViewpoint()+" ");
		    		 }
		    		 catch(Exception ex)
		    		 {
		    			 
		    		 }
		    		 try
		    		 {
		    			 EmotionalWord vword=(EmotionalWord)list.get(i);
		    			 temp.append(vword.getEmotionpoint()+" ");
		    		 }
		    		 catch(Exception ex)
		    		 {
		    			 
		    		 }
		    		 try
		    		 {
		    			 LevelWord vword=(LevelWord)list.get(i);
		    			 temp.append(vword.getLevelpoint()+" ");
		    		 }
		    		 catch(Exception ex)
		    		 {
		    			 
		    		 }
		    	 }
		    	 fileWritter.write(temp.append("\n").toString());
		    	 fileWritter.flush();
			 }
		     fileWritter.close();
		     System.out.println("Done");

		  }catch(IOException e){
		      e.printStackTrace();
		 }
	}
}
