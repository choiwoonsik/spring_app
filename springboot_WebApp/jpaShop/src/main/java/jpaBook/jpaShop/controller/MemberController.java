package jpaBook.jpaShop.controller;

import jpaBook.jpaShop.controller.form.MemberForm;
import jpaBook.jpaShop.domain.Address;
import jpaBook.jpaShop.domain.Member;
import jpaBook.jpaShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/members/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm());
		return "members/createMemberForm";
	}

	@PostMapping("/members/new")
	public String create(@Valid MemberForm form, BindingResult result) {

		if (result.hasErrors()) {
			return "members/createMemberForm";
		}

		Address address = new Address(form.getZipcode(), form.getStreetAdr(), form.getDetailAdr());
		Member member = new Member();
		member.setName(form.getName());
		member.setEmail(form.getEmail());
		member.setAddress(address);

		try {
			memberService.join(member);
		} catch (Exception IllegalStateException) {
			result.addError(new FieldError("member", "email", "중복된 사용자가 존재합니다."));
			if (result.hasErrors())
				return "members/createMemberForm";
		}

		return "redirect:/";
	}

	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findAllMembers();
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
