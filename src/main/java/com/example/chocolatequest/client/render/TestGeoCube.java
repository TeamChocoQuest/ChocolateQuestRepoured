import software.bernie.geckolib.cache.object.GeoCube;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class TestGeoCube {
    public static void main(String[] args) {
        System.out.println("Methods:");
        for (Method m : GeoCube.class.getMethods()) {
            if (m.getDeclaringClass() != Object.class) {
                System.out.println(m.getName() + " -> " + m.getReturnType().getName());
            }
        }
        System.out.println("Fields:");
        for (Field f : GeoCube.class.getDeclaredFields()) {
            System.out.println(f.getName() + " -> " + f.getType().getName());
        }
    }
}
