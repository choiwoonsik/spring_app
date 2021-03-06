package jpaBook.jpaShop.service;

import jpaBook.jpaShop.domain.Member;
import jpaBook.jpaShop.repository.MemberDaoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberDaoImpl memberDaoImpl;

	// 회원 가입
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member); // 중복 회원 검증 - email
		memberDaoImpl.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		// EXCEPTION
		List<Member> findMember = memberDaoImpl.findByEmail(member.getEmail());
		if (findMember.size() != 0) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	// 회원 전체 조회
	public List<Member> findAllMembers() {
		return memberDaoImpl.findAll();
	}

	// 회원 조회
	public Member findMember(Long memberId) {
		return memberDaoImpl.findOne(memberId);
	}
}
