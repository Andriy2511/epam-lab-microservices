package org.example.trainerworkloadservice.repository;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingMonthSummaryRepository extends JpaRepository<TrainingMonthSummary, Long> {
    Optional<TrainingMonthSummary> findByMonthNumberAndTrainingYear(
            int monthNumber,
            TrainingYear trainingYear
    );

    @Query("""
            SELECT tms
            FROM TrainingMonthSummary tms
            JOIN tms.trainingYear ty
            JOIN ty.trainerWorkload tw
            WHERE tw.trainerId = :trainerId
              AND ty.trainingYear = :trainingYear
              AND tms.monthNumber = :monthNumber
            """)
    Optional<TrainingMonthSummary> findByTrainerIdAndTrainingYearAndMonthNumber(
            @Param("trainerId") Long trainerId,
            @Param("trainingYear") int trainingYear,
            @Param("monthNumber") int monthNumber
    );
}
