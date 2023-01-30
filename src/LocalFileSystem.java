import java.io.File;  // Import the File class
import java.util.*;
import java.lang.Exception;

public class LocalFileSystem implements FileSystem {

    public String getRoot(){ return ""; }
    public String getParent(String path){ return ""; }
    public List<String> getChildren(String path){ return List<String>; }
    public List<String> getAncestor(String path){ return List<String>; }
    public String getAbsolutePath(String relativePath){ return ""; }
    public String getRelativePath(String absolutePath){ return ""; }
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS){}
    public FileSystem getReference(){return new FileSystem();}
    public File createDirectory(String path){return new File(); }
    public void fileCopy(File input, File output) throws Exception{}

}