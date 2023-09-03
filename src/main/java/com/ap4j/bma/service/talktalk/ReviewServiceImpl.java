//package com.ap4j.bma.service.talktalk;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ReviewServiceImpl implements ReviewService {
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Override
//    public boolean registerBoard(BoardDTO params) {
//
//        int queryResult =0;
//        if(params.getIdx()==null) {
//            queryResult= boardMapper.insertBoard(params);
//        } else {
//            queryResult = boardMapper.updateBoard(params);
//        }
//        return (queryResult==1)?true:false;
//    }
//
//    @Override
//    public BoardDTO getBoardDetail(Long idx) {
//        return boardMapper.selectBoardDetail(idx);
//    }
//
//    @Override
//    public boolean deleteBoard(Long idx) {
//        int queryResult=0;
//        BoardDTO board = boardMapper.selectBoardDetail(idx);
//
//        if(board!=null && "N".equals(board.getDeleteYn())){
//            queryResult = boardMapper.deleteBoard(idx);
//        }
//        return (queryResult==1)?true:false;
//    }
//
//    @Override
//    public List<BoardDTO> getBoardList(){
//        List<BoardDTO> boardList = Collections.emptyList();
//        int boardTotalCount = boardMapper.selectBoardTotalCount();
//        if(boardTotalCount>0) {
//            boardList= boardMapper.selectBoardList();
//        }
//
//        return boardList;
//    }
//
//
//    @Override
//    public boolean cntPlus(Long idx) {
//        return boardMapper.cntPlus(idx);
//    }
//
//    @Override
//    public Page<Board> findBoardList(Pageable pageable) {
//        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
//                pageable.getPageSize());
//        return boardRepository.findAll(pageable);
//    }
//
//    @Override
//    public Board findBoardByIdx(Long idx) {
//        return boardRepository.findById(idx).orElse(new Board());
//    }
//}