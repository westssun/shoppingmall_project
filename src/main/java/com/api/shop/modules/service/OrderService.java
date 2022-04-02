package com.api.shop.modules.service;

import com.api.shop.common.exception.BadRequestException;
import com.api.shop.modules.entity.*;
import com.api.shop.modules.form.OrderAddForm;
import com.api.shop.modules.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 회원의 주문 리스트 조회
     * @param memberIdx
     * @return
     */
    @Transactional
    public List<Order> getOrderListOfMember(long memberIdx) {
        Member member = memberService.getMember(memberIdx);

        if (member == null) {
            throw new BadRequestException("존재하지 않는 회원입니다.");
        }

        return member.getOrderList();
    }

    /**
     * 주문 등록
     * @param orderAddForm
     * @return
     */
    public Long addOrder(OrderAddForm orderAddForm) {
        Member member = memberService.getMember(orderAddForm.getMemberIdx());
        Item item = itemService.getItem(orderAddForm.getItemIdx());
        Address address = orderAddForm.getAddress();

        OrderItem orderItem = OrderItem. createOrderItem(item, item.getPrice(), orderAddForm.getItemCount());
        Order order = Order.createOrder(member, address, orderItem);

        orderRepository.save(order);
        return order.getIdx();
    }
}