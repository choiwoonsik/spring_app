package jpaBook.jpaShop.service;

import jpaBook.jpaShop.domain.Member;
import jpaBook.jpaShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	// 회원 가입
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member); // 중복 회원 검증 - email
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		// EXCEPTION
		List<Member> findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember.size() != 0) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	// 회원 전체 조회
	public List<Member> findAllMembers() {
		return memberRepository.findAll();
	}

	// 회원 조회
	public Member findMember(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	@Transactional
	public void update(Long memberId, String name) {
		Member member = memberRepository.findOne(memberId);
		member.setName(name);
	}
}
