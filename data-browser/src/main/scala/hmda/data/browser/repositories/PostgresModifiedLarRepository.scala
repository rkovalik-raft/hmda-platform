package hmda.data.browser.repositories
import akka.NotUsed
import akka.stream.scaladsl.Source
import hmda.data.browser.models.ModifiedLarEntity
import monix.eval.Task
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class PostgresModifiedLarRepository(tableName: String,
                                    config: DatabaseConfig[JdbcProfile])
    extends ModifiedLarRepository {
  import config._
  import config.profile.api._

  private val columns: String =
    """id,
    	lei,
    	loan_type,
    	loan_purpose,
    	preapproval,
    	construction_method,
    	occupancy_type,
    	loan_amount,
    	action_taken_type,
    	state,
    	county,
    	tract,
      ethnicity_applicant_1,
      ethnicity_applicant_2,
      ethnicity_applicant_3,
      ethnicity_applicant_4,
      ethnicity_applicant_5,
      ethnicity_observed_applicant,
      ethnicity_co_applicant_1,
      ethnicity_co_applicant_2,
      ethnicity_co_applicant_3,
      ethnicity_co_applicant_4,
      ethnicity_co_applicant_5,
      ethnicity_observed_co_applicant,
      race_applicant_1,
      race_applicant_2,
      race_applicant_3,
      race_applicant_4,
      race_applicant_5,
      race_co_applicant_1,
      race_co_applicant_2,
      race_co_applicant_3,
      race_co_applicant_4,
      race_co_applicant_5,
      race_observed_applicant,
      race_observed_co_applicant,
      sex_applicant,
      sex_co_applicant,
      observed_sex_applicant,
      observed_sex_co_applicant,
      age_applicant,
      applicant_age_greater_than_62,
      age_co_applicant,
      coapplicant_age_greater_than_62,
      income,
      purchaser_type,
      rate_spread,
      hoepa_status,
      lien_status,
      credit_score_type_applicant,
      credit_score_type_co_applicant,
      denial_reason1,
      denial_reason2,
      denial_reason3,
      denial_reason4,
      total_loan_costs,
      total_points,
      origination_charges,
      discount_points,
      lender_credits,
      interest_rate,
      payment_penalty,
      debt_to_incode,
      loan_value_ratio,
      loan_term,
      rate_spread_intro,
      baloon_payment,
      insert_only_payment,
      amortization,
      other_amortization,
      property_value,
      home_security_policy,
      lan_property_interest,
      total_units,
      mf_affordable,
      application_submission,
      payable,
      aus1,
      aus2,
      aus3,
      aus4,
      aus5,
      reverse_mortgage,
      line_of_credits,
      business_or_commercial,
      population,
      minority_population_percent,
      ffiec_med_fam_income,
      tract_to_msamd,
      owner_occupied_units,
      one_to_four_fam_units,
      msa_md,
      loan_flag,
      created_at,
      submission_id,
      msa_md_name,
      filing_year,
      conforming_loan_limit,
      median_age,
      median_age_calculated,
      median_income_percentage,
      race_categorization,
      sex_categorization,
      ethnicity_categorization,
      percent_median_msa_income
    """.stripMargin

  override def find(msaMd: Int,
                    actionTaken: Int,
                    race: String): Source[ModifiedLarEntity, NotUsed] = {
    val searchQuery = sql"""
    SELECT #$columns
    FROM #${tableName}
    WHERE msa_md = #${msaMd}
      AND action_taken_type = #${actionTaken}
      AND race_categorization = '#${race}'
    """.as[ModifiedLarEntity]

    val publisher = db.stream(searchQuery)
    Source.fromPublisher(publisher)
  }

  override def find(state: String,
                    actionTaken: Int,
                    race: String): Source[ModifiedLarEntity, NotUsed] = {
    val searchQuery = sql"""
    SELECT #$columns
    FROM #${tableName}
    WHERE state = '#${state}'
      AND action_taken_type = #${actionTaken}
      AND race_categorization = '#${race}'
    """.as[ModifiedLarEntity]

    val publisher = db.stream(searchQuery)
    Source.fromPublisher(publisher)
  }

  override def findAndAggregate(msaMd: Int,
                                actionTaken: Int,
                                race: String): Task[Statistic] = {
    val query =
      sql"""SELECT
              COUNT(loan_amount),
              SUM(loan_amount)
            FROM #${tableName}
            WHERE msa_md = #${msaMd}
              AND action_taken_type = #${actionTaken}
              AND race_categorization = '#${race}'
      """.as[Statistic].head

    Task.deferFuture(db.run(query))
  }

  override def findAndAggregate(state: String,
                                actionTaken: Int,
                                race: String): Task[Statistic] = {
    val query =
      sql"""SELECT
              COUNT(loan_amount),
              SUM(loan_amount)
            FROM #${tableName}
            WHERE state = '#${state}'
              AND action_taken_type = #${actionTaken}
              AND race_categorization = '#${race}'
      """.as[Statistic].head

    Task.deferFuture(db.run(query))
  }

  override def find(actionTaken: Int,
                    race: String): Source[ModifiedLarEntity, NotUsed] = {
    val searchQuery = sql"""
    SELECT #$columns
    FROM #${tableName}
    WHERE action_taken_type = #${actionTaken}
      AND race_categorization = '#${race}'
    """.as[ModifiedLarEntity]

    val publisher = db.stream(searchQuery)
    Source.fromPublisher(publisher)
  }

  override def findAndAggregate(actionTaken: Int,
                                race: String): Task[Statistic] = {
    val query =
      sql"""SELECT
              COUNT(loan_amount),
              SUM(loan_amount)
            FROM #${tableName}
            WHERE action_taken_type = #${actionTaken}
            AND race_categorization = '#${race}'
      """.as[Statistic].head

    Task.deferFuture(db.run(query))
  }
}
