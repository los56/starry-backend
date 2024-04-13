package team.ubox.starry.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.repository.entity.CommunityPost;
import team.ubox.starry.repository.entity.CommunityReply;
import team.ubox.starry.repository.entity.CustomUserDetail;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.repository.CommunityPostRepository;
import team.ubox.starry.repository.CommunityReplyRepository;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.service.dto.community.PostDTO;
import team.ubox.starry.service.dto.community.ReplyDTO;
import team.ubox.starry.service.dto.community.ResponseCommunityUserDTO;
import team.ubox.starry.helper.AuthHelper;
import team.ubox.starry.helper.UUIDHelper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;
    private final CommunityReplyRepository communityReplyRepository;

    public PostDTO.Response writePost(@Valid PostDTO.RequestWrite dto) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.save(CommunityPost.builder().writer(userDetail).title(dto.getTitle())
                .content(dto.getContent()).writeDate(Timestamp.from(Instant.now())).build());

        return PostDTO.Response.from(post);
    }

    public PostDTO.Response editPost(@Valid PostDTO.RequestEdit dto) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.findById(dto.getIndex()).orElseThrow(() -> new IllegalStateException("존재하지 않는 글"));
        if(!post.getWriter().getId().equals(userDetail.getId())) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        post.update(dto.getTitle(), dto.getContent());

        return PostDTO.Response.from(post);
    }

    public PostDTO.Response deletePost(Integer index) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityPost post = communityPostRepository.deleteByIndexAndWriterId(index, userDetail.getId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 글이거나 권한이 없습니다."));

        return PostDTO.Response.from(post);
    }

    public PostDTO.ResponseList viewPost (PostDTO.RequestList dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage() - 1, dto.getPostPerPage());
        if(dto.getIsAscending()) {
            pageRequest.withSort(Sort.Direction.ASC, "index");
        } else {
            pageRequest.withSort(Sort.Direction.DESC, "index");
        }

        UUID writerId = UUIDHelper.stringToUUID(dto.getId());
        User writer = userRepository.findById(writerId).orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        Page<CommunityPost> page = communityPostRepository.findSliceByWriterId(writerId, pageRequest);
        List<PostDTO.Response> postList = page.stream().map(PostDTO.Response::from).toList();
        postList.forEach(p -> p.setWriterData(ResponseCommunityUserDTO.from(writer)));

        PostDTO.ResponseList postListDTO = new PostDTO.ResponseList();
        postListDTO.setPosts(postList);
        postListDTO.setPage(dto.getPage());
        postListDTO.setAllPageCount(page.getTotalPages());
        postListDTO.setAllPostsCount(page.getTotalElements());

        return postListDTO;
    }

    public ReplyDTO.Response writeReply(@Valid ReplyDTO.RequestWrite dto) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        User writer = userRepository.findById(userDetail.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));

        CommunityPost post = communityPostRepository.findById(dto.getPostIndex()).orElseThrow(() -> new IllegalStateException("존재하지 않는 글입니다."));
        CommunityReply reply = communityReplyRepository.save(
                CommunityReply.builder()
                        .post(post)
                        .writer(writer)
                        .content(dto.getContent())
                        .writeDate(Timestamp.from(Instant.now()))
                        .build());

        ReplyDTO.Response responseReplyDTO = new ReplyDTO.Response();
        responseReplyDTO.setPostIndex(post.getIndex());
        responseReplyDTO.setWriter(writer.getIdString());
        responseReplyDTO.setContent(reply.getContent());
        responseReplyDTO.setWriteDate(reply.getWriteDate());

        return responseReplyDTO;
    }

    public ReplyDTO.Response deleteReply(Integer index) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        CommunityReply reply = communityReplyRepository.deleteByIndexAndWriterId(index, userDetail.getId()).orElseThrow(() -> new IllegalStateException("권한이 없습니다."));

        ReplyDTO.Response responseReplyDTO = new ReplyDTO.Response();
        responseReplyDTO.setPostIndex(reply.getPost().getIndex());
        responseReplyDTO.setWriter(UUIDHelper.UUIDToString(userDetail.getId()));
        responseReplyDTO.setContent(reply.getContent());
        responseReplyDTO.setWriteDate(reply.getWriteDate());

        return responseReplyDTO;
    }
}
