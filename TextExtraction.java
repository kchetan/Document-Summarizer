import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TextExtraction {

	private String fileURL;
	private String text;
	private OutputStream outputstream;
	private ParseContext context;
	private Detector detector;
	private Parser parser;
	private Metadata metadata;
	private ConsoleAppender console;
	private HashMap<String, String> metaHashMap;

	// Constructor class for TextExtraction using Tika Parser from a file of any
	// format
	public TextExtraction(String[] argv) {

		this.fileURL = argv[0];
		setSystemProxies();
		initExtractor();
		addAppender();
		extractRawContent();
		text = getText();
		dumpTextToIO();
	}

	// Add Appender to Log
	public void addAppender() {

		console = new ConsoleAppender(); // create appender
		// configure the appender
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.FATAL);
		console.activateOptions();
		// add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(console);

		FileAppender fa = new FileAppender();
		fa.setName("FileLogger");
		fa.setFile("mylog.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.DEBUG);
		fa.setAppend(true);
		fa.activateOptions();

		// add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(fa);
	}

	// Dumps extracted text to IO
	public void dumpTextToIO() {

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					new File("text.txt")));
			bufferedWriter.write(text);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Extract Raw Content from file
	public void extractRawContent() {

		try {
			URL url;
			File file = new File(fileURL);
			if (file.isFile()) {
				url = file.toURI().toURL();
			} else {
				url = new URL(fileURL);
			}
			InputStream input = TikaInputStream.get(url, metadata);
			ContentHandler handler = new BodyContentHandler(outputstream);
			parser.parse(input, handler, metadata, context);
			input.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		parseMetaData();
	}

	// Setter to get File URL
	public String getFileURL() {
		return fileURL;
	}

	// Returns the extracted text
	public String getText() {

		String extractedText = "";

		extractedText = outputstream.toString();
		extractedText = extractedText.replaceAll("\\n+", "\n");
		StringBuilder stringBuilder = new StringBuilder();
		int len = extractedText.length();
		for (int i = 0; i < len; i++) {

			if (i == 0)
				stringBuilder.append(extractedText.charAt(i));
			else {

				char curChar = extractedText.charAt(i);
				if (curChar == '\n' && extractedText.charAt(i - 1) == '\n') {

					continue;
				}
				if (curChar == ' ' && extractedText.charAt(i - 1) == ' ') {

					continue;
				}
				stringBuilder.append(extractedText.charAt(i));
			}
		}

		return new String(stringBuilder);
	}

	// Initializes Extractor or Tika Parser
	public void initExtractor() {

		context = new ParseContext();
		detector = new DefaultDetector();
		parser = new AutoDetectParser(detector);
		context.set(Parser.class, parser);
		outputstream = new ByteArrayOutputStream();
		metadata = new Metadata();
	}

	// Parses Meta Data of file
	public void parseMetaData() {

		String[] metadataNames = metadata.names();
		metaHashMap = new HashMap<String, String>();
		for (String name : metadataNames) {

			metaHashMap.put(name, metadata.get(name));
		}
		// printMetaData();

	}

	// Prints metaData of given file
	public void printMetaData() {

		System.out
				.println("=====================MetaData=========================");
		for (String key : metaHashMap.keySet()) {

			System.out.println(key + ": " + metaHashMap.get(key));
		}
		System.out
				.println("======================================================\n");
	}

	// Setter to set File URL
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	
	// Add System wide proxies
	public void setSystemProxies() {

		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("https.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("https.proxyPort", "8080");
	}

	// Driver program that extract raw text from a file and dumps it
	public static void main(String[] argv) {

		new TextExtraction(argv);
	}
}
