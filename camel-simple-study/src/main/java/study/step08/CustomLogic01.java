package study.step08;

public class CustomLogic01 {
    public String logic(String arg) {
        if ("".equals(arg))
            throw new RuntimeException("the end");

        arg = arg + " + customLoogic01#logic(String arg)";
        return arg;
    }
}
