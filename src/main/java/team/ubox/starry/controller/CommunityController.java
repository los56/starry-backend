package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.community.RequestPostListDTO;
import team.ubox.starry.dto.community.RequestWritePostDTO;
import team.ubox.starry.dto.community.ResponsePostDTO;
import team.ubox.starry.dto.community.ResponsePostListDTO;
import team.ubox.starry.service.CommunityService;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/post/write")
    public ResponseEntity<ResponsePostDTO> writePost(@Valid @RequestBody RequestWritePostDTO dto) {
        return ResponseEntity.ok(communityService.writePost(dto));
    }

    @GetMapping("/post")
    public ResponseEntity<ResponsePostListDTO> viewPost(@RequestParam String id, @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "30") Integer count, @RequestParam(defaultValue = "false") Boolean isAscending) {
        RequestPostListDTO requestDto = new RequestPostListDTO();
        requestDto.setId(id);
        requestDto.setPage(page);
        requestDto.setPostPerPage(count);
        requestDto.setIsAscending(isAscending);

        return ResponseEntity.ok(communityService.viewPost(requestDto));
    }

    @DeleteMapping("/post")
    public ResponseEntity<ResponsePostDTO> deletePost(@RequestParam Integer index) {
        if(index < 1) {
            throw new IllegalArgumentException("index가 잘못되었습니다.");
        }

        return null;
    }


}
