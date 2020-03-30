package org.zero.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zero.domain.MemberVO;

import lombok.Getter;

@Getter
public class CustomUser extends User{
// CustomUser는 User 클래스를 상속하기 때문에 부모 클래스의 생성자를 호출해야만 정상적인 객체를 생성할 수 있다.
// MemberVO를 파라미터로 전달해서 User클래스에 맞게 생성자를 호출해야 한다.
// 이 과정에서 AuthVO 인스턴스는 GrantedAuthority 객체로 변환해야 하므로 stream()과 map()을 이용해서 처리한다
	
	private static final long serialVersionUID = 1L;
	
	private MemberVO member;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
	}
	
	public CustomUser(MemberVO vo) {
		super(vo.getUserid(), vo.getUserpw(), vo.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuth()))
				.collect(Collectors.toList()));
		
		this.member = vo;
	}

}
