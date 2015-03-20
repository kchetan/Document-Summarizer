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
		/*
		 * // Unirest.setProxy(new HttpHost("127.0.0.1", 8000));
		 * Unirest.setProxy(new HttpHost("proxy.iiit.ac.in",8080)); // Unirest.
		 * HttpResponse<JsonNode> response = Unirest.post(
		 * "https://textanalysis-text-summarization.p.mashape.com/text-summarizer"
		 * ) .header("X-Mashape-Key",
		 * "oBueQBMTQwmshVH8ZXZWybUr0BIhp1jpTVzjsnEj5QXY52Qvd7")
		 * .header("Content-Type", "application/json") .header("Accept",
		 * "application/json") .body(
		 * "{'url':'http://en.wikipedia.org/wiki/Automatic_summarization','text':'','sentnum':8}"
		 * ) .asJson();
		 */

	}

}
