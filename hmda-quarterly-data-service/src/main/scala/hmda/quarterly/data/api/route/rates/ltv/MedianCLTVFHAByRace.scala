package hmda.quarterly.data.api.route.rates.ltv

import hmda.model.filing.lar.enums.FHAInsured
import hmda.quarterly.data.api.dao.repo.QuarterlyGraphRepo
import hmda.quarterly.data.api.dto.QuarterGraphData.GraphSeriesSummary
import hmda.quarterly.data.api.route.rates.RatesGraph
import hmda.quarterly.data.api.route.rates.RatesGraph.BY_RACE
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

object MedianCLTVFHAByRace extends RatesGraph("ltv", "ltv-fha-re") {

  override protected def title: String = FHA_BY_RACE_TITLE
  override protected def subtitle: String = FHA_BY_RACE_SUBTITLE
  override protected def summaryType: RatesGraph.Value = BY_RACE

  override protected def getSummaryByRace(title: String, race: String): CancelableFuture[GraphSeriesSummary] =
    QuarterlyGraphRepo.fetchMedianCLTVByTypeByRace(FHAInsured, race, heloc = false, conforming = false)
      .map(convertToGraph(title, _)).runToFuture
}
