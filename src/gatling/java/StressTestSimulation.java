import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class StressTestSimulation extends Simulation {

    private static final String authorization = "Bearer token";

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://url.com.br")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .disableFollowRedirect();

    private static final String requestBody = """
            {
              "document": "documento",
              "attributes": ["NAME", "ADDRESSES", "DOCUMENT", "REGISTRATIONSTATUS"]
            }
            """;

    private static final ScenarioBuilder scn = scenario(" API Test")
            .exec(http("request")
                    .post("/ms-da/enrichment/v1/enrichments/people")
                    .body(StringBody(requestBody)).asJson());

    {
        setUp(
                scn.injectOpen(
                        rampUsersPerSec(10).to(100).during(60),
                        constantUsersPerSec(100).during(300)
                )
        ).protocols(httpProtocol);
    }
}
