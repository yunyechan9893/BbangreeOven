package com.bbangle.bbangle.preference.repository;

import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.preference.domain.Preference;
import com.bbangle.bbangle.preference.dto.MemberPreferenceResponse;
import java.util.Optional;

public interface PreferenceQueryDSLRepository {
    MemberPreferenceResponse getMemberPreference(Member member);
}
