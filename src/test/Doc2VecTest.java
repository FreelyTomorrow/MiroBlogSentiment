package test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.ansj.vec.Learn;
import com.ansj.vec.LearnDocVec;
import com.ansj.vec.Word2VEC;
import com.ansj.vec.domain.Neuron;
import com.ansj.vec.util.ReadWriteFile;

public class Doc2VecTest {

	public static void main(String[] args) throws IOException {

		File result = new File("file//microbloginput.txt");

		Learn learn = new Learn();

		// ѵ��������

		learn.learnFile(result);

		learn.saveModel(new File("model//microblg.mod"));

		Word2VEC w2v = new Word2VEC();

		w2v.loadJavaModel("model//microblg.mod");

		//System.out.println(w2v.distance("���"));

		// �õ�ѵ����Ĵ�������ѵ���ı�����

		Map<String, Neuron> word2vec_model = learn.getWord2VecModel();

		LearnDocVec learn_doc = new LearnDocVec(word2vec_model);

		learn_doc.learnFile(result);

		// �ı�����д�ļ�

		Map<Integer, float[]> doc_vec = learn_doc.getDocVector();

		StringBuilder sb = new StringBuilder("΢��ʹ��Dec2Vector�������Ľ��\n");

		for (int doc_no : doc_vec.keySet()) {

			StringBuilder doc = new StringBuilder("MicroBlog_" + doc_no + " ");

			float[] vector = doc_vec.get(doc_no);

			for (float e : vector) {

				doc.append(e + " ");
			}
			sb.append(doc.toString().trim() + "\n");

		}
		
		ReadWriteFile.writeFile("file//microBlog_doc_200_java.vec",
				sb.toString());

	}

}
