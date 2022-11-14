package com.example.services.implement;

import com.example.common.dto.request.ProductImageRequest;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.CommonResponse;
import com.example.common.dto.response.ProductResponse;
import com.example.common.dto.response.ProductReviewReponse;
import com.example.common.exception.ApplicationException;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.utils.Utils;
import com.example.common.utils.mapper.ProductMapper;
import com.example.common.utils.mapper.ProductReviewMapper;
import com.example.dao.document.Cart;
import com.example.dao.document.Image;
import com.example.dao.document.ListProduct;
import com.example.dao.document.ProductImage;
import com.example.dao.entity.Brand;
import com.example.dao.entity.Category;
import com.example.dao.entity.Product;
import com.example.dao.entity.ProductReview;
import com.example.dao.repository.mongo.CartRepository;
import com.example.dao.repository.mongo.ProductImageRepository;
import com.example.dao.repository.mysql.BrandRepository;
import com.example.dao.repository.mysql.CategoryRepository;
import com.example.dao.repository.mysql.ProductRepository;
import com.example.dao.repository.mysql.ProductReviewRepository;
import com.example.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.common.commonenum.MessageError.BRAND_NOT_FOUND;
import static com.example.common.commonenum.MessageError.CATEGORY_NOT_FOUND;
import static com.example.common.commonenum.MessageError.PRODUCT_IMAGE_NOT_FOUND;
import static com.example.common.commonenum.MessageError.PRODUCT_IS_EXIST;
import static com.example.common.commonenum.MessageError.PRODUCT_NOT_FOUND;

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
public class ProductServicesImplement implements ProductServices {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) throws ApplicationException {
        Optional<Brand> brand = brandRepository.findById(productRequest.getId_brand());
        Optional<Category> category = categoryRepository.findById(productRequest.getId_category());
        List<Product> product = productRepository.findAllByNameEqualsIgnoreCaseAndDeleteByIsNullAndDeleteDateIsNull(productRequest.getName());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        CustomUserDetail customUserDetail = (CustomUserDetail) securityContext.getAuthentication().getPrincipal();
        if (!product.isEmpty()) throw new ApplicationException(PRODUCT_IS_EXIST);
        brand.orElseThrow(() -> new ApplicationException(BRAND_NOT_FOUND));
        category.orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
        Product newProduct = productMapper.requestToEntity(productRequest);
        newProduct.setBrand(brand.get());
        newProduct.setCategory(category.get());
        newProduct.setCreatedDate(LocalDateTime.now());
        newProduct.setCreatedBy(customUserDetail.getUser().getFirstName() + customUserDetail.getUser().getLastName());
        Product result = productRepository.save(newProduct);
        addOrUpdateProductImage(result.getId(), productRequest.getImage(), false);
        return getProductAfterUpdateOrCreate(result);
    }

    private void addOrUpdateProductImage(int productId, List<ProductImageRequest> productImageList, boolean update) throws ApplicationException {
        if (!update && productImageList != null && !productImageList.isEmpty()) {
            ProductImage productImage = new ProductImage();
            productImage.setId(productId);
            List<Image> imageList = new ArrayList<>();
            for (ProductImageRequest item : productImageList) {
                Image image = new Image();
                int imageIndex = productImageList.indexOf(item);
                image.setImageId(imageIndex + 1);
                image.setImage(productImageList.get(imageIndex).getImage());
                imageList.add(image);
            }
            productImage.setImages(imageList);
            productImageRepository.save(productImage);
        }
        if (update && productImageList != null && !productImageList.isEmpty()) {
            Optional<ProductImage> productImage = productImageRepository.findById(productId);
            if (productImage.isPresent()) {
                ProductImage result = productImage.get();
                List<Image> imageList = result.getImages();
                Image image;
                for (ProductImageRequest item : productImageList) {
                    image = new Image();
                    if (imageList.isEmpty()) image.setImageId(1);
                    else {
                        List<Image> imageIndex = imageList.stream().skip(imageList.size() - 1).collect(Collectors.toList());
                        image.setImageId(imageIndex.get(0).getImageId() + 1);
                    }
                    image.setImage(productImageList.get(productImageList.indexOf(item)).getImage());
                    imageList.add(image);
                }
                result.setImages(imageList);
                productImageRepository.save(result);
            }
        }
    }

    @Override
    public CommonResponse getAllProduct(int page, int size) throws ApplicationException {
        List<Product> productList = productRepository.findAllByDeleteByIsNullAndDeleteDateIsNull();
        List<ProductReview> productReviewList = productReviewRepository.findAll();

        List<ProductResponse> productResponseList = productMapper.entityToResponse(productList);
        List<ProductReviewReponse> productReviewResponseList = productReviewMapper.entityToResponse(productReviewList);
        List<ProductReviewReponse> temp;
        for (ProductResponse productResponse : productResponseList) {
            productResponse.setImage(utils.getProductImage(productResponse.getId()));
            productResponse.setPriceAfterDiscount(utils.calculatePrice(productResponse));
            temp = new ArrayList<>();
            if (productReviewResponseList != null) {
                for (ProductReviewReponse productReviewReponse : productReviewResponseList) {
                    if (productReviewReponse.getProductId() == productResponse.getId()) {
                        temp.add(productReviewReponse);
                        productResponse.setProductReviewList(temp);
                    }
                }
            }
        }
        if (!productResponseList.isEmpty()) {
            return new CommonResponse().getCommonResponse(page, size, productResponseList);
        } else throw new ApplicationException(PRODUCT_NOT_FOUND);
    }

    @Override
    public ProductResponse getProductById(int id) throws ApplicationException {
        Optional<Product> product = productRepository.findById(id);
        product.orElseThrow(() -> new ApplicationException(PRODUCT_IS_EXIST));
        ProductResponse productResponse = productMapper.entityToResponse(product.get());
        productResponse.setImage(utils.getProductImage(productResponse.getId()));
        productResponse.setPriceAfterDiscount(utils.calculatePrice(productResponse));
        return productResponse;
    }

    @Override
    public CommonResponse getProductByKeyWord(int page, int size, String name, String brand, String category, float fromPrice, float toPrice) throws ApplicationException {
        List<ProductResponse> productResponseList = filterProduct(name, brand, category, fromPrice, toPrice);
        if (productResponseList != null && !productResponseList.isEmpty()) {
            return new CommonResponse().getCommonResponse(page, size, productResponseList);
        } else throw new ApplicationException(PRODUCT_NOT_FOUND);
    }

    @Override
    public ProductResponse updateProduct(int id, ProductRequest productRequest) throws ApplicationException {
        Optional<Product> product = productRepository.findById(id);
        product.orElseThrow(() -> new ApplicationException(PRODUCT_IS_EXIST));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        CustomUserDetail customUserDetail = (CustomUserDetail) securityContext.getAuthentication().getPrincipal();
        Product update = product.get();
        update.setName(productRequest.getName());
        update.setType(productRequest.getType());
        update.setPrice(productRequest.getPrice());
        update.setUnitInStock(productRequest.getUnitInStock());
        update.setUpdateDate(LocalDateTime.now());
        update.setUpdateBy(customUserDetail.getUser().getFirstName() + customUserDetail.getUser().getLastName());
        if (productRequest.getImage() != null && !productRequest.getImage().isEmpty())
            addOrUpdateProductImage(update.getId(), productRequest.getImage(), true);
        update.setDiscount(productRequest.getDiscount());
        Product result = productRepository.save(update);
        updateProductFromCart(id, update);
        return getProductAfterUpdateOrCreate(result);
    }

    @Override
    public boolean deleteProduct(List<Integer> id) throws ApplicationException {
        List<Product> product = productRepository.findAllById(id);
        if (!product.isEmpty()) {
            for (int i : id) {
                deleteProductFromCart(i);
            }
            productRepository.deleteAllById(id);
            productImageRepository.deleteAllById(id);
            return true;
        } else throw new ApplicationException(PRODUCT_NOT_FOUND);
    }

    public void updateProductFromCart(int productId, Product product) {
        List<Cart> cartResult = cartRepository.findAll();
        for (Cart cart : cartResult) {
            for (ListProduct listProduct : cart.getProductList()) {
                if (listProduct.getId() == productId) {
                    listProduct.setProductName(product.getName());
                    listProduct.setPrice(product.getPrice());
                    listProduct.setDiscount(product.getDiscount());
                    break;
                }
            }
            cart.setTotalPrice(utils.calculatePrice(cart.getProductList()));
        }
        cartRepository.saveAll(cartResult);
    }

    private void deleteProductFromCart(int productId) {
        List<Cart> cartResult = cartRepository.findAll();
        for (Cart cart : cartResult) {
            for (ListProduct productInCart : cart.getProductList()) {
                if (productInCart.getId() == productId) {
                    cart.getProductList().remove(productInCart);
                    break;
                }
            }
            cart.setTotalPrice(utils.calculatePrice(cart.getProductList()));
        }
        cartRepository.saveAll(cartResult);
        cartResult.forEach(cart -> {
            if (cart.getProductList().isEmpty()) {
                cartRepository.delete(cart);
            }
        });
    }

    @Override
    public boolean deleteImageOfProduct(int productId, List<Integer> imageId) throws ApplicationException {
        Optional<ProductImage> result = productImageRepository.findById(productId);
        if (result.isPresent()) {
            ProductImage productImage = result.get();
            for (Integer i : imageId) {
                for (Image image : productImage.getImages()) {
                    if (image.getImageId() == i) {
                        productImage.getImages().remove(image);
                        break;
                    }
                }
            }
            productImageRepository.save(productImage);
            return true;
        } else {
            throw new ApplicationException(PRODUCT_IMAGE_NOT_FOUND);
        }
    }

    public ProductResponse getProductAfterUpdateOrCreate(Product product) {
        ProductResponse response = productMapper.entityToResponse(product);
        response.setImage(utils.getProductImage(response.getId()));
        response.setPriceAfterDiscount(utils.calculatePrice(response));
        return response;
    }

    private List<ProductResponse> filterProduct(@Nullable String name, @Nullable String brand, @Nullable String category, float fromPrice, float toPrice) {
        List<Product> productList = productRepository.findAllByDeleteByIsNullAndDeleteDateIsNull();
        List<Product> filter = new ArrayList<>();
        List<ProductResponse> productResponseList = new ArrayList();
        if (name != null && fromPrice == 0 && toPrice == 0) {
            productList.forEach(items -> {
                if (items.getName().toLowerCase().contains(name.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (category == null && brand != null && fromPrice == 0 && toPrice == 0) {
            productList.forEach(items -> {
                if (items.getBrand().getName().toLowerCase().contains(brand.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (brand == null && category != null && fromPrice == 0 && toPrice == 0) {
            productList.forEach(items -> {
                if (items.getCategory().getName().toLowerCase().contains(category.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice != 0 && toPrice != 0 && brand == null && category == null) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) == -1 && items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) == 1) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice != 0 && toPrice != 0 && brand != null && category == null) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) == -1 && items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) == 1 && items.getBrand().getName().toLowerCase().contains(brand.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice != 0 && toPrice != 0 && brand == null && category != null) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) == -1 && items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) == 1 && items.getCategory().getName().toLowerCase().contains(category.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice != 0 && toPrice != 0 && brand != null && category != null) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) < 0 && items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) > 0 && items.getBrand().getName().toLowerCase().contains(brand.toLowerCase()) && items.getCategory().getName().toLowerCase().contains(category.toLowerCase())) {
                    filter.add(items);
                }
            });
        }

        if (brand != null && category != null && fromPrice == 0 && toPrice == 0) {
            productList.forEach(items -> {
                if (items.getBrand().getName().toLowerCase().equals(brand.toLowerCase()) && items.getCategory().getName().toLowerCase().equals(category.toLowerCase())) {
                    filter.add(items);
                }
            });
        }
        filter.forEach(item -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(item.getId());
            productResponse.setName(item.getName());
            productResponse.setPrice(item.getPrice());
            productResponse.setPriceAfterDiscount(utils.calculatePrice(item));
            productResponse.setType(item.getType());
            productResponse.setUnitInStock(item.getUnitInStock());
            productResponse.setBrand(item.getBrand().getName());
            productResponse.setCategory(item.getCategory().getName());
            productResponse.setDiscount(item.getDiscount());
            productResponse.setImage(utils.getProductImage(productResponse.getId()));
            productResponseList.add(productResponse);
        });
        if (!productResponseList.isEmpty()) {
            return productResponseList;
        } else return null;
    }
}
