import java.io.*;  // Import the File class
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Exception;
import java.nio.file.*;
import java.time.Instant;

public class LocalFileSystem implements FileSystem {

    Path path;

    public LocalFileSystem(Path path){
        this.path = path;
    }

    public Path getRoot(){ 
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
        File file = new File(this.path.toString());
        return file.getParentFile().getName();
    }
    public List<String> getChildren(String path){
       
        return Stream.of(new File(this.getRoot().resolve(path).toString()).listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .collect(Collectors.toList());
        
    }

    public String[] getFiles(String relativePath) throws IOException {
        Path dirPath = this.getRoot().resolve(relativePath);
        List<String> files = new ArrayList<>();

        if (Files.isDirectory(dirPath)) {
            Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        Path fileRelativePath = this.getRoot().relativize(filePath);
                        files.add(fileRelativePath.toString());
                    });
        }

        return files.toArray(new String[0]);
    }
    public List<String> getAncestor(String path){ 
        List<String> ancestor = Arrays.asList(this.getRoot().toString().split(Pattern.quote("\\")));
        return ancestor.subList(0, ancestor.size()-1); 
    }
    public String getAbsolutePath(String relativePath){ return this.getRoot() + relativePath; }

    public String getRelativePath(String absolutePath){ return absolutePath.split("\\" + this.getParent())[0]; }

    public void replace(String absolutePathTargetFS, FileSystem fsSource, String absolutePathSourceFS){
        Path sourcePath = this.getRoot().resolve(absolutePathSourceFS);
        Path targetPath = fsSource.getRoot().resolve(absolutePathTargetFS);
        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public FileSystem getReference(){return this;}

    public Path createDirectory(String path){
        try{
            Path p = Paths.get(path);
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
        Instant modifiedTime = Files.getLastModifiedTime(this.getRoot().resolve(filePath)).toInstant();
        Instant otherModifiedTime = Files.getLastModifiedTime(otherFs.getRoot().resolve(otherFilePath)).toInstant();
        return !modifiedTime.isAfter(otherModifiedTime);
    }
    
    public boolean isFileExists(Path filePath) {
        return Files.exists(this.getRoot().resolve(filePath));
    }

    public String[] getSubDirectories(String relativePath) throws IOException {
        Path dirPath = this.getRoot().resolve(relativePath);
        List<String> subDirs = new ArrayList<>();

        if (Files.isDirectory(dirPath)) {
            Files.list(dirPath)
                    .filter(Files::isDirectory)
                    .forEach(subDirPath -> {
                        Path subDirRelativePath = this.getRoot().relativize(subDirPath);
                        subDirs.add(subDirRelativePath.toString());
                    });
        }

        return subDirs.toArray(new String[0]);
    }

    public static void main(String args[]) {  
        

    }

}
