package hmda.quarterly.data.api.route.rates.tlc

import hmda.model.filing.lar.enums.FHAInsured
import hmda.quarterly.data.api.dao.repo.QuarterlyGraphRepo
import hmda.quarterly.data.api.dto.QuarterGraphData.GraphSeriesSummary
import hmda.quarterly.data.api.route.rates.RatesGraph
import hmda.quarterly.data.api.route.rates.RatesGraph._
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

object MedianTotalLoanCostsFHAByRaceLoanPurposeHome extends RatesGraph(
  "tlc",
  "tlc-fha-re-loan-purpose-home",
  FHA_BY_RACE_TITLE,
  FHA_BY_RACE_SUBTITLE,
  Category.BY_RACE) {

  override protected def getSummaryByRace(title: String, race: String): CancelableFuture[GraphSeriesSummary] =
    QuarterlyGraphRepo.fetchMedianTotalLoanCostsByTypeByRaceLoanPurposeHome(FHAInsured, race, heloc = false, conforming = false)
      .map(convertToGraph(title, _)).runToFuture
}
