/**
 * Created by isopen on 19.09.2016.
 */

import com.isopen.polymorphism.BasePolymorphism;
import com.isopen.polymorphism.DerivativePolymorphism;

public class PolymorphismApplication {
    public static void main(String[] args) {
        BasePolymorphism basePolymorphism = new DerivativePolymorphism();
        System.out.println(basePolymorphism.sayHello());
        System.out.println(basePolymorphism.sayHi());
        System.out.println(basePolymorphism.say());
        System.out.println(basePolymorphism.getSayHao());
    }
}
