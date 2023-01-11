package com.ggapp.services.implement;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.request.UserOrderRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.Constant;
import com.ggapp.common.utils.mapper.OrderMapper;
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
import com.ggapp.dao.repository.mysql.StoreRepository;
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
 * TOTAL_HOURS_WASTED_HERE = 204
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

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CustomUserDetail customUserDetail, UserOrderRequest userOrderRequest)
            throws ApplicationException {
        OrderResponse orderResponse = new OrderResponse();
        if (userOrderRequest.getProductId() != null && userOrderRequest.getProductId().length > 0) {
            orderResponse = createOrderByProductId(customUserDetail, userOrderRequest.getProductId(),
                    userOrderRequest.getStoreId());
        } else {
            orderResponse = createOrderByCart(customUserDetail);
        }
        return orderResponse;
    }

    private OrderResponse createOrderByCart(CustomUserDetail customUserDetail) throws ApplicationException {
        List<Order> last = new AutoIncrement(orderRepository).getLastOfCollection();
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<User> userResult = userRepository.findById(customUserDetail.getAccountDetail().getOwnerId());

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));

        Order newOrder = new Order();
        if (last != null)
            newOrder.setId(last.get(0).getId() + 1);
        else newOrder.setId(1);
        newOrder.setCustomerId(user.getId());
        newOrder.setCreateDate(new Date());
        newOrder.setListProducts(cart.getProductList());
        newOrder.setStatus(Constant.NEW);
        newOrder.setTotalPrice(cart.getTotalPrice());
        StringBuilder stringBuilder = new StringBuilder(user.getAddress());
        stringBuilder.append(", ");
        stringBuilder.append(user.getDistrict());
        stringBuilder.append(", ");
        stringBuilder.append(user.getCity());
        newOrder.setAddress(stringBuilder.toString());
        Order order = orderRepository.save(newOrder);
        cartService.deleteCartAfterCreateOrder(cart.getId());
        return getOrderAfterCreateOrUpdate(order);
    }

    private OrderResponse createOrderByProductId(CustomUserDetail customUserDetail, int[] productId, int storeId)
            throws ApplicationException {

        List<Order> last = new AutoIncrement(orderRepository).getLastOfCollection();

        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<User> userResult = userRepository.findById(customUserDetail.getAccountDetail().getOwnerId());

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));

        if (productId != null && productId.length > 0) {
            Order newOrder = new Order();
            BigDecimal totalPrice = new BigDecimal(0);
            List<ListProduct> productList = new ArrayList<>();
            for (ListProduct product : cart.getProductList()) {
                for (int id : productId) {
                    if (product.getId() == id) {
                        productList.add(product);
                        totalPrice = totalPrice.add(product.getPriceAfterDiscount()
                                .multiply(BigDecimal.valueOf(product.getProductAmount())));
                        cartService.removeSingleProductFromCart(customUserDetail, product.getId());
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
    public CommonResponsePayload getOrderByCustomerId(CustomUserDetail customUserDetail, int page, int size) {
        List<Order> result = orderRepository.findOrderByCustomerId(customUserDetail.getAccountDetail().getOwnerId());
        if (result != null) {
            return new CommonResponsePayload().getCommonResponse(page, size, result);
        } else return null;
    }

    @Override
    @Transactional
    public OrderResponse updateOrderByEmp(int id, CustomUserDetail customUserDetail, OrderRequest orderRequest) throws ApplicationException {
        Optional<Order> orderResult = orderRepository.findById(id);
        Order update = orderResult.orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        update.setEmployeeId(customUserDetail.getAccountDetail().getOwnerId());
        update.setStatus(orderRequest.getStatus());
        update.setAddress(orderRequest.getAddress());
        Order result = orderRepository.save(update);
        return orderMapper.entityToResponse(result);
    }

    @Override
    public OrderResponse updateOrderByUser(CustomUserDetail customUserDetail, UserOrderRequest userOrderRequest) throws ApplicationException {
        Optional<Order> orderResult = orderRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Order update = orderResult.orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        update.setAddress(userOrderRequest.getAddress());
        Order result = orderRepository.save(update);
        return orderMapper.entityToResponse(result);
    }

    @Override
    public void deleteOrder(CustomUserDetail customUserDetail, int id) throws ApplicationException {
        Optional<Order> orderResult = orderRepository.findOrderByIdAndCustomerId(id, customUserDetail.getAccountDetail().getOwnerId());
        Order order = orderResult.orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        for (ListProduct listProduct : order.getListProducts()) {
            returnProductFromOrder(listProduct.getId(), listProduct.getProductAmount());
        }
        orderRepository.deleteById(order.getId());
    }

    private void returnProductFromOrder(int productId, long amount) {
        Optional<Product> update = productRepository.findById(productId);
        productRepository.save(update.get());
    }
}
