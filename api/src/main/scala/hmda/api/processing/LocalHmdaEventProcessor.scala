package hmda.api.processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import hmda.persistence.CommonMessages._
import hmda.persistence.processing.HmdaFileParser.{ ParsingCompleted, ReadHmdaRawFile }
import hmda.persistence.processing.HmdaFileValidator.{ BeginValidation, ValidationCompleted, ValidationCompletedWitErrors, ValidationStarted }
import hmda.persistence.processing.HmdaRawFile.{ UploadCompleted, UploadStarted }
import hmda.persistence.processing.{ HmdaFileParser, HmdaFileValidator, HmdaRawFile }

object LocalHmdaEventProcessor {
  def props(): Props = Props(new LocalHmdaEventProcessor)

  def createLocalHmdaEventProcessor(system: ActorSystem): ActorRef = {
    system.actorOf(LocalHmdaEventProcessor.props())
  }

}

class LocalHmdaEventProcessor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[Event])
  }

  override def receive: Receive = {

    case e: Event => e match {
      case UploadStarted(submissionId) =>
        log.debug(s"Upload started for submission $submissionId")

      case UploadCompleted(size, submissionId) =>
        fireUploadCompletedEvents(size, submissionId)

      case ParsingCompleted(submissionId) =>
        fireParsingCompletedEvents(submissionId)

      case ValidationStarted(submissionId) =>
        log.debug(s"Validation started for $submissionId")

      case ValidationCompletedWitErrors(submissionId) =>
        log.debug("validation completed with errors")
        fireValidationCompletedEvents(submissionId)

      case ValidationCompleted(submissionId) =>
        fireValidationCompletedEvents(submissionId)

      case _ => //ignore other events

    }
  }

  private def fireUploadCompletedEvents(size: Int, submissionId: String): Unit = {
    log.debug(s"$size lines uploaded for submission $submissionId")
    val hmdaFileParser = context.actorOf(HmdaFileParser.props(submissionId))
    hmdaFileParser ! ReadHmdaRawFile(s"${HmdaRawFile.name}-$submissionId")
  }

  private def fireParsingCompletedEvents(submissionId: String): Unit = {
    log.debug(s"Parsing completed for $submissionId")
    val larValidator = context.system.actorSelection(s"/user/larValidation")
    val hmdaFileValidator = context.actorOf(HmdaFileValidator.props(submissionId, larValidator))
    hmdaFileValidator ! BeginValidation
  }

  private def fireValidationCompletedEvents(submissionId: String): Unit = {
    log.debug(s"Validation completed for submission $submissionId")
  }
}
