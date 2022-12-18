package com.ggapp.services.implement;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.utils.CommonUtils;
import com.ggapp.common.utils.mapper.CartMapper;
import com.ggapp.dao.document.Cart;
import com.ggapp.dao.document.ListProduct;
import com.ggapp.dao.document.User;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.entity.ProductStore;
import com.ggapp.dao.entity.Store;
import com.ggapp.dao.repository.mongo.CartRepository;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.dao.repository.mysql.ProductStoreRepository;
import com.ggapp.dao.repository.mysql.StoreRepository;
import com.ggapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.CART_CREATED_ERROR;
import static com.ggapp.common.enums.MessageResponse.CART_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_IN_STORE_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_IS_OUT_OF_STOCK;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.STORE_NOT_FOUND;
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
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductStoreRepository productStoreRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public CartResponse createCart(Long customerId, Long productId, Long storeId, long productAmount) throws ApplicationException {
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<User> userResult = userRepository.findById(customerId);

        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));

        if (productStoreResult.get().getUnitInStock() > 0) {
            Cart newCart = new Cart();
            List<ListProduct> listProducts = new ArrayList<>();
            listProducts.add(new ListProduct(product.getId(), product.getName(), product.getPrice(),
                    commonUtils.getProductImage(product.getId()), commonUtils.calculatePrice(product),
                    product.getDiscount(), checkProductAmount(productStore, productAmount)));
            productStore.setUnitInStock(productAmount > 1 ? (productStore.getUnitInStock() - productAmount) : (productStore.getUnitInStock() - 1));
            productStoreRepository.save(productStore);
            newCart.setId(user.getId());
            newCart.setProductList(listProducts);
            newCart.setTotalPrice(commonUtils.calculatePrice(product, productAmount));
            Cart cart = cartRepository.save(newCart);
            return getCartAfterUpdateOrCreate(cart);
        } else throw new ApplicationException(PRODUCT_IS_OUT_OF_STOCK);
    }

    public CartResponse getCartAfterUpdateOrCreate(Cart cart) {
        return cartMapper.entityToResponse(cart);
    }

    @Override
    public CartResponse getCartById(Long id) throws ApplicationException {
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
    public CartResponse updateProductAmountInCart(Long customerId, Long productId, Long storeId, long amount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<User> userResult = userRepository.findById(customerId);

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart update = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));

        if (checkProductAmount(productStore, amount) > 0) {
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId().equals(product.getId())) {
                    if (items.getProductAmount() > amount) {
                        productStore.setUnitInStock(productStore.getUnitInStock() + (items.getProductAmount() - amount));
                    } else {
                        productStore.setUnitInStock(productStore.getUnitInStock() - (amount - items.getProductAmount()));
                    }
                    items.setProductAmount(amount);
                    productRepository.save(product);
                    break;
                }
            }
            update.setProductList(list);
            update.setUpdateDate(LocalDateTime.now());
            update.setUpdateBy(user.getFullName());
            update.setTotalPrice(commonUtils.calculatePrice(list));
            Cart result = cartRepository.save(update);
            if (result.getProductList().isEmpty()) {
                return deleteCart(user.getId(), store.getId());
            }
            return getCartAfterUpdateOrCreate(result);
        }
        return null;
    }

    @Override
    public CartResponse addProductToCart(Long customerId, Long productId, Long storeId, long productAmount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<User> userResult = userRepository.findById(customerId);
        Optional<Store> storeResult = storeRepository.findById(storeId);

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));

        List<ListProduct> productInCart = cart.getProductList();
        if (productInCart.stream().noneMatch(a -> a.getId().equals(product.getId()))) {
            productInCart.add(new ListProduct(product.getId(), product.getName(), product.getPrice(),
                    commonUtils.getProductImage(product.getId()), commonUtils.calculatePrice(product),
                    product.getDiscount(), checkProductAmount(productStore, productAmount)));
            productStore.setUnitInStock(productStore.getUnitInStock() - 1);
            productRepository.save(product);
            cart.setId(user.getId());
            cart.setProductList(productInCart);
            cart.setTotalPrice(commonUtils.calculatePrice(productInCart));
            cartRepository.save(cart);
            return getCartAfterUpdateOrCreate(cart);
        } else {
            long amount = 0;
            for (ListProduct items : productInCart) {
                if (items.getId().equals(product.getId())) {
                    amount = items.getProductAmount() + productAmount;
                    break;
                }
            }
            return updateProductAmountInCart(user.getId(), product.getId(), store.getId(), amount);
        }
    }

    @Override
    public CartResponse removeProductFromCart(Long customerId, Long productId, Long storeId) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customerId);
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<User> userResult = userRepository.findById(customerId);

        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!cart.getProductList().isEmpty()) {
            Cart update = cartResult.get();
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId().equals(product.getId())) {
                    returnProductFromCart(items.getId(), store.getId(), items.getProductAmount());
                    list.remove(items);
                    break;
                }
            }
            update.setTotalPrice(commonUtils.calculatePrice(list));
            update.setProductList(list);
            cartRepository.save(update);
            if (cartResult.get().getProductList().isEmpty()) {
                return deleteCart(user.getId(), store.getId());
            }
        }
        return null;
    }

    @Override
    public boolean isCartExists(Long customerId) {
        return cartRepository.findById(customerId).isPresent();
    }

    @Override
    public CartResponse deleteCart(Long id, Long storeId) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(id);
        Optional<Store> storeResult = storeRepository.findById(storeId);

        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));

        if (!cart.getProductList().isEmpty()) {
            for (ListProduct item: cart.getProductList()) {
                returnProductFromCart(item.getId(), store.getId(), item.getProductAmount());
            }
            cartRepository.deleteById(id);
            return getCartAfterUpdateOrCreate(cartResult.get());
        }
        cartRepository.deleteById(id);
        return getCartAfterUpdateOrCreate(cartResult.get());
    }

    private void returnProductFromCart(Long productId, Long storeId, long amount) throws ApplicationException {
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<Store> storeResult = storeRepository.findById(storeId);

        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));

        productStore.setUnitInStock(productStore.getUnitInStock() + amount);
        productStoreRepository.save(productStore);
    }

    @Override
    public boolean deleteCartAfterCreateOrder(Long id) {
        Optional<Cart> cartResult = cartRepository.findById(id);
        if (cartResult.isPresent()) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private long checkProductAmount(ProductStore productStore, long amount) throws ApplicationException {
        if (productStore.getUnitInStock() < amount) {
            throw new ApplicationException("Maximum amount is: " + productStore.getUnitInStock(), HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            throw new ApplicationException("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        return amount;
    }
}
