package study.step08;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * 例外が発生したらリトライとかさせてみる
 */
public class Example {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // 1つ目のルート
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // 例外処理用のルート、通常のルートの前に定義しておく
                onException(Exception.class) //
                        // 10回のリトライ（初回と合わせて11回実行することになる）
                        .maximumRedeliveries(10) //
                        // 初回から4回目までのリトライ間隔は1秒,5回目以降のリトライ間隔は3秒になる
                        .delayPattern("0:1000;5:3000")
                        // handled(true)は正常に処理し終えたよという事。例外をリスローしないという事。
                        .handled(true) //
                        // Processerを作らずボディにメッセージを追加する記述方法
                        .setBody().constant("something happened.") //
                        // 独自実装も追加出来る
                        .process((exchange) -> {
                            String body = exchange.getIn().getBody(String.class);
                            body = body + " (T_T)";
                            exchange.getIn().setBody(body);
                        }) //
                        .to("stream:out");

                from("stream:in?promptMessage=Enter :").routeId("exampleRoute1") //
                        .process((exchange) -> {
                            String message = exchange.getIn().getBody(String.class);
                            if ("".equals(message)) {
                                System.out.println("let's try!");
                                throw new RuntimeException("the end.");
                            }

                            message = message + " + process";
                            exchange.getIn().setBody(message);
                        }) //
                        .bean(CustomLogic01.class, "logic(${body})") //
                        // 2つ目のルートを呼出す
                        .to("direct:route2") //
                        .to("stream:out");
            }
        });
        // 2つ目のルート
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:route2").routeId("exampleRoute2") //
                        .to("log:route2").throwException(new Exception("error"));
            }
        });
        // ずっと起動している
        main.run();
    }
}
