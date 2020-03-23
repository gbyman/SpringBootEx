package org.zero.mapper;

import java.util.List;

import org.zero.domain.BoardAttachVO;

public interface BoardAttachMapper {

	public void insert(BoardAttachVO vo);
	
	public void delete(String uuid);
	
	public List<BoardAttachVO> findByBno(Long bno);
	
	public void deleteAll(Long bno);
	
	// 첨부파일 목록
	public List<BoardAttachVO> getOldFiles();
}
