package jpaBook.jpaShop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpaBook.jpaShop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice;

	private int count;

	//==생성 메서드==/
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setCount(count);
		orderItem.setOrderPrice(orderPrice);

		item.removeStock(orderItem.count);
		return orderItem;
	}

	//==비지니스 로직==//
	/*
	주문 취소
	 */
	public void cancel() {
		getItem().addStock(count);
	}

	/*
	총 주문 비용
	 */
	public long getTotalPrice() {
		return (long) getCount() * getOrderPrice();
	}
}
