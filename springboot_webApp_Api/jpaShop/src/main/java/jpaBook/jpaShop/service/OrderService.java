package jpaBook.jpaShop.service;

import jpaBook.jpaShop.domain.Delivery;
import jpaBook.jpaShop.domain.Member;
import jpaBook.jpaShop.domain.Order;
import jpaBook.jpaShop.domain.OrderItem;
import jpaBook.jpaShop.domain.item.Item;
import jpaBook.jpaShop.repository.ItemRepository;
import jpaBook.jpaShop.repository.MemberRepository;
import jpaBook.jpaShop.repository.OrderRepository;
import jpaBook.jpaShop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;
	private final EntityManager em;

	/**
	 * 주문
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {

		// Entity 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		// 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		// 주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		orderRepository.save(order);

		return order.getId();
	}

	/**
	 * 취소
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		// 주문 취소
		order.cancel();
	}

	public List<Order> findAllByString(OrderSearch orderSearch) {
		//language=JPAQL
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}
		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
				.setMaxResults(1000); //최대 1000건
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();
	}

	/**
	 * 검색 - 동적 쿼리 활용
	 */
//	public List<Order> findOrders(OrderSearch orderSearch) {
//		orderRepository
//	}
}
