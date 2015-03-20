import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class TikaExtraction {
	public static void main(final String[] args) throws IOException,
			SAXException, TikaException, UnirestException {

		File file = new File(
				"C:/Users/SAMEER/Desktop/Game-Skill-Improvements-using-Raspberry-PI.docx");
		Tika tika = new Tika();
		String filecontent = tika.parseToString(file);
		System.out.println("Extracted Content: " + filecontent);
	}

}
