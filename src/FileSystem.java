import java.io.File;  // Import the File class
import java.util.*;
import java.lang.Exception;
import java.nio.file.*;

public interface FileSystem {

    public String getRoot();
    public String getParent();
    public List<String> getChildren();
    public List<String> getAncestor(String path);
    public String getAbsolutePath(String relativePath);
    public String getRelativePath(String absolutePath);
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS);
    public FileSystem getReference();
    public Path createDirectory(String path);
    public void fileCopy(File input, File output) throws Exception;
    public boolean isFileExists(Path filePath);

}
