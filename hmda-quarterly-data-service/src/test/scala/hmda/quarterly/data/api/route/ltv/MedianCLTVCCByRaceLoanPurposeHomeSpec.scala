package hmda.quarterly.data.api.route

import org.scalatest.{Matchers, WordSpec}
import hmda.quarterly.data.api.route.rates.ltv.MedianCLTVCCByRaceLoanPurposeHome


class MedianCLTVCCByRaceLoanPurposeHomeSpec extends WordSpec with Matchers {
  val route = MedianCLTVCCByRaceLoanPurposeHome.getRoute
  val routeSummary = MedianCLTVCCByRaceLoanPurposeHome.getSummary
  "median cltvcc by race loan purpose home route" should {
    "return the correct summary route" in {
      assert(routeSummary.isCompleted)
    }
  }
  "median cltvcc by race loan purpose home route" should {
    "have a string title" in {
      assert(route.title.isInstanceOf[String])
    }
  }
}
