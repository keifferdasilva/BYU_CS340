import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LineCount extends SearchTemplate {
	private String _directory;
	private String _pattern;
	private boolean _recurse;
	private int _totalLineCount;
	private Matcher _fileMatcher;
	
	public LineCount(String directory, String pattern, boolean recurse) {
		super(directory, pattern, recurse, Pattern.compile(pattern).matcher(""));
		_directory = directory;
		_pattern = pattern;
		_recurse = recurse;
		_totalLines = 0;
		_fileMatcher = Pattern.compile(_pattern).matcher("");
		run();
	}

	@Override
	protected void getMatches(File file) throws IOException {
		Reader reader = new BufferedReader(new FileReader(file));
		int curLineCount = 0;
		try {
			curLineCount = 0;
			Scanner input = new Scanner(reader);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				++curLineCount;
				++_totalLines;
			}
		}
		finally {
			System.out.println(curLineCount + "  " + file);
			reader.close();
		}
	}

	@Override
	protected void printMatches() {
		System.out.println("TOTAL: " + _totalLines);
	}


	
	public static void main(String[] args) {
		String directory = "";
		String pattern = "";
		boolean recurse = false;
		
		if (args.length == 2) {
			recurse = false;
			directory = args[0];
			pattern = args[1];
		}
		else if (args.length == 3 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			pattern = args[2];
		}
		else {
			usage();
			return;
		}
		
		new LineCount(directory, pattern, recurse);
	}
	
	private static void usage() {
		System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
	}

}
