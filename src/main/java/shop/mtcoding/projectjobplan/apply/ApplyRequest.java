package shop.mtcoding.projectjobplan.apply;

import lombok.Data;
import shop.mtcoding.projectjobplan.resume.Resume;

public class ApplyRequest {
    @Data
    public static class ApplyDTO {
        private Integer resumeId;
        private Integer boardId;
        // toEntity 가능?
    }
    
    @Data
    public static class UpdateDTO {
        private Integer resumeId;
        private Integer boardId;
        private Boolean status;
    }
}
