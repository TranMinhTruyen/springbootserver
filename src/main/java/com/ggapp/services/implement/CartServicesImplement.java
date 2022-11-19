package com.ggapp.services.implement;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.utils.Utils;
import com.ggapp.common.utils.mapper.CartMapper;
import com.ggapp.dao.document.Cart;
import com.ggapp.dao.document.ListProduct;
import com.ggapp.dao.document.User;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.repository.mongo.CartRepository;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.services.CartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.commonenum.MessageError.CART_CREATED_ERROR;
import static com.ggapp.common.commonenum.MessageError.CART_NOT_FOUND;
import static com.ggapp.common.commonenum.MessageError.PRODUCT_NOT_FOUND;
import static com.ggapp.common.commonenum.MessageError.USER_NOT_FOUND;

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
public class CartServicesImplement implements CartServices {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private Utils utils;

    @Override
    public CartResponse createCart(int customerId, int productId, long productAmount) throws ApplicationException {
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<User> userResult = userRepository.findById(customerId);
        productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (productResult.get().getUnitInStock() > 0) {
            Cart newCart = new Cart();
            List<ListProduct> listProducts = new ArrayList<>();
            Product product = productResult.get();
            listProducts.add(new ListProduct(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    utils.getProductImage(product.getId()),
                    utils.calculatePrice(product),
                    product.getDiscount(),
                    checkProductAmount(product, productAmount)));
            product.setUnitInStock(productAmount > 1 ? (product.getUnitInStock() - productAmount) : (product.getUnitInStock() - 1));
            productRepository.save(product);
            newCart.setId(userResult.get().getId());
            newCart.setProductList(listProducts);
            newCart.setTotalPrice(utils.calculatePrice(product, productAmount));
            Cart cart = cartRepository.save(newCart);
            return getCartAfterUpdateOrCreate(cart);
        } else throw new ApplicationException(CART_CREATED_ERROR);


    }

    public CartResponse getCartAfterUpdateOrCreate(Cart cart) {
        return cartMapper.entityToResponse(cart);
    }

    @Override
    public CartResponse getCartById(int id) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(id);
        if (cartResult.isPresent()) {
            long totalAmount = 0;
            for (ListProduct listProduct : cartResult.get().getProductList()) {
                totalAmount = totalAmount + listProduct.getProductAmount();
            }
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCustomerId(cartResult.get().getId());
            cartResponse.setProductList(cartResult.get().getProductList());
            cartResponse.setTotalAmount(totalAmount);
            cartResponse.setTotalPrice(cartResult.get().getTotalPrice());
            return cartResponse;
        }
        return null;
    }

    @Override
    public CartResponse updateProductAmountInCart(int customerId, int productId, long amount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<User> userResult = userRepository.findById(customerId);
        Optional<Product> productResult = productRepository.findById(productId);
		User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
		Cart update = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
		Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        if (checkProductAmount(product, amount) > 0) {
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId() == productId) {
                    if (items.getProductAmount() > amount) {
						product.setUnitInStock(product.getUnitInStock() + (items.getProductAmount() - amount));
                    } else {
						product.setUnitInStock(product.getUnitInStock() - (amount - items.getProductAmount()));
                    }
                    items.setProductAmount(amount);
                    productRepository.save(product);
                    break;
                }
            }
            update.setProductList(list);
			update.setUpdateDate(LocalDateTime.now());
			update.setUpdateBy(user.getFirstName() + user.getLastName());
            update.setTotalPrice(utils.calculatePrice(list));
            Cart result = cartRepository.save(update);
            if (result.getProductList().isEmpty()) {
                return deleteCart(customerId);
            }
            return getCartAfterUpdateOrCreate(result);
        }
        return null;
    }

    @Override
    public CartResponse addProductToCart(int customerId, int productId, long productAmount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<User> userResult = userRepository.findById(customerId);
        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        List<ListProduct> productInCart = cart.getProductList();
        if (productInCart.stream().noneMatch(a -> a.getId() == product.getId())) {
            productInCart.add(new ListProduct(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    utils.getProductImage(product.getId()),
                    utils.calculatePrice(product),
                    product.getDiscount(),
                    checkProductAmount(product, productAmount)));
            product.setUnitInStock(product.getUnitInStock() - 1);
            productRepository.save(product);
            cart.setId(user.getId());
            cart.setProductList(productInCart);
            cart.setTotalPrice(utils.calculatePrice(productInCart));
            cartRepository.save(cart);
            return getCartAfterUpdateOrCreate(cart);
        } else {
            long amount = 0;
            for (ListProduct items : productInCart) {
                if (items.getId() == productResult.get().getId()) {
                    amount = items.getProductAmount() + productAmount;
                    break;
                }
            }
            return updateProductAmountInCart(customerId, productId, amount);
        }
    }

    @Override
    public CartResponse removeProductFromCart(int customerId, int productId) {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        if (cartResult.isPresent() && !cartResult.get().getProductList().isEmpty()) {
            Cart update = cartResult.get();
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId() == productId) {
                    returnProductFromCart(items.getId(), items.getProductAmount());
                    list.remove(items);
                    break;
                }
            }
            update.setTotalPrice(utils.calculatePrice(list));
            update.setProductList(list);
            cartRepository.save(update);
            if (cartResult.get().getProductList().isEmpty()) {
                return deleteCart(customerId);
            }
        }
        return null;
    }

    @Override
    public boolean isCartExists(int customerId) {
        return cartRepository.findById(customerId).isPresent();
    }

    @Override
    public CartResponse deleteCart(int id) {
        Optional<Cart> cartResult = cartRepository.findById(id);
        if (cartResult.isPresent()) {
            if (!cartResult.get().getProductList().isEmpty()) {
                cartResult.get().getProductList().forEach(items -> {
                    returnProductFromCart(items.getId(), items.getProductAmount());
                });
                cartRepository.deleteById(id);
                return getCartAfterUpdateOrCreate(cartResult.get());
            }
            cartRepository.deleteById(id);
            return getCartAfterUpdateOrCreate(cartResult.get());
        } else return null;
    }

    private void returnProductFromCart(int productId, long amount) {
        Optional<Product> update = productRepository.findById(productId);
        update.get().setUnitInStock(update.get().getUnitInStock() + amount);
        productRepository.save(update.get());
    }

    @Override
    public boolean deleteCartAfterCreateOrder(int id) {
        Optional<Cart> cartResult = cartRepository.findById(id);
        if (cartResult.isPresent()) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private long checkProductAmount(Product product, long amount) throws ApplicationException {
        if (product.getUnitInStock() < amount) {
            throw new ApplicationException("Maximum amount is: " + product.getUnitInStock(), HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            throw new ApplicationException("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        return amount;
    }
}
