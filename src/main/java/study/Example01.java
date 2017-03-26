package study;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Example01 {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("hello camel");

        Main main = new Main();
        // 一度起動するだけ
        // main.start();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // 5回
                from("timer:test-hello?repeatCount=5").to("log:test-log");
            }
        });
        // ずっと起動している
        main.run();
    }

}
