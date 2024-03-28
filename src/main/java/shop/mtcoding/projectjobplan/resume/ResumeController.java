package shop.mtcoding.projectjobplan.resume;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.projectjobplan.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ResumeController {
    private final HttpSession session;
    private final ResumeService resumeService;

    @GetMapping("/resumes/main")
    public String main() {
        return "/resume/main";
    }

    @GetMapping("/resumes/post-form") // 이력서 작성 폼
    public String postForm(HttpServletRequest request) {

        return "/resume/post-form";
    }

    @PostMapping("/resumes/post") // 이력서 작성 action
    public String post(ResumeRequest.SaveDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Resume resume = resumeService.createResume(requestDTO, sessionUser);

        return "redirect:/resume/" + resume.getId();
    }

    @GetMapping("/resumes/{resumeId}")
    public String detail(@PathVariable int resumeId,HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.DetailDTO resumeDetail = resumeService.findResumeById(resumeId,sessionUser);
        request.setAttribute("resumeDetail", resumeDetail);

        return "/resume/detail";
    }

    @GetMapping("/resumes/listings")
    public String listings(HttpServletRequest request) {
        List<ResumeResponse.MainDTO> responseDTO = resumeService.getAllResume();
        request.setAttribute("resumeList", responseDTO);

        return "/resume/listings";
    }

    @GetMapping("/resumes/{resumeId}/update-form")
    public String updateForm(@PathVariable int resumeId, HttpServletRequest request) {
        // todo: 권한체크
        ResumeResponse.UpdateDTO responseDTO = resumeService.getResume(resumeId);
        request.setAttribute("resume", responseDTO);

        return "/resume/update-form";
    }

    @PostMapping("/resumes/{resumeId}/update")
    public String update(@PathVariable int resumeId, ResumeRequest.UpdateDTO requestDTO) {
        resumeService.setResume(resumeId, requestDTO);

        return "redirect:/resumes/" + resumeId;
    }

    @PostMapping("/resumes/{resumeId}/delete")
    public String delete(@PathVariable int resumeId) {
        resumeService.removeResume(resumeId);

        return "redirect:/resume/listings";
    }
}
