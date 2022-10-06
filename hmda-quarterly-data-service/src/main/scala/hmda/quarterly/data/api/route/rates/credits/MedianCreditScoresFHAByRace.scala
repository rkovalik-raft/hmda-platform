package hmda.quarterly.data.api.route.rates.credits

import hmda.model.filing.lar.enums.FHAInsured
import hmda.quarterly.data.api.dao.repo.QuarterlyGraphRepo
import hmda.quarterly.data.api.dto.QuarterGraphData.GraphSeriesSummary
import hmda.quarterly.data.api.route.rates.RatesGraph
import hmda.quarterly.data.api.route.rates.RatesGraph.BY_RACE
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

object MedianCreditScoresFHAByRace extends RatesGraph("credit", "credit-scores-fha-re") {

  override protected def title: String = FHA_BY_RACE_TITLE
  override protected def subtitle: String = FHA_BY_RACE_SUBTITLE
  override protected def summaryType: RatesGraph.Value = BY_RACE

  override protected def getSummaryByRace(title: String, race: String): CancelableFuture[GraphSeriesSummary] =
    QuarterlyGraphRepo.fetchMedianCreditScoreByTypeByRace(FHAInsured, race)
      .map(convertToGraph(title, _)).runToFuture
}
