//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReplyDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class ReplyService {
//
//    @Autowired
//    private final ReplyRepository replyRepository;
//
//
//    /**
//     * 게시글 저장
//     *
//     * @param params - 게시글 정보
//     * @return Generated PK
//     */
//    @Transactional
//    public Long savePost(final PostRequest params) {
//        postMapper.save(params);
//        return params.getId();
//    }
//
//    /**
//     * 게시글 상세정보 조회
//     *
//     * @param id - PK
//     * @return 게시글 상세정보
//     */
//    public PostResponse findPostById(final Long id) {
//        return postMapper.findById(id);
//    }
//
//    /**
//     * 게시글 수정
//     *
//     * @param params - 게시글 정보
//     * @return PK
//     */
//    @Transactional
//    public Long updatePost(final PostRequest params) {
//        postMapper.update(params);
//        return params.getId();
//    }
//
//    /**
//     * 게시글 삭제
//     *
//     * @param id - PK
//     * @return PK
//     */
//    public Long deletePost(final Long id) {
//        postMapper.deleteById(id);
//        return id;
//    }
//
//    /**
//     * 게시글 리스트 조회
//     *
//     * @return 게시글 리스트
//     */
//    public List<TalkTalkReplyDto> getAllReplysByReview(int board_no) {
//        List<Reply> replys = replyRepository.findByBoard_no(board_no);
//        return replys.stream()
//                .map(reply -> new TalkTalkReplyDto(reply.getReply_no(), reply.get()))
//                .collect(Collectors.toList());
//    }
//
//    /* CREATE */
//    @Transactional
//    public Long commentSave(String nickname, Long id, CommentRequestDto dto) {
//        User user = userRepository.findByNickname(nickname);
//        osts posts = postsRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));
//
//        dto.setUser(user);
//        dto.setPosts(posts);
//
//        Comment comment = dto.toEntity();
//        commentRepository.save(comment);
//
//        return dto.getId();
//    }
//}