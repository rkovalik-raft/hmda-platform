package hmda.quarterly.data.api.route

import org.scalatest.{Matchers, WordSpec}
import hmda.quarterly.data.api.route.rates.tlc.MedianTotalLoanCostsFHAByRaceLoanPurposeRefinance


class MedianTotalLoanCostsFHAByRaceLoanPurposeRefinanceSpec extends WordSpec with Matchers {
  val route = MedianTotalLoanCostsFHAByRaceLoanPurposeRefinance.getRoute
  val routeSummary = MedianTotalLoanCostsFHAByRaceLoanPurposeRefinance.getSummary
  "median total loan costs fha by race loan purpose refinance route" should {
    "return the correct summary route" in {
      assert(routeSummary.isCompleted)
    }
  }
  "median total loan costs fha by race loan purpose refinance route" should {
    "have a string title" in {
      assert(route.title.isInstanceOf[String])
    }
  }
}
