package org.example.trainerworkloadservice.repository.implementation;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.repository.TrainingMonthSummaryCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingMonthSummaryCustomRepositoryImpl implements TrainingMonthSummaryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.collection-name}")
    private String collectionName;

    @Autowired
    public TrainingMonthSummaryCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<TrainingMonthSummary> findTrainingMonthSummary(Long trainerId, int trainingYear, int monthNumber) {
        MatchOperation matchStage = Aggregation.match(
                Criteria.where("trainerId").is(trainerId)
                        .and("trainingYears.trainingYear").is(trainingYear)
                        .and("trainingYears.months.monthNumber").is(monthNumber)
        );

        UnwindOperation unwindYears = Aggregation.unwind("trainingYears");

        MatchOperation matchYearStage = Aggregation.match(
                Criteria.where("trainingYears.trainingYear").is(trainingYear)
        );

        UnwindOperation unwindMonths = Aggregation.unwind("trainingYears.months");

        MatchOperation matchMonthStage = Aggregation.match(
                Criteria.where("trainingYears.months.monthNumber").is(monthNumber)
        );

        ProjectionOperation projectStage = Aggregation.project()
                .and("trainingYears.months.monthNumber").as("monthNumber")
                .and("trainingYears.months.totalDuration").as("totalDuration");

        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                unwindYears,
                matchYearStage,
                unwindMonths,
                matchMonthStage,
                projectStage
        );

        AggregationResults<TrainingMonthSummary> results = mongoTemplate.aggregate(
                aggregation,
                collectionName,
                TrainingMonthSummary.class
        );

        return Optional.ofNullable(results.getUniqueMappedResult());
    }
}
