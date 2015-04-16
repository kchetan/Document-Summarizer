import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private String summarizerURL;
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
		this.summarizerURL = argv[1];
		setSystemProxies();
		initExtractor();
		addAppender();
		extractRawContent();
		text = getText();
		dumpTextToIO();
		deleteExtraFile();
		runSystemCommands();
		deleteExtraFiles();
	}

	public void execCommand(String str, int type) {

		String arr[] = new String[10];
		arr = str.split(" ");

		try {
			ProcessBuilder builder = new ProcessBuilder(arr);
			if (type == 0) {
				builder.redirectOutput(new File("summary.txt"));
				builder.redirectError(new File("error1.txt"));
			} else {
				builder.redirectOutput(new File("output.txt"));
				builder.redirectError(new File("error2.txt"));
				// builder.redirectOutput(Redirect.INHERIT);
			}
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void runSystemCommands() {

		String str = "";
		str += "python ";
		str += summarizerURL;
		str += " text.txt";
		execCommand(str, 0);
		str = "";
		String OUTPUT_FILE_NAME = "model.bin";
		try {
			InputStream in = getClass().getResourceAsStream(
					"summarysimilarity.py");
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					new File("summarysimilarity.py")));
			String line;
			while ((line = input.readLine()) != null) {

				bufferedWriter.write(line + "\n");
			}
			bufferedWriter.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BytesStreamsAndFiles bStreamsAndFiles = new BytesStreamsAndFiles();
		byte[] fileContents = bStreamsAndFiles.read();
		bStreamsAndFiles.write(fileContents, OUTPUT_FILE_NAME);

		str += "python ";
		str += "summarysimilarity.py ";

		str += "model.bin ";
		str += "summary.txt";
		execCommand(str, 1);

	}

	public String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);

			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				
//				output.
				output.append(line + "\n");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	public void deleteExtraFile() {
		File file1 = new File("mylog.log");
		if(file1.exists()){
			file1.delete();
		}
		
	}
	public void deleteExtraFiles() {

		try {
			File file1 = new File("mylog.log");
			File file2 = new File("text.txt");
			File file3 = new File("error1.txt");
//			File file4 = new File("error2.txt");
			File file5 = new File("model.bin");
			// File file6=new File("summary.txt");
			File file7 = new File("text.txt");
			File file8 = new File("summarysimilarity.py");
			if (file1.exists()) {
				file1.delete();
			}
			if (file2.exists()) {
				file2.delete();
			}
			if (file3.exists()) {
				file3.delete();
			}
			/*
			if (file4.exists()) {
				file4.delete();
			}
			*/
			if (file5.exists()) {
				file5.delete();
			}
			/*
			 * if(file6.exists()){ file6.delete(); }
			 */
			if (file7.exists()) {
				file7.delete();
			}
			if (file8.exists()) {
				file8.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
