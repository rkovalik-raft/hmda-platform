package hmda.api.http.codec.filing.submission

import hmda.api.http.model.filing.submissions.{
  PaginatedResponse,
  PaginationLinks,
  ParsingErrorSummary
}
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.syntax._
import SubmissionStatusCodec._
import hmda.messages.submission.SubmissionProcessingEvents.HmdaRowParsedError
import io.circe.generic.auto._
import hmda.Seq
import hmda.model.filing.submission.SubmissionStatus
import hmda.model.filing.submissions.PaginatedResource

object ParsingErrorSummaryCodec {

  implicit val parsingErrorSummaryEncoder: Encoder[ParsingErrorSummary] =
    new Encoder[ParsingErrorSummary] {
      override def apply(a: ParsingErrorSummary): Json = Json.obj(
        ("transmittalSheetErrors", Json.arr(a.transmittalSheetErrors.asJson)),
        ("larErrors", Json.arr(a.larErrors.asJson)),
        ("count", Json.fromInt(a.count)),
        ("total", Json.fromInt(a.total)),
        ("status", a.status.asJson),
        ("_links", a.links.asJson)
      )
    }

  implicit val parsingErrorSummaryDecoder: Decoder[ParsingErrorSummary] =
    new Decoder[ParsingErrorSummary] {
      override def apply(c: HCursor): Result[ParsingErrorSummary] =
        for {
          tsErrors <- c.downField("transmittalSheetErrors").as[Seq[String]]
          larErrors <- c.downField("larErrors").as[Seq[HmdaRowParsedError]]
          total <- c.downField("total").as[Int]
          status <- c.downField("status").as[SubmissionStatus]
          links <- c.downField("links").as[PaginationLinks]
        } yield {
          val path = PaginatedResponse.staticPath(links.href)
          val currentPage = PaginatedResponse.currentPage(links.self)
          ParsingErrorSummary(tsErrors,
                              larErrors,
                              path,
                              currentPage,
                              total,
                              status)
        }
    }

}
