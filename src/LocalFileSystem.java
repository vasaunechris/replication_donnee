import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.lang.Exception;
import java.nio.file.Files;

public class LocalFileSystem implements FileSystem {



    public String getRoot(){ return ""; }
    public String getParent(String path){ return ""; }
    public List<String> getChildren(String path){ return List<String>; }
    public List<String> getAncestor(String path){ return List<String>; }
    public String getAbsolutePath(String relativePath){ return ""; }
    public String getRelativePath(String absolutePath){ return ""; }
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS){}
    public FileSystem getReference(){return new FileSystem();}
    public File createDirectory(String path){
        try{
            Path p = Paths.get(path);
            Files.createDirectories(p, null);
            
        } catch (IOException e) {
            
        }
        return new File("f");

    }
    public void fileCopy(File input, File output) throws Exception{
        InputStream ip = null;
        OutputStream op = null;
        try {
            ip = new FileInputStream(input);
            op = new FileOutputStream(output);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer)) > 0) {
                op.write(buffer, 0, length);
            }
        } catch (Exception e) {
            System.err.println("Failed to copy" + e.getMessage());
        } finally {
            ip.close();
            op.close();
        }
    }

}