package study.step03;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * 外部の独自処理を追加してみる
 */
public class Example {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stream:in?promptMessage=Enter :") //
                        .process((exchange) -> {
                            String message = exchange.getIn().getBody(String.class);
                            message = message + " + process";
                            exchange.getIn().setBody(message);
                        }) //
                        .bean(CustomLogic01.class, "logic(${body})") //
                        .to("stream:out");
            }
        });
        // ずっと起動している
        main.run();
    }
}
