package study.step02;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * 独自処理を追加するしてみる
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
                            message = message + "!";
                            exchange.getIn().setBody(message);
                        }) //
                        .to("stream:out");
            }
        });
        // ずっと起動している
        main.run();
    }
}
