import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends SearchTemplate{

/*	private String _dirName;
	private String _filePattern;
	private boolean _recurse;
	private Matcher _fileMatcher;
	private Matcher _searchMatcher;
	private int _totalMatches;*/
	
	public FileSearch(String dirName, String filePattern, String searchPattern, boolean recurse) {
		super(dirName, filePattern, recurse, Pattern.compile(filePattern).matcher(""), Pattern.compile(searchPattern).matcher(""));
		run();
	}

	@Override
	protected void printMatches() {
		System.out.println("");
		System.out.println("TOTAL MATCHES: " + _totalMatches);
	}

	/*private void searchDirectory(File dir) {

		if(dir.isDirectory()) {
			if (dir.canRead()) {

				for (File file : dir.listFiles()) {
					if (file.isFile()) {
						if (file.canRead()) {
							searchFile(file);
						} else {
							unreadableFile(file);
						}
					}
				}

				if (_recurse) {
					for (File file : dir.listFiles()) {
						if (file.isDirectory()) {
							searchDirectory(file);
						}
					}
				}
			}
			else {
				System.out.println("Directory " + dir + " is unreadable");
			}
		}
		else{
			System.out.println(dir + " is not a directory");
		}
	}*/
	
	/*private void searchFile(File file) {
		String fileName = "";

		try {
			fileName = file.getCanonicalPath();
		}
		catch (IOException e) {
		}
		
		_fileMatcher.reset(fileName);
		if (_fileMatcher.find()) {
			try {
				int curMatches = 0;

				InputStream data = new BufferedInputStream(new FileInputStream(file));
				try {
					Scanner input = new Scanner(data);
					while (input.hasNextLine()) {
						String line = input.nextLine();

						_searchMatcher.reset(line);
						if (_searchMatcher.find()) {
							if (++curMatches == 1) {
								System.out.println("");
								System.out.println("FILE: " + file);
							}

							System.out.println(line);
							++_totalMatches;
						}
					}
				}
				finally {
					data.close();

					if (curMatches > 0) {
						System.out.println("MATCHES: " + curMatches);
					}
				}
			}
			catch (IOException e) {
				unreadableFile(file);
			}
		}
	}*/

	@Override
	protected void getMatches(File file) throws IOException {
		int curMatches = 0;

		InputStream data = new BufferedInputStream(new FileInputStream(file));
		try {
			Scanner input = new Scanner(data);
			while (input.hasNextLine()) {
				String line = input.nextLine();

				_searchMatcher.reset(line);
				if (_searchMatcher.find()) {
					if (++curMatches == 1) {
						System.out.println("");
						System.out.println("FILE: " + file);
					}

					System.out.println(line);
					++_totalMatches;
				}
			}
		}
		finally {
			data.close();

			if (curMatches > 0) {
				System.out.println("MATCHES: " + curMatches);
			}
		}
	}

	
	public static void main(String[] args) {
		
		String dirName = "";
		String filePattern = "";
		String searchPattern = "";
		boolean recurse = false;
		
		if (args.length == 3) {
			recurse = false;
			dirName = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			dirName = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		}
		else {
			usage();
			return;
		}
		
		new FileSearch(dirName, filePattern, searchPattern, recurse);
	}
	
	private static void usage() {
		System.out.println("USAGE: java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}

}
