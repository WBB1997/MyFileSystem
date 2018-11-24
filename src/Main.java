import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        FileSystem fileSystem = FileSystem.getInstance();
        try {
//            fileSystem.update("test3","4");
//            List<String> list =  fileSystem.valueList();
//            List<Value> v = fileSystem.getV();
//            for(String str : list)
//                System.out.println(str);
//            for (Value value:v)
//                System.out.println(value.toString());
//            fileSystem.add("test1","1");
//            fileSystem.add("test2","2");
//            fileSystem.add("test3","3");
//            fileSystem.add("test4","4");
            System.out.println(fileSystem.get("4"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
