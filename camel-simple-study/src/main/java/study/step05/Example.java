package study.step05;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * 例外が発生した場合の処理記述
 */
public class Example {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // 例外処理用のルート、通常のルートの前に定義しておく
                onException(RuntimeException.class, Exception.class) //
                        .handled(true) //
                        // Processerを作らずボディにメッセージを追加する記述方法
                        .setBody().constant("something happened.") //
                        // 独自実装も追加出来る
                        .process((e) -> {
                            String body = e.getIn().getBody(String.class);
                            body = body + " (T_T)";
                            e.getIn().setBody(body);
                        }) //
                        .to("stream:out");

                from("stream:in?promptMessage=Enter :").routeId("exampleRoute") //
                        .process((exchange) -> {
                            String message = exchange.getIn().getBody(String.class);
                            if ("".equals(message))
                                throw new RuntimeException("the end");

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
