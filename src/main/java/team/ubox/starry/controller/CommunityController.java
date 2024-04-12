package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.CustomResponse;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.service.CommunityService;
import team.ubox.starry.service.dto.community.PostDTO;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/post/write")
    public CustomResponse<PostDTO.Response> writePost(@Valid @RequestBody PostDTO.RequestWrite dto) {
        return new CustomResponse<>(communityService.writePost(dto));
    }

    @GetMapping("/post")
    public CustomResponse<PostDTO.ResponseList> getPostDetail(@RequestParam String id, @RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "30") Integer count, @RequestParam(defaultValue = "false") Boolean isAscending) {
        PostDTO.RequestList dtoForFind = new PostDTO.RequestList();
        dtoForFind.setId(id);
        dtoForFind.setPage(page);
        dtoForFind.setPostPerPage(count);
        dtoForFind.setIsAscending(isAscending);

        return new CustomResponse<>(communityService.viewPost(dtoForFind));
    }

    @DeleteMapping("/post")
    public CustomResponse<PostDTO.Response> deletePost(@RequestParam Integer index) {
        if(index < 1) {
            throw new CustomException(CustomError.NEGATIVE_INDEX);
        }

        return null;
    }


}
