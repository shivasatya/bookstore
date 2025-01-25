package com.order.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.order.entity.OrderEntity;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	@Query("SELECT o FROM OrderEntity o WHERE o.id = :id")
	Optional<OrderEntity> findById(@Param("id") Long id);


}
