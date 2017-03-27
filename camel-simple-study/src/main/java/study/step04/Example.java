package study.step04;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * 例外を発生させてみる
 */
public class Example {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stream:in?promptMessage=Enter :").routeId("exampleRoute") //
                        .process((exchange) -> {
                            String message = exchange.getIn().getBody(String.class);
                            if ("".equals(message))
                                throw new RuntimeException("the end");

                            message = message + " + process";
                            exchange.getIn().setBody(message);
                        }).id("myprocess") //
                        .bean(CustomLogic01.class, "logic(${body})").id("CustomLogic01#logic(string)") //
                        .to("stream:out");
            }
        });
        // ずっと起動している
        main.run();
    }
}
