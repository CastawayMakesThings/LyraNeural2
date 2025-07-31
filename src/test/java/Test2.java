import com.equinox.lyra2.api.Running;
import com.equinox.lyra2.objects.LyraModel;

public class Test2 {
    public static void main(String[] args) {
        LyraModel lyraModel = new LyraModel();
        lyraModel.load("testModel.lyra");
        System.out.println(Running.feed(lyraModel, 3));
    }
}
