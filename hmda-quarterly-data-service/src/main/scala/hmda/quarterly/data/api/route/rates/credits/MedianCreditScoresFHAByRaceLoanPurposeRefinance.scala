package hmda.quarterly.data.api.route.rates.credits

import hmda.model.filing.lar.enums.FHAInsured
import hmda.quarterly.data.api.dao.repo.QuarterlyGraphRepo
import hmda.quarterly.data.api.dto.QuarterGraphData.GraphSeriesSummary
import hmda.quarterly.data.api.route.rates.CountRatesGraph
import hmda.quarterly.data.api.route.rates.RatesGraph._
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

object MedianCreditScoresFHAByRaceLoanPurposeRefinance extends CountRatesGraph(
  "credit",
  "credit-scores-fha-re-loan-purpose-refinance",
  FHA_BY_RACE_TITLE,
  FHA_BY_RACE_SUBTITLE,
  Category.BY_RACE) {

  override protected def getSummaryByRace(title: String, race: String): CancelableFuture[GraphSeriesSummary] =
    QuarterlyGraphRepo.fetchMedianCreditScoreByTypeByRaceLoanPurposeRefinance(FHAInsured, race)
      .map(convertToGraph(title, _)).runToFuture
}
