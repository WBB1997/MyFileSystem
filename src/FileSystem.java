import java.io.*;
import java.util.*;

public class FileSystem {
    private static final File datafile = new File("cache.bin");
    private static final File indexfile = new File(".index");
    private static HashMap<String, Value> index;
    private static TreeSet<Value> set;
    private static FileSystem ourInstance = new FileSystem();

    static FileSystem getInstance() {
        return ourInstance;
    }

    private FileSystem() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexfile));
            index = (HashMap<String, Value>) ois.readObject();
            set = new TreeSet<>(index.values());
            ois.close();
            remoDefrag();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            index = new HashMap<>();
            set = new TreeSet<>();
        }
    }

    public void add(String data, String key) throws IOException {
        byte[] b = data.getBytes();
        RandomAccessFile randomAccessFile = new RandomAccessFile(datafile, "rw");
        long start = randomAccessFile.length();
        long length = b.length;
        Value v =  new Value(key, start, length);
        if(!index.containsValue(v)) {
            // 建立索引
            index.put(key, v);
            // 添加数据
            randomAccessFile.seek(start);
            randomAccessFile.write(b);
            //储存
            store();
        }
        //关闭文件
        randomAccessFile.close();
    }

    public void delete(String key) throws IOException {
        Value v;
        if ((v = index.get(key)) != null) {
            index.remove(key);
            store();
        }
    }

    public void update(String data, String key) throws IOException {
        delete(key);
        add(data, key);
        store();
    }

    public String get(String key) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(datafile, "r");
        Value value = index.get(key);
        randomAccessFile.seek(value.getStart());
        byte[] b = new byte[(int) value.getLength()];
        randomAccessFile.read(b);
        randomAccessFile.close();
        return new String(b);
    }

    public Set<String> keySet() {
        return index.keySet();
    }

    public List<String> valueList() throws IOException {
        List<String> tmp = new ArrayList<>();
        for (String keyword : index.keySet())
            tmp.add(get(keyword));
        return tmp;
    }

    List<Value> getV(){
        return new ArrayList<>(index.values());
    }

    public void remoDefrag() throws IOException {
        Iterator<Value> setit = set.iterator();
        long size = 0;
        long prevEnd = 0;
        while (setit.hasNext()) {
            Value value = setit.next();
            long nowStart = value.getStart();
            long length = value.getLength();
            String key = value.getKey();
            move(prevEnd, nowStart, length);
            index.get(key).setStart(prevEnd);
            prevEnd += length;
            size += length;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(datafile, "rw");
        randomAccessFile.setLength(size);
        randomAccessFile.close();
        store();
    }


    private void store() throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(indexfile));
        outputStream.writeObject(index);
        outputStream.close();
    }

    private void move(long target, long start, long length) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(datafile, "rw");
        randomAccessFile.seek(start);
        byte[] b = new byte[(int) length];
        randomAccessFile.read(b);
        randomAccessFile.seek(target);
        randomAccessFile.write(b);
        randomAccessFile.close();
    }
}

class Value implements Comparable<Value>, Serializable {
    private String key;
    private long start;
    private long length;

    Value(String key, long start, long length) {
        this.key = key;
        this.start = start;
        this.length = length;
    }

    @Override
    public boolean equals(Object obj) {
        Value obj1 = (Value) obj;
        return key.equals(obj1.key);
    }

    String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    long getStart() {
        return start;
    }

    void setStart(long start) {
        this.start = start;
    }

    long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Value{" +
                "key='" + key + '\'' +
                ", start=" + start +
                ", length=" + length +
                '}';
    }

    @Override
    public int compareTo(Value o) {
        return (int) (this.start - o.start);
    }
}

