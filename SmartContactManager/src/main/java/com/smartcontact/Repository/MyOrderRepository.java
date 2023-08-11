package com.smartcontact.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcontact.Entities.OrdersDetails;

public interface MyOrderRepository extends JpaRepository<OrdersDetails, Long> {

	public OrdersDetails findByOrderId(String orderId);
}
