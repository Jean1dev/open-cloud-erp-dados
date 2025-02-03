import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class StressTestSimulation extends Simulation {

  val authorization = "Bearer token"

  val httpProtocol = http
    .baseUrl("https://url.com.br")
    .header("Content-Type", "application/json")
    .header("Authorization", authorization)
    .disableFollowRedirect

  val requestBody = StringBody(
    """
      {
        "document": "documento",
        "attributes": ["NAME", "ADDRESSES", "DOCUMENT", "REGISTRATIONSTATUS"]
      }
    """.stripMargin)

  val scn = scenario(" API Test")
    .exec(http("request")
      .post("/ms-da/enrichment/v1/enrichments/people")
      .body(requestBody).asJson)

  setUp(
    scn.inject(
      rampUsersPerSec(10) to 100 during (1 minute), // Aumenta o número de usuários gradualmente de 10 para 100 durante 1 minuto
      constantUsersPerSec(100) during (5 minutes) // Mantém 100 usuários por segundo durante 5 minutos
    )
  ).protocols(httpProtocol)
}