package com.initialsgame.backend.domain.word.repository;

import com.initialsgame.backend.domain.word.entity.Word;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Long> {

	Optional<Word> findByText(String text);

	List<Word> findByInitialsAndSyllableCount(String initials, int syllableCount);

	@Query(value = "SELECT * FROM words WHERE syllable_count IN (:syllableCounts) ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
	Optional<Word> findRandomBySyllableCountIn(@Param("syllableCounts") List<Integer> syllableCounts);
}
