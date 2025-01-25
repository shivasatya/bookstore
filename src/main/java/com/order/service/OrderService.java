package com.order.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.dto.OrderItemRequestDTO;
import com.order.dto.OrderItemResponseDTO;
import com.order.dto.OrderRequestDTO;
import com.order.dto.OrderResponseDTO;
import com.order.entity.OrderEntity;
import com.order.entity.OrderItem;
import com.order.repo.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
 // Create a new order
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(orderRequestDTO.getUserId());
        orderEntity.setItems(orderRequestDTO.getItems().stream().map(this::convertToOrderItem).collect(Collectors.toList()));

        // Calculate totalAmount
        double total = orderEntity.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        orderEntity.setTotalAmount(total);
        orderEntity.setStatus("PROCESSING");
        orderEntity.setOrderDate(LocalDateTime.now());

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        return convertToOrderResponseDTO(savedOrder);
    }

    // Update an existing order
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Optional<OrderEntity> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isPresent()) {
            OrderEntity existingOrder = existingOrderOpt.get();
            existingOrder.setUserId(orderRequestDTO.getUserId());
            existingOrder.setItems(orderRequestDTO.getItems().stream().map(this::convertToOrderItem).collect(Collectors.toList()));

            // Recalculate totalAmount
            double total = existingOrder.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            existingOrder.setTotalAmount(total);

            existingOrder.setStatus("PROCESSING");
            OrderEntity updatedOrder = orderRepository.save(existingOrder);

            return convertToOrderResponseDTO(updatedOrder);
        }
        return null; // or throw an exception if not found
    }

    // Get all orders
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToOrderResponseDTO).collect(Collectors.toList());
    }

    // Get order by ID
    public OrderResponseDTO getOrderById(Long id) {
        Optional<OrderEntity> orderEntityOpt = orderRepository.findById(id);
        return orderEntityOpt.map(this::convertToOrderResponseDTO).orElse(null);
    }

    // Convert OrderEntity to OrderResponseDTO
    private OrderResponseDTO convertToOrderResponseDTO(OrderEntity orderEntity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(orderEntity.getId());
        responseDTO.setUserId(orderEntity.getUserId());
        responseDTO.setOrderDate(orderEntity.getOrderDate());
        responseDTO.setStatus(orderEntity.getStatus());
        responseDTO.setTotalAmount(orderEntity.getTotalAmount());
        responseDTO.setItems(orderEntity.getItems().stream().map(this::convertToOrderItemResponseDTO).collect(Collectors.toList()));
        return responseDTO;
    }

    // Convert OrderItem to OrderItemResponseDTO
    private OrderItemResponseDTO convertToOrderItemResponseDTO(OrderItem orderItem) {
        OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
        itemDTO.setBookId(orderItem.getBookId());
        itemDTO.setQuantity(orderItem.getQuantity());
        itemDTO.setPrice(orderItem.getPrice());
        return itemDTO;
    }

    // Convert OrderItemRequestDTO to OrderItem
    private OrderItem convertToOrderItem(OrderItemRequestDTO itemDTO) {
        OrderItem item = new OrderItem();
        item.setBookId(itemDTO.getBookId());
        item.setQuantity(itemDTO.getQuantity());
        item.setPrice(itemDTO.getPrice());
        return item;
    }
}
