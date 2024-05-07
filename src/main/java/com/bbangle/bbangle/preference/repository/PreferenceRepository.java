package com.bbangle.bbangle.preference.repository;

import com.bbangle.bbangle.preference.domain.Preference;
import com.bbangle.bbangle.preference.domain.PreferenceType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long>, PreferenceQueryDSLRepository {

    Optional<Preference> findByPreferenceType(PreferenceType preferenceType);

}
