import java.io.*;  // Import the File class
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Exception;
import java.nio.file.*;

public class LocalFileSystem implements FileSystem {

    String path;

    public LocalFileSystem(String path){
        this.path = path;
    }

    public String getRoot(){ 
        /*File currentDir = new File(".");
        try {
            return currentDir.getCanonicalPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
        */
        return this.path;
    }
    public String getParent(){ 
        File file = new File(this.path);
        return file.getParentFile().getName();
    }
    public List<String> getChildren(){   
        return Stream.of(new File(this.path).listFiles())
            //.filter(file -> !file.isDirectory())
            .map(File::getName)
            .collect(Collectors.toList());
    }
    public List<String> getAncestor(String path){ 
        List<String> ancestor = Arrays.asList(this.getRoot().split(Pattern.quote("\\")));
        return ancestor.subList(0, ancestor.size()-1); 
    }
    public String getAbsolutePath(String relativePath){ return ""; }
    public String getRelativePath(String absolutePath){ return ""; }
    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS){
        Path sourcePath = rootPath.resolve(filePath);
        Path targetPath = otherFs.rootPath.resolve(otherFilePath);
        Files.createDirectories(targetPath.getParent());
        Files.copy(sourcePath, targetPath);
    }
    
    public FileSystem getReference(){return this;}

    public Path createDirectory(String path){
        try{
            Path p = Paths.get(path);
            System.out.println();
            return Files.createDirectories(p);
        } catch (IOException e) {
            System.err.println("Failed to create" + e.getMessage());
        }
        return null;
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
            System.out.println("Copy is done");
        } catch (Exception e) {
            System.err.println("Failed to copy" + e.getMessage());
        } finally {
            ip.close();
            op.close();
        }
    }

    public boolean isNewer(Path filePath, FileSystem otherFs, Path otherFilePath) throws IOException {
        Instant modifiedTime = Files.getLastModifiedTime(rootPath.resolve(filePath)).toInstant();
        Instant otherModifiedTime = Files.getLastModifiedTime(otherFs.rootPath.resolve(otherFilePath)).toInstant();
        return modifiedTime.isAfter(otherModifiedTime);
    }
    
    public boolean isFileExists(Path filePath) {
        return Files.exists(rootPath.resolve(filePath));
    }

    public static void main(String args[]) {  
        FileSystem fs = new LocalFileSystem("C:\\Users\\Christian Vasaune\\Desktop\\replication\\replication_donnee");
        System.out.println(fs.getAncestor(fs.getParent()));
    }

}
