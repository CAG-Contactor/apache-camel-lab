package se.cag.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;

import javax.jms.ConnectionFactory;

public class CamelTraceInOutBetweenRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("vm://localhost");
        CamelContext context = getContext();
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        from("direct:trace")
                .routeId("MessageInOutTracer")
                .process(new InOutMessageLogger())
                .to("jms:inbox")
                .process(new InOutMessageLogger())
                .to("jms:outbox")
                .process(new InOutMessageLogger());
    }

    private class InOutMessageLogger implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println("exchange.getIn().getHeaders() = " + exchange.getIn().getHeaders());
            System.out.println("exchange.getIn().getBody() = " + exchange.getIn().getBody());
            System.out.println("exchange.getOut().getHeaders() = " + exchange.getOut().getHeaders());
            System.out.println("exchange.getOut().getBody() = " + exchange.getOut().getBody());

            exchange.getOut().setHeaders(exchange.getIn().getHeaders());
            exchange.getOut().setBody(exchange.getIn().getBody());

        }


    }
}
