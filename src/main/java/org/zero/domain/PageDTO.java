package org.zero.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

	private int startPage;
	private int endPage;
	private boolean prev, next;
	
	private int total;
	private Criteria cri;
	
	public PageDTO(Criteria cri, int total) {
		
		this.cri = cri;
		this.total = total;
		
		// 페이징의 끝 번호 계산
		this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;
		// 화면에 10개를 보여준다고 가정하면 시작 번호는 무조건 끝 번호에서 9라는 값을 뺀 값
		this.startPage = this.endPage - 9;
		
		// 끝 번호(endPage)와 한 페이지당 출력되는 데이터 수(amount)의 곱이 전체 데이터 수(total)보다 크면 
		// 끝 번호(endPage)는 total을 이용해서 다시 계산
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		// 이전(prev)의 경우 시작 번호(startPage)가 1보다 큰 경우라면 존재
		this.prev = this.startPage > 1;
		// realEnd가 끝 번호(endPage)보다 큰 경우에만 존재
		this.next = this.endPage < realEnd;
		
	}
}
