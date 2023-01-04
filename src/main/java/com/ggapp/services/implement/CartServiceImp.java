package com.ggapp.services.implement;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
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
    public CartResponse createCartAndAddProductToCart(CustomUserDetail customUserDetail, int productId, int storeId, int productAmount)
            throws ApplicationException {
        if (cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId()).isEmpty()) {
            return createCart(customUserDetail, productId, storeId, productAmount);
        }else {
            return addProductToCart(customUserDetail, productId, storeId, productAmount);
        }
    }

    private CartResponse createCart(CustomUserDetail customUserDetail, int productId, int storeId, int productAmount) throws ApplicationException {
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<User> userResult = userRepository.findById(customUserDetail.getAccountDetail().getOwnerId());

        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));
        product = productStore.getProduct();

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
            newCart.setAmountInCart(listProducts.size());
            newCart.setTotalPrice(commonUtils.calculatePrice(product, productAmount));
            Cart cart = cartRepository.save(newCart);
            return cartMapper.entityToResponse(cart);
        } else throw new ApplicationException(PRODUCT_IS_OUT_OF_STOCK);
    }

    @Override
    public CartResponse getCartOwner(CustomUserDetail customUserDetail) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));

        long totalAmount = 0;
        for (ListProduct listProduct : cart.getProductList()) {
            totalAmount = totalAmount + listProduct.getProductAmount();
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setCustomerId(cart.getId());
        cartResponse.setProductList(cart.getProductList());
        cartResponse.setProductTotalAmount(totalAmount);
        cartResponse.setAmountInCart(cart.getProductList().size());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        return cartResponse;
    }

    @Override
    public CartResponse updateProductAmountInCart(CustomUserDetail customUserDetail, int productId, int storeId, int amount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<User> userResult = userRepository.findById(customUserDetail.getAccountDetail().getOwnerId());

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart update = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));
        product = productStore.getProduct();

        if (checkProductAmount(productStore, amount) > 0) {
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId() == product.getId()) {
                    if (items.getProductAmount() > amount) {
                        productStore.setUnitInStock(productStore.getUnitInStock() + (items.getProductAmount() - amount));
                    } else {
                        productStore.setUnitInStock(productStore.getUnitInStock() - (amount - items.getProductAmount()));
                    }
                    items.setProductAmount(amount);
                    productStoreRepository.save(productStore);
                    break;
                }
            }
            update.setProductList(list);
            update.setUpdateDate(LocalDateTime.now());
            update.setUpdateBy(user.getFullName());
            update.setAmountInCart(list.size());
            update.setTotalPrice(commonUtils.calculatePrice(list));
            Cart result = cartRepository.save(update);
            if (result.getProductList().isEmpty()) {
                return deleteCart(customUserDetail, store.getId());
            }
            return cartMapper.entityToResponse(result);
        }
        return null;
    }

    private CartResponse addProductToCart(CustomUserDetail customUserDetail, int productId, int storeId, int productAmount) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Product> productResult = productRepository.findById(productId);
        Optional<User> userResult = userRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Store> storeResult = storeRepository.findById(storeId);

        User user = userResult.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));

        Optional<ProductStore> productStoreResult = productStoreRepository.findProductStoreByStoreIdAndProductId(store.getId(), product.getId());
        ProductStore productStore = productStoreResult.orElseThrow(() -> new ApplicationException(PRODUCT_IN_STORE_NOT_FOUND));

        List<ListProduct> productInCart = cart.getProductList();
        if (productInCart.stream().noneMatch(a -> a.getId() == product.getId())) {
            productInCart.add(new ListProduct(product.getId(), product.getName(), product.getPrice(),
                    commonUtils.getProductImage(product.getId()), commonUtils.calculatePrice(product),
                    product.getDiscount(), checkProductAmount(productStore, productAmount)));
            productStore.setUnitInStock(productStore.getUnitInStock() - 1);
            productRepository.save(product);
            cart.setId(user.getId());
            cart.setProductList(productInCart);
            cart.setAmountInCart(productInCart.size());
            cart.setTotalPrice(commonUtils.calculatePrice(productInCart));
            cartRepository.save(cart);
            return cartMapper.entityToResponse(cart);
        } else {
            int amount = 0;
            for (ListProduct items : productInCart) {
                if (items.getId() == product.getId()) {
                    amount = items.getProductAmount() + productAmount;
                    break;
                }
            }
            return updateProductAmountInCart(customUserDetail, product.getId(), store.getId(), amount);
        }
    }

    @Override
    public CartResponse removeProductFromCart(CustomUserDetail customUserDetail, int productId, int storeId) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Store> storeResult = storeRepository.findById(storeId);
        Optional<Product> productResult = productRepository.findById(productId);

        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));

        if (!cart.getProductList().isEmpty()) {
            Cart update = cartResult.get();
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId() == product.getId()) {
                    returnProductFromCart(items.getId(), store.getId(), items.getProductAmount());
                    list.remove(items);
                    break;
                }
            }
            update.setTotalPrice(commonUtils.calculatePrice(list));
            update.setProductList(list);
            update.setAmountInCart(list.size());
            cartRepository.save(update);
            if (cartResult.get().getProductList().isEmpty()) {
                return deleteCart(customUserDetail, store.getId());
            }
        }
        return null;
    }

    @Override
    public CartResponse removeSingleProductFromCart(CustomUserDetail customUserDetail, int productId) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Product> productResult = productRepository.findById(productId);

        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));
        Product product = productResult.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));

        if (!cart.getProductList().isEmpty()) {
            Cart update = cartResult.get();
            List<ListProduct> list = update.getProductList();
            for (ListProduct items : list) {
                if (items.getId() == product.getId()) {
                    list.remove(items);
                    break;
                }
            }
            update.setTotalPrice(commonUtils.calculatePrice(list));
            update.setProductList(list);
            update.setAmountInCart(list.size());
            cartRepository.save(update);
            if (cartResult.get().getProductList().isEmpty()) {
                cartRepository.deleteById(customUserDetail.getAccountDetail().getOwnerId());
            }
        }
        return null;
    }

    @Override
    public CartResponse deleteCart(CustomUserDetail customUserDetail, int storeId) throws ApplicationException {
        Optional<Cart> cartResult = cartRepository.findById(customUserDetail.getAccountDetail().getOwnerId());
        Optional<Store> storeResult = storeRepository.findById(storeId);

        Store store = storeResult.orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        Cart cart = cartResult.orElseThrow(() -> new ApplicationException(CART_NOT_FOUND));

        if (!cart.getProductList().isEmpty()) {
            for (ListProduct item: cart.getProductList()) {
                returnProductFromCart(item.getId(), store.getId(), item.getProductAmount());
            }
            cartRepository.deleteById(customUserDetail.getAccountDetail().getOwnerId());
            return cartMapper.entityToResponse(cart);
        }
        cartRepository.deleteById(customUserDetail.getAccountDetail().getOwnerId());
        return cartMapper.entityToResponse(cart);
    }

    private void returnProductFromCart(int productId, int storeId, int amount) throws ApplicationException {
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
    public boolean deleteCartAfterCreateOrder(int id) {
        Optional<Cart> cartResult = cartRepository.findById(id);
        if (cartResult.isPresent()) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private int checkProductAmount(ProductStore productStore, int amount) throws ApplicationException {
        if (productStore.getUnitInStock() < amount) {
            throw new ApplicationException("Maximum amount is: " + productStore.getUnitInStock(), HttpStatus.FORBIDDEN);
        }
        if (amount <= 0) {
            throw new ApplicationException("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        return amount;
    }
}
