package study;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Example {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        PropertiesComponent pc = new PropertiesComponent();
        pc.setLocation("camel.properties");
        main.bind("properties", pc);
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("twitter://search?type=direct&keywords=camel" //
                        + "&consumerKey={{CONSUMER_KEY}}" //
                        + "&consumerSecret={{CONSUMER_SECRET}}" //
                        + "&accessToken={{ACCESS_TOKEN}}" //
                        + "&accessTokenSecret={{ACCESS_TOKEN_SECRET}}") // (1)
                                // .to("log:camel-twitter-log") //
                                .marshal().json(JsonLibrary.Jackson, false) // (2)
                                .to("stream:out");
            }
        });
        main.start();
    }

}
