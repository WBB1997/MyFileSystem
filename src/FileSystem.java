import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSystem {
    private static final String filename = "Data.cache";
    private static File data = null;
//    private static File index = null;
    private static int pageSize = 2048;
    private static int pageNum = 4096;
    private static FileSystem ourInstance = new FileSystem();

    public static FileSystem getInstance() {
        return ourInstance;
    }

    private FileSystem() {
        data = new File(filename);
//        index = new File(filepath + "Index.cache");
    }

    public void add(byte[] b) throws IOException {
        if(b.length > pageSize)
            throw  new OutOfMemoryError("Data length out of pageSize");
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(data));
        dataInputStream.read()
    }

    public void delete(){

    }

    public void update(){

    }

    public void get(){

    }
}

