package com.order.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.order.dto.OrderRequestDTO;
import com.order.dto.OrderResponseDTO;
import com.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create a new order
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /// dfndh reoi oihfioh ioefiow
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.createOrder(orderRequestDTO);
    }

    // Update an existing order
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.updateOrder(id, orderRequestDTO);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Get all orders
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }
}
