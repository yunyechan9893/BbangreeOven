package com.bbangle.bbangle.member.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.common.image.service.S3Service;
import com.bbangle.bbangle.common.image.validation.ImageValidator;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.dto.InfoUpdateRequest;
import com.bbangle.bbangle.member.dto.ProfileInfoResponseDto;
import com.bbangle.bbangle.member.repository.ProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;
  private final S3Service imageService;

  @Override
  public ProfileInfoResponseDto getProfileInfo(Long memberId) {
    Member member = profileRepository.findById(memberId)
        .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
    return ProfileInfoResponseDto.builder()
        .profileImg(member.getProfile())
        .nickname(member.getNickname())
        .phoneNumber(member.getPhone())
        .birthDate(member.getBirth())
        .build();
  }

  @Transactional
  public void updateProfileInfo(
      InfoUpdateRequest request, Long memberId,
      MultipartFile profileImg
  ) {
    Member member = profileRepository.findById(memberId)
        .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
    if (profileImg != null && !profileImg.isEmpty()) {
      ImageValidator.validateImage(profileImg);
      String imgUrl = imageService.saveImage(profileImg);
      member.updateProfile(imgUrl);
    }
    member.update(request);
  }

  @Override
  public String doubleCheckNickname(String nickname) {
    Optional<Member> member = profileRepository.findByNickname(nickname);
    if (!member.isEmpty()) {
      return nickname;
    }
    return "";
  }
}
