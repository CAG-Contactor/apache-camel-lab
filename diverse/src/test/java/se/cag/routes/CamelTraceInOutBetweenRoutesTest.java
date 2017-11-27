package se.cag.routes;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CamelTraceInOutBetweenRoutesTest extends CamelTestSupport {

    protected RouteBuilder createRouteBuilder() {
        return new CamelTraceInOutBetweenRoutes();
    }

    @Test
    public void testInOuData() {
        NotifyBuilder notifyBuilder = new NotifyBuilder(context).whenDone(1).create();
        template.sendBodyAndHeader("direct:trace", "Test", "key", "value");
        assertTrue(notifyBuilder.matchesMockWaitTime());
    }

}