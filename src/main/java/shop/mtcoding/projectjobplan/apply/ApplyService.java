package shop.mtcoding.projectjobplan.apply;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.mtcoding.projectjobplan.board.Board;
import shop.mtcoding.projectjobplan.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplyService {
    private final ApplyJpaRepository applyJpaRepository;
    private final ApplyQueryRepository applyQueryRepository;

    public void createApply(ApplyRequest.ApplyDTO requestDTO) {
        // todo : (개인) 지원하기
    }

    public void getAllResumeByBoardUserId(int boardUserId) {
        // todo : (기업) 모든 지원자 현황 보기
    }

    public void getAllResumeByBoardId(int boardId) {
        // todo : (기업) 공고별 지원자 보기
    }

    public List<ApplyResponse.ApplyDTO> getAllBoardByResumeId(User sessionUser) {
        // todo : (개인) 지원 현황 보기
        Sort sort = Sort.by(Sort.Direction.DESC, "resumeId");
        List<Apply> applyList = applyJpaRepository.findAll(sort);

        return applyList.stream().map(apply -> new ApplyResponse.ApplyDTO(apply, sessionUser)).toList();
    }

    public void updateApply(ApplyRequest.UpdateDTO requestDTO) {
        // todo : 합격/불합격 처리
    }


}
