package com.ggapp.services.implement;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.utils.Constant;
import com.ggapp.dao.document.AutoIncrement;
import com.ggapp.dao.document.Cart;
import com.ggapp.dao.document.ListProduct;
import com.ggapp.dao.document.Order;
import com.ggapp.dao.document.User;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.repository.mongo.CartRepository;
import com.ggapp.dao.repository.mongo.OrderRepository;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.services.CartService;
import com.ggapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.CART_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.ORDER_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.ORDER_NOT_FOUND_PRODUCT;
import static com.ggapp.common.enums.MessageResponse.USER_NOT_FOUND;

/**
 * @author Tran Minh Truyen
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE = 200
 */

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Override
    public OrderResponse createOrderByCart(int customerId) throws ApplicationException {
        List<Order> last = new AutoIncrement(orderRepository).getLastOfCollection();
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<User> userResult = userRepository.findById(customerId);
        cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (userResult.isPresent() && cartResult.isPresent()) {
            Order newOrder = new Order();
            if (last != null)
                newOrder.setId(last.get(0).getId() + 1);
            else newOrder.setId(1);
            newOrder.setCustomerId(userResult.get().getId());
            newOrder.setCreateDate(new Date());
            newOrder.setListProducts(cartResult.get().getProductList());
            newOrder.setStatus(Constant.NEW);
            newOrder.setTotalPrice(cartResult.get().getTotalPrice());
            StringBuilder stringBuilder = new StringBuilder(userResult.get().getAddress());
            stringBuilder.append(", ");
            stringBuilder.append(userResult.get().getDistrict());
            stringBuilder.append(", ");
            stringBuilder.append(userResult.get().getCity());
            newOrder.setAddress(stringBuilder.toString());
            Order order = orderRepository.save(newOrder);
            cartService.deleteCartAfterCreateOrder(cartResult.get().getId());
            return getOrderAfterCreateOrUpdate(order);
        } else return null;
    }

    @Override
    public OrderResponse createOrderByProductId(int customerId, int[] productId) throws ApplicationException {
        List<Order> last = new AutoIncrement(orderRepository).getLastOfCollection();
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<User> userResult = userRepository.findById(customerId);
        if (userResult.isPresent() && cartResult.isPresent() && productId != null) {
            Order newOrder = new Order();
            BigDecimal totalPrice = new BigDecimal(0);
            List<ListProduct> productList = new ArrayList<>();
            for (ListProduct product : cartResult.get().getProductList()) {
                for (int id : productId) {
                    if (product.getId() == id) {
                        productList.add(product);
                        totalPrice = totalPrice.add(product.getPriceAfterDiscount()
                                .multiply(BigDecimal.valueOf(product.getProductAmount())));
                        cartService.removeProductFromCart(customerId, product.getId());
                    }
                }
            }
            if (!productList.isEmpty()) {
                if (last != null)
                    newOrder.setId(last.get(0).getId() + 1);
                else newOrder.setId(1);
                newOrder.setCustomerId(userResult.get().getId());
                newOrder.setCreateDate(new Date());
                newOrder.setListProducts(productList);
                newOrder.setTotalPrice(totalPrice);
                newOrder.setStatus(Constant.NEW);
                StringBuilder stringBuilder = new StringBuilder(userResult.get().getAddress());
                stringBuilder.append(", ");
                stringBuilder.append(userResult.get().getDistrict());
                stringBuilder.append(", ");
                stringBuilder.append(userResult.get().getCity());
                newOrder.setAddress(stringBuilder.toString());
                Order order = orderRepository.save(newOrder);
                return getOrderAfterCreateOrUpdate(order);
            } else {
                throw new ApplicationException(ORDER_NOT_FOUND_PRODUCT);
            }
        } else return null;
    }


    private OrderResponse getOrderAfterCreateOrUpdate(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setCustomerId(order.getCustomerId());
        orderResponse.setEmployeeId(order.getEmployeeId());
        orderResponse.setCreateDate(order.getCreateDate());
        orderResponse.setListProducts(order.getListProducts());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setAddress(order.getAddress());
        return orderResponse;
    }

    @Override
    public CommonResponse getOrderByCustomerId(int page, int size, int id) {
        List<Order> result = orderRepository.findOrderByCustomerId(id);
        if (result != null) {
            return new CommonResponse().getCommonResponse(page, size, result);
        } else return null;
    }

    @Override
    @Transactional
    public boolean updateOrder(int id, OrderRequest orderRequest) throws ApplicationException {
        Optional<Order> order = orderRepository.findById(id);
        order.orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        if (order.isPresent()) {
            Order update = order.get();
            update.setEmployeeId(orderRequest.getEmployeeId());
            update.setStatus(orderRequest.getStatus());
            update.setAddress(orderRequest.getAddress());
            orderRepository.save(update);
            return true;
        } else return false;
    }

    @Override
    public boolean deleteOrder(int id, int customerId) throws ApplicationException {
        Optional<Order> order = orderRepository.findOrderByIdAndCustomerId(id, customerId);
        order.orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        for (ListProduct listProduct : order.get().getListProducts()) {
            returnProductFromOrder(listProduct.getId(), listProduct.getProductAmount());
        }
        orderRepository.deleteById(order.get().getId());
        return true;
    }

    private void returnProductFromOrder(int productId, long amount) {
        Optional<Product> update = productRepository.findById(productId);
        update.get().setUnitInStock(update.get().getUnitInStock() + amount);
        productRepository.save(update.get());
    }
}
