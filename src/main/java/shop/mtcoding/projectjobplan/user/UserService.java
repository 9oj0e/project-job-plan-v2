package shop.mtcoding.projectjobplan.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.projectjobplan._core.errors.exception.Exception400;
import shop.mtcoding.projectjobplan._core.errors.exception.Exception401;
import shop.mtcoding.projectjobplan._core.errors.exception.Exception404;
import shop.mtcoding.projectjobplan.apply.Apply;
import shop.mtcoding.projectjobplan.apply.ApplyJpaRepository;
import shop.mtcoding.projectjobplan.offer.Offer;
import shop.mtcoding.projectjobplan.offer.OfferJpaRepository;
import shop.mtcoding.projectjobplan.rating.RatingJpaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final ApplyJpaRepository applyJpaRepository;
    private final RatingJpaRepository ratingJpaRepository;
    private final OfferJpaRepository offerJpaRepository;

    @Transactional
    public User createUser(UserRequest.JoinDTO requestDTO) { // join
        Optional<User> userOP = userJpaRepository.findByUsername(requestDTO.getUsername());
        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저입니다.");
        }
        User user = new User(requestDTO);

        return userJpaRepository.save(user);
    }

    public User getUser(UserRequest.LoginDTO requestDTO) { // login

        return userJpaRepository.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 틀렸습니다."));
    }

    @Transactional(readOnly = true)
    public UserResponse.ProfileDTO getUser(Integer sessionUserId, Integer boardId, Integer resumeId, Pageable pageable) {
        User user = userJpaRepository.findById(sessionUserId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 유저입니다."));
        List<Apply> applyList;
        List<Offer> offerList;
        if (user.getIsEmployer()) { // 기업 마이페이지
            if (boardId == null) { // 모든 지원자 현황 보기 & 모든 제안 현황 보기
                applyList = applyJpaRepository.findByBoardUserId(user.getId());
                offerList = offerJpaRepository.findByBoardUserId(user.getId());
            } else { // 공고별 지원자 보기 & 공고별 제안 현황 보기
                applyList = applyJpaRepository.findByBoardId(boardId);
                offerList = offerJpaRepository.findByBoardId(boardId);
            }
        } else { // 개인 마이페이지
            if (resumeId == null) { // 모든 지원 현황 보기 & 모든 제안 현황 보기
                applyList = applyJpaRepository.findByResumeUserId(user.getId());
                offerList = offerJpaRepository.findByResumeUserId(user.getId());
            } else { // 이력서별 지원 현황 보기 & 이력서별 제안 현황 보기
                applyList = applyJpaRepository.findByResumeId(resumeId);
                offerList = offerJpaRepository.findByResumeId(resumeId);
            }
        }
        Double rating = ratingJpaRepository.findRatingAvgByUserId(user.getId()).orElse(0.0);

        return new UserResponse.ProfileDTO(user, applyList, offerList, rating, pageable);
    }

    public UserResponse.UpdateFormDTO getUser(int userId) { // 회원수정폼
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));

        return new UserResponse.UpdateFormDTO(user);
    }

    @Transactional // 회원수정
    public User setUser(int userId, UserRequest.UpdateDTO requestDTO) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));
        user.update(requestDTO);

        return user;
    }

    @Transactional
    public void removeUser(int userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));

        userJpaRepository.delete(user);
    }
}
