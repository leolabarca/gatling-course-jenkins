package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicLoadSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def getAllVideoGames() =
    exec(
      http("Get all video games")
        .get("/videogame")
    )

  def getSpecificGame() =
    exec(
      http("Get specific game")
        .get("/videogame/2")
    )

  val scn = scenario("Basic Load Simulation")
    .pause(10)                 // 👈 “silencio” inicial 10 s
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(
      rampConcurrentUsers(1).to(50).during(30.seconds),   // sube 1 → 50
      rampConcurrentUsers(50).to(20).during(30.seconds),  // baja 50 → 20
      constantConcurrentUsers(20).during(60.seconds)      // 20 conc. por 60 s
    ).protocols(httpProtocol)
  )
}
