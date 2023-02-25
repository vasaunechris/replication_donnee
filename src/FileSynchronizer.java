import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSynchronizer {

    public void synchronize(FileSystem fs1, FileSystem fs2) throws IOException {
        FileSystem refCopy1 = fs1.getReference();
        FileSystem refCopy2 = fs2.getReference();
        List<String> dirtyPaths1 = computeDirty(refCopy1, fs1, "");
        List<String> dirtyPaths2 = computeDirty(refCopy2, fs2, "");
        reconcile(fs1, dirtyPaths1, fs2, dirtyPaths2, "");
    }

    public void reconcile(FileSystem fs1, List<String> dirtyPaths1, FileSystem fs2, List<String> dirtyPaths2,
                          String currentRelativePath) throws IOException {

        for (String path : dirtyPaths1) {
            Path fullPath = Paths.get(currentRelativePath, path);
            if (dirtyPaths2.contains(path)) {
                if (fs1.isNewer(fullPath, fs2, fullPath)) {
                    fs2.replace(fullPath.toString(), fs1, fullPath.toString());
                } else {
                    fs1.replace(fullPath.toString(), fs2, fullPath.toString());
                }
                dirtyPaths2.remove(path);
            } else {
                fs1.replace(fullPath.toString(), fs2, fullPath.toString());
            }
        }

        for (String path : dirtyPaths2) {
            Path fullPath = Paths.get(currentRelativePath, path);
            fs2.replace(fullPath.toString(), fs1, fullPath.toString());
        }

        for (String subDir : fs1.getSubDirectories(currentRelativePath)) {
            String subPath = Paths.get(currentRelativePath, subDir).toString();
            List<String> subDirtyPaths1 = computeDirty(fs1.getReference(), fs1, subPath);
            List<String> subDirtyPaths2 = computeDirty(fs2.getReference(), fs2, subPath);
            reconcile(fs1, subDirtyPaths1, fs2, subDirtyPaths2, subPath);
        }
    }

    public List<String> computeDirty(FileSystem lastSync, FileSystem fs, String currentRelativePath) throws IOException {
        List<String> dirtyPaths = new ArrayList<>();

        for (String file : fs.getChildren(currentRelativePath)) {
            Path fullPath = Paths.get(currentRelativePath, file);
            if (!lastSync.isFileExists(fullPath) || lastSync.isNewer(fullPath, fs, fullPath)) {
                dirtyPaths.add(file);
            }
        }

        for (String subDir : fs.getSubDirectories(currentRelativePath)) {
            String subPath = Paths.get(currentRelativePath, subDir).toString();
            List<String> subDirtyPaths = computeDirty(lastSync, fs, subPath);
            if (!subDirtyPaths.isEmpty()) {
                dirtyPaths.add(subDir);
            }
        }

        return dirtyPaths;
    }
    public static void main(String args[]) {  
        Path path1 = Paths.get("C:\\Users\\Christian Vasaune\\Desktop\\replication\\replication_donnee\\test");
        Path path2 = Paths.get("C:\\Users\\Christian Vasaune\\Desktop\\replication\\replication_donnee\\test2");
        FileSystem fs1 = new LocalFileSystem(path1);
        FileSystem fs2 = new LocalFileSystem(path2);

        FileSynchronizer sync = new FileSynchronizer();
        try {
            sync.synchronize(fs1, fs2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
