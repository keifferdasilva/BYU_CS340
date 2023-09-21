import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;

public abstract class SearchTemplate {

    protected String _directory;

    protected String _filePattern;

    protected boolean _recurse;

    protected Matcher _fileMatcher;

    protected Matcher _searchMatcher;

    protected int _totalMatches;

    protected int _totalLines;

    public SearchTemplate(String directory, String filePattern, boolean recurse, Matcher fileMatcher){
        _filePattern = filePattern;
        _recurse = recurse;
        _fileMatcher = fileMatcher;
        _directory = directory;
    }

    public SearchTemplate(String directory, String filePattern, boolean recurse, Matcher fileMatcher, Matcher searchMatcher){
        _filePattern = filePattern;
        _recurse = recurse;
        _fileMatcher = fileMatcher;
        _directory = directory;
        _searchMatcher = searchMatcher;
    }

    protected void searchDirectory(File directory){
        if(directory.isDirectory()) {
            if (directory.canRead()) {

                for (File file : directory.listFiles()) {
                    if (file.isFile()) {
                        if (file.canRead()) {
                            searchFile(file);
                        } else {
                            System.out.println("File " + file + " is unreadable");
                        }
                    }
                }

                if (_recurse) {
                    for (File file : directory.listFiles()) {
                        if (file.isDirectory()) {
                            searchDirectory(file);
                        }
                    }
                }
            }
            else {
                System.out.println("Directory " + directory + " is unreadable");
            }
        }
        else{
            System.out.println(directory + " is not a directory");
        }
    }

    protected void searchFile(File file){
        String fileName = "";

        try {
            fileName = file.getCanonicalPath();
        }
        catch (IOException e) {
        }
        _fileMatcher.reset(fileName);
        if (_fileMatcher.find()) {
            try {
                getMatches(file);
            }
            catch (IOException e) {
                System.out.println("File " + file + " is unreadable");
            }
        }
    }

    protected abstract void getMatches(File file) throws IOException;

    protected void run(){
        searchDirectory(new File(_directory));
        printMatches();
    }

    protected abstract void printMatches();
}
