import io.github.equinoxelectronic.lyra2.api.Running;
import io.github.equinoxelectronic.lyra2.objects.LyraModel;

public class Test2 {
    public static void main(String[] args) {
        LyraModel lyraModel = new LyraModel();
        lyraModel.load("testModel.lyra");
        System.out.println(Running.feed(lyraModel, 3));
    }
}
