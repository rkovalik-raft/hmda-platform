package hmda.quarterly.data.api.route

import org.scalatest.{Matchers, WordSpec}
import hmda.quarterly.data.api.route.rates.ltv.MedianCLTVByTypeLoanPurposeHome


class MedianCLTVByTypeLoanPurposeHomeSpec extends WordSpec with Matchers {
  val route = MedianCLTVByTypeLoanPurposeHome.getRoute
  val routeSummary = MedianCLTVByTypeLoanPurposeHome.getSummary
  "median cltv by type loan purpose home route" should {
    "return the correct summary route" in {
      assert(routeSummary.isCompleted)
    }
  }
  "median cltv by type loan purpose home route" should {
    "have a string title" in {
      assert(route.title.isInstanceOf[String])
    }
  }
}
