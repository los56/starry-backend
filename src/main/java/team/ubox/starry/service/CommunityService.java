package team.ubox.starry.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.community.*;
import team.ubox.starry.entity.CommunityPost;
import team.ubox.starry.entity.CommunityReply;
import team.ubox.starry.entity.User;
import team.ubox.starry.repository.CommunityPostRepository;
import team.ubox.starry.repository.CommunityReplyRepository;
import team.ubox.starry.util.AuthUtil;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityPostRepository communityPostRepository;
    private final CommunityReplyRepository communityReplyRepository;

    public ResponsePostDTO writePost(@Valid RequestWritePostDTO dto) {
        User writer = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.save(CommunityPost.builder().writer(writer).title(dto.getTitle())
                .content(dto.getContent()).writeDate(Timestamp.from(Instant.now())).build());

        return ResponsePostDTO.from(post);
    }

    public ResponsePostDTO editPost(@Valid RequestEditPostDTO dto) {
        User writer = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.findById(dto.getIndex()).orElseThrow(() -> new IllegalStateException("존재하지 않는 글"));
        if(!post.getWriter().getId().equals(writer.getId())) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        post.update(dto.getTitle(), dto.getContent());

        return ResponsePostDTO.from(post);
    }

    public ResponsePostDTO deletePost(Integer index) {
        User writer = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.deleteByIndexAndWriterId(index, writer.getId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 글이거나 권한이 없습니다."));

        return ResponsePostDTO.from(post);
    }

    public ResponsePostListDTO viewPost (RequestPostListDTO dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage() - 1, dto.getPostPerPage());
        if(dto.getIsAscending()) {
            pageRequest.withSort(Sort.Direction.ASC, "index");
        } else {
            pageRequest.withSort(Sort.Direction.DESC, "index");
        }

        UUID writerId = UUIDUtil.stringToUUID(dto.getId());

        Page<CommunityPost> page = communityPostRepository.findSliceByWriterId(writerId, pageRequest);
        List<ResponsePostDTO> postList = page.stream().map(ResponsePostDTO::from).toList();

        ResponsePostListDTO postListDTO = new ResponsePostListDTO();
        postListDTO.setPosts(postList);
        postListDTO.setPage(dto.getPage());
        postListDTO.setAllPageCount(page.getTotalPages());
        postListDTO.setAllPostsCount(page.getTotalElements());

        return postListDTO;
    }

    public ResponseReplyDTO writeReply(@Valid RequestWriteReplyDTO dto) {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.findById(dto.getPostIndex()).orElseThrow(() -> new IllegalStateException("존재하지 않는 글입니다."));
        CommunityReply reply = communityReplyRepository.save(
                CommunityReply.builder()
                        .post(post)
                        .writer(authUser)
                        .content(dto.getContent())
                        .writeDate(Timestamp.from(Instant.now()))
                        .build());

        ResponseReplyDTO responseReplyDTO = new ResponseReplyDTO();
        responseReplyDTO.setPostIndex(post.getIndex());
        responseReplyDTO.setWriter(authUser.getIdString());
        responseReplyDTO.setContent(reply.getContent());
        responseReplyDTO.setWriteDate(reply.getWriteDate());

        return responseReplyDTO;
    }

    public ResponseReplyDTO deleteReply(Integer index) {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityReply reply = communityReplyRepository.deleteByIndexAndWriterId(index, authUser.getId()).orElseThrow(() -> new IllegalStateException("권한이 없습니다."));

        ResponseReplyDTO responseReplyDTO = new ResponseReplyDTO();
        responseReplyDTO.setPostIndex(reply.getPost().getIndex());
        responseReplyDTO.setWriter(authUser.getIdString());
        responseReplyDTO.setContent(reply.getContent());
        responseReplyDTO.setWriteDate(reply.getWriteDate());

        return responseReplyDTO;
    }
}
