package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.StarryResponse;
import team.ubox.starry.dto.community.*;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.CommunityService;

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
    public StarryResponse<PostDTO.ResponseList> viewPost(@RequestParam String id, @RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "30") Integer count, @RequestParam(defaultValue = "false") Boolean isAscending) {
        PostDTO.RequestList requestDto = new PostDTO.RequestList();
        requestDto.setId(id);
        requestDto.setPage(page);
        requestDto.setPostPerPage(count);
        requestDto.setIsAscending(isAscending);

        return new StarryResponse<>(communityService.viewPost(requestDto));
    }

    @DeleteMapping("/post")
    public StarryResponse<PostDTO.Response> deletePost(@RequestParam Integer index) {
        if(index < 1) {
            throw new StarryException(StarryError.NEGATIVE_INDEX);
        }

        return null;
    }


}
