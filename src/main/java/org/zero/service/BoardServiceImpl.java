package org.zero.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zero.domain.BoardAttachVO;
import org.zero.domain.BoardVO;
import org.zero.domain.Criteria;
import org.zero.mapper.BoardAttachMapper;
import org.zero.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service // 비즈니스 영역을 담당하는 객체임을 표시하기 위해 사용
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board) {

		log.info("register......" + board);
		mapper.insertSelectKey(board);

		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}

		board.getAttachList().forEach(attach -> {

			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	@Override
	public BoardVO get(Long bno) {

		log.info("get........." + bno);
		
		return mapper.read(bno);
	}
	
	// 게시물 수정은 기존 첨부파일 관련 데이터 삭제한 후 다시 첨부파일 데이터 추가하는 방식으로 동작
	@Transactional
	@Override
	public boolean modify(BoardVO board) {

		log.info("modify........." + board);
		
		attachMapper.deleteAll(board.getBno());
		boolean modifyResult = mapper.update(board) == 1;
		
		if( modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0 ){
			
			board.getAttachList().forEach(attach -> {
				
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return modifyResult;
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {

		log.info("remove........." + bno);
		
		attachMapper.deleteAll(bno);
		return mapper.delete(bno) == 1;
	}

//	@Override
//	public List<BoardVO> getList() {
//		
//		log.info("getList...............");
//		
//		return mapper.getList();
//	}
	
	@Override
	public List<BoardVO> getList(Criteria cri) {
		
		log.info("get List with criteria: " + cri);
		
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {

		log.info("get total count");
		return mapper.getTotalCount(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno){
		
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
}
