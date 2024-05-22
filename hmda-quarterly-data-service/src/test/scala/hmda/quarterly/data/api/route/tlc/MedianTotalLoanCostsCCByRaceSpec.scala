package hmda.quarterly.data.api.route

import org.scalatest.{Matchers, WordSpec}
import hmda.quarterly.data.api.route.rates.tlc.MedianTotalLoanCostsCCByRace


class MedianTotalLoanCostsCCByRaceSpec extends WordSpec with Matchers {
  val route = MedianTotalLoanCostsCCByRace.getRoute
  val routeSummary = MedianTotalLoanCostsCCByRace.getSummary
  "median total loan costs cc by race route" should {
    "return the correct summary route" in {
      assert(!routeSummary.isCompleted)
    }
  }
  "median total loan costs cc by race route" should {
    "have a string title" in {
      assert(route.title.isInstanceOf[String])
    }
  }
}
