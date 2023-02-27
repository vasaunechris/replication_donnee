import java.io.File;  // Import the File class
import java.io.IOException;
import java.util.*;
import java.lang.Exception;
import java.nio.file.*;

public interface FileSystem {

    public Path getRoot();
    public String getParent();
    public List<String> getChildren(String path);
    public List<String> getAncestor(String path);
    public String getAbsolutePath(String relativePath);
    public String getRelativePath(String absolutePath);
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS);
    public FileSystem getReference();
    public Path createDirectory(String path);
    public void fileCopy(File input, File output) throws Exception;
    public boolean isFileExists(Path filePath);
    public boolean isNewer(Path fullPath, FileSystem fs2, Path fullPath2) throws IOException;
    public String[] getSubDirectories(String relativePath) throws IOException;
    public String[] getFiles(String relativePath) throws IOException;

}
