package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.CommunityService;
import team.ubox.starry.service.dto.community.PostDTO;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/post/write")
    public StarryResponse<PostDTO.Response> writePost(@Valid @RequestBody PostDTO.RequestWrite dto) {
        return new StarryResponse<>(communityService.writePost(dto));
    }

    @GetMapping("/post")
    public StarryResponse<PostDTO.ResponseList> getPostDetail(@RequestParam String id, @RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "30") Integer count, @RequestParam(defaultValue = "false") Boolean isAscending) {
        PostDTO.RequestList dtoForFind = new PostDTO.RequestList();
        dtoForFind.setId(id);
        dtoForFind.setPage(page);
        dtoForFind.setPostPerPage(count);
        dtoForFind.setIsAscending(isAscending);

        return new StarryResponse<>(communityService.viewPost(dtoForFind));
    }

    @DeleteMapping("/post")
    public StarryResponse<PostDTO.Response> deletePost(@RequestParam Integer index) {
        if(index < 1) {
            throw new StarryException(StarryError.NEGATIVE_INDEX);
        }

        return null;
    }


}
