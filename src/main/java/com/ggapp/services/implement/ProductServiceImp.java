package com.ggapp.services.implement;

import com.ggapp.common.dto.request.ProductImageRequest;
import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.ProductImageResponse;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.dto.response.ProductReviewReponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.CommonUtils;
import com.ggapp.common.utils.FileUtils;
import com.ggapp.common.utils.mapper.ProductMapper;
import com.ggapp.common.utils.mapper.ProductReviewMapper;
import com.ggapp.dao.document.Cart;
import com.ggapp.dao.document.ListProduct;
import com.ggapp.dao.entity.ProductImage;
import com.ggapp.dao.entity.Brand;
import com.ggapp.dao.entity.Category;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.repository.mongo.CartRepository;
import com.ggapp.dao.repository.mysql.ProductImageRepository;
import com.ggapp.dao.repository.mysql.BrandRepository;
import com.ggapp.dao.repository.mysql.CategoryRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import com.ggapp.dao.repository.mysql.ProductReviewRepository;
import com.ggapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.BRAND_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.CATEGORY_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_IMAGE_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_IS_EXIST;
import static com.ggapp.common.enums.MessageResponse.PRODUCT_NOT_FOUND;
import static com.ggapp.common.utils.Constant.PRODUCT_FILE_PATH;

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
public class ProductServiceImp implements ProductService {

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
    private CommonUtils commonUtils;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest, CustomUserDetail customUserDetail) throws ApplicationException {

        Optional<Brand> brand = brandRepository.findById(productRequest.getIdBrand());
        Optional<Category> category = categoryRepository.findById(productRequest.getIdCategory());
        List<Product> product = productRepository.findAllByNameEqualsIgnoreCaseAndIsDeletedFalse(productRequest.getName());

        if (!product.isEmpty()) throw new ApplicationException(PRODUCT_IS_EXIST);

        Product newProduct = productMapper.requestToEntity(productRequest);
        newProduct.setBrand(brand.orElse(null));
        newProduct.setCategory(category.orElse(null));
        newProduct.setCreatedDate(LocalDateTime.now());
        newProduct.setCreatedBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
        newProduct.setDeleted(false);
        addOrUpdateProductImage(newProduct, productRequest.getImage(), false);
        Product result = productRepository.save(newProduct);
        return getProductAfterUpdateOrCreate(result);
    }

    private void addOrUpdateProductImage(Product product, List<ProductImageRequest> productImageList, boolean update) throws ApplicationException {
        if (!update && productImageList != null && !productImageList.isEmpty()) {
            List<ProductImage> imageList = new ArrayList<>();
            ProductImage image = null;
            for (ProductImageRequest item : productImageList) {
                image = new ProductImage();
                int imageIndex = productImageList.indexOf(item);
                image.setDeleted(false);
                image.setProduct(product);
                image.setImagePath(fileUtils.saveFile(product.getName() + "_" + imageIndex,
                        item.getImageData(), PRODUCT_FILE_PATH + product.getName()));
                imageList.add(image);
            }
            List<ProductImage> saveAll = productImageRepository.saveAll(imageList);
            product.setImagesPath(saveAll);
        }
        if (update && productImageList != null && !productImageList.isEmpty()) {
            Optional<List<ProductImage>> imageList = productImageRepository.findByProductId(product.getId());
            if (imageList.isPresent()) {
                List<ProductImage> result = imageList.get();
                for (ProductImageRequest productImageRequest : productImageList) {
                    for (ProductImage productImage : result) {
                        if (productImage.getId() == productImageRequest.getImageId()) {
                            productImage.setImagePath(fileUtils.saveFile(product.getName() + "_" + product.getProductCode(),
                                    productImageRequest.getImageData(), PRODUCT_FILE_PATH + product.getName()));
                        }
                    }
                }
                List<ProductImage> saveAll = productImageRepository.saveAll(result);
                product.setImagesPath(saveAll);
            }
        }
    }

    @Override
    public CommonResponsePayload getAllProduct(int page, int size) throws ApplicationException {
        List<Product> productList = productRepository.findAllByIsDeletedFalse();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            productResponseList.add(getProduct(product));
        }
        if (!productResponseList.isEmpty()) {
            return new CommonResponsePayload().getCommonResponse(page, size, productResponseList);
        } else throw new ApplicationException(PRODUCT_NOT_FOUND);
    }

    @Override
    public ProductResponse getProductById(int id) throws ApplicationException {
        Optional<Product> product = productRepository.findById(id);
        product.orElseThrow(() -> new ApplicationException(PRODUCT_NOT_FOUND));
        return getProductAfterUpdateOrCreate(product.get());
    }

    @Override
    public CommonResponsePayload getProductByKeyWord(int page, int size, String name, String brand, String category,
                                                     float fromPrice, float toPrice) throws ApplicationException {
        List<Product> productList = new ArrayList<>();

        if (StringUtils.hasText(name)) {
            productList = productRepository.findAllByNameEqualsIgnoreCaseAndIsDeletedFalse(name);
        }
        if (StringUtils.hasText(brand) && !StringUtils.hasText(category)) {
            productList = productRepository.findAllByBrandNameAndIsDeletedFalse(brand);
        }
        if (!StringUtils.hasText(brand) && StringUtils.hasText(category)) {
            productList = productRepository.findAllByCategoryNameAndIsDeletedFalse(category);
        }
        if (StringUtils.hasText(brand) && StringUtils.hasText(category)) {
            productList = productRepository.findAllByCategoryNameAndBrandNameAndIsDeletedFalse(category, brand);
        }
        if (productList.isEmpty()) {
            productList = productRepository.findAllByIsDeletedFalse();
        }
        List<ProductResponse> productResponseList = filterProduct(fromPrice, toPrice, productList);
        if (productResponseList != null && !productResponseList.isEmpty()) {
            return new CommonResponsePayload().getCommonResponse(page, size, productResponseList);
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
        update.setUpdateDate(LocalDateTime.now());
        update.setUpdateBy(customUserDetail.getAccountDetail().getFirstName() + customUserDetail.getAccountDetail().getLastName());
        if (productRequest.getImage() != null && !productRequest.getImage().isEmpty())
            addOrUpdateProductImage(update, productRequest.getImage(), true);
        update.setDiscount(productRequest.getDiscount());
        Product result = productRepository.save(update);
        updateProductFromCart(id, update);
        return getProductAfterUpdateOrCreate(result);
    }

    @Override
    public boolean deleteLogicProduct(int[] id) throws ApplicationException {
        List<Product> product = productRepository.findAllByIdIn(id);
        if (!product.isEmpty()) {
            for (long i : id) {
                for (Product p : product) {
                    if (p.getId() == i) {
                        p.setDeleted(true);
                    }
                }
                deleteProductFromCart(i);
            }
            productRepository.saveAll(product);
            return true;
        } else throw new ApplicationException(PRODUCT_NOT_FOUND);
    }

    @Override
    public void deleteProduct(int[] id) throws ApplicationException {
        List<Product> product = productRepository.findAllByIdIn(id);
        if (!product.isEmpty()) {
            for (int i : id) {
                deleteProductFromCart(i);
                productRepository.deleteById(i);
                productImageRepository.deleteById(i);
            }
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
            cart.setTotalPrice(commonUtils.calculatePrice(cart.getProductList()));
        }
        cartRepository.saveAll(cartResult);
    }

    private void deleteProductFromCart(long productId) {
        List<Cart> cartResult = cartRepository.findAll();
        for (Cart cart : cartResult) {
            for (ListProduct productInCart : cart.getProductList()) {
                if (productInCart.getId() == productId) {
                    cart.getProductList().remove(productInCart);
                    break;
                }
            }
            cart.setTotalPrice(commonUtils.calculatePrice(cart.getProductList()));
        }
        cartRepository.saveAll(cartResult);
        cartResult.forEach(cart -> {
            if (cart.getProductList().isEmpty()) {
                cartRepository.delete(cart);
            }
        });
    }

    @Override
    public void deleteLogicImageOfProduct(int productId, int[] imageId) throws ApplicationException {
        Optional<List<ProductImage>> result = productImageRepository.findByProductId(productId);
        if (result.isPresent()) {
            List<ProductImage> imageList = result.get();
            for (int i : imageId) {
                for (ProductImage productImage : imageList) {
                    if (productImage.getId() == i) {
                        productImage.setDeleted(true);
                        break;
                    }
                }
            }
            productImageRepository.saveAll(imageList);
        } else {
            throw new ApplicationException(PRODUCT_IMAGE_NOT_FOUND);
        }
    }

    public ProductResponse getProductAfterUpdateOrCreate(Product product) {
        ProductResponse response = productMapper.entityToResponse(product);
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        ProductImageResponse productImageResponse = null;
        for (ProductImage productImage : product.getImagesPath()) {
            productImageResponse = new ProductImageResponse();
            productImageResponse.setId(productImage.getId());
            productImageResponse.setImageData(productImage.getImagePath());
            productImageResponseList.add(productImageResponse);
        }
        response.setImage(productImageResponseList);
        response.setPriceAfterDiscount(commonUtils.calculatePrice(response));
        return response;
    }

    public ProductResponse getProduct(Product product) throws ApplicationException {
        ProductResponse response = productMapper.entityToResponse(product);
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        List<ProductReviewReponse> productReviewReponseList = new ArrayList<>();
        ProductImageResponse productImageResponse = null;
        for (ProductImage productImage : product.getImagesPath()) {
            productImageResponse = new ProductImageResponse();
            productImageResponse.setId(productImage.getId());
            productImageResponse.setImageData(fileUtils.getFile(productImage.getImagePath()));
            productImageResponseList.add(productImageResponse);
        }
        productReviewReponseList = productReviewMapper.entityToResponse(product.getProductReviewList());
        response.setProductReviewList(productReviewReponseList);
        response.setImage(productImageResponseList);
        response.setPriceAfterDiscount(commonUtils.calculatePrice(response));
        return response;
    }

    private List<ProductResponse> filterProduct(float fromPrice, float toPrice, List<Product> productList) throws ApplicationException {
        List<Product> filter = new ArrayList<>();
        List<ProductResponse> productResponseList = new ArrayList<>();

        if (fromPrice != 0 && toPrice != 0) {
            productList.forEach(items -> {
                if ((items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) < 0 || items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) == 0)
                        && (items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) > 0) || items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) == 0) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice == 0 && toPrice != 0) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) < 0 || items.getPrice().compareTo(BigDecimal.valueOf(toPrice)) == 0) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice != 0 && toPrice == 0) {
            productList.forEach(items -> {
                if (items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) > 0 || items.getPrice().compareTo(BigDecimal.valueOf(fromPrice)) == 0) {
                    filter.add(items);
                }
            });
        }

        if (fromPrice == 0 && toPrice == 0) {
            filter.addAll(productList);
        }
        for (Product product : filter) {
            productResponseList.add(getProduct(product));
        }
        if (!productResponseList.isEmpty()) {
            return productResponseList;
        } else return null;
    }
}
