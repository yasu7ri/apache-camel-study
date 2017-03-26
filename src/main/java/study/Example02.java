package study;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Example02 {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stream:in?promptMessage=Enter :")
                .process((exchange) -> {
                        String message = exchange.getIn().getBody(String.class);
                        message = message + "!";
                        exchange.getIn().setBody(message);
                    
                }).to("stream:out");
            }
        });
        // ずっと起動している
        main.run();
    }
}
