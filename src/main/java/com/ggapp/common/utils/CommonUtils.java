package com.ggapp.common.utils;

import com.ggapp.common.dto.response.ProductImageResponse;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.AccountDetail;
import com.ggapp.dao.document.Account;
import com.ggapp.dao.document.ListProduct;
import com.ggapp.dao.document.User;
import com.ggapp.dao.entity.ProductImage;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.entity.ProductVoucher;
import com.ggapp.dao.entity.Voucher;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.dao.repository.mysql.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.USER_NOT_FOUND;
import static com.ggapp.common.utils.Constant.ADMIN_TYPE;
import static com.ggapp.common.utils.Constant.EMPLOYEE_TYPE;
import static com.ggapp.common.utils.Constant.USER_TYPE;

@Component
public class CommonUtils {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUtils fileUtils;

    public List<String> getMethodNameOfClass(Class<?> clazz) {
        List<String> methodName = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            methodName.add(field.getName());
        }
        return methodName;
    }

    public String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public BigDecimal calculatePrice(Product product, long amount) {
        BigDecimal discountPrice = product.getPrice().multiply(BigDecimal.valueOf((product.getDiscount() / 100)));
        BigDecimal productPrice = product.getPrice().subtract(discountPrice);
        return productPrice.multiply(BigDecimal.valueOf(amount)).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(Product product) {
        BigDecimal discountPrice = product.getPrice().multiply(BigDecimal.valueOf((product.getDiscount() / 100)));
        return product.getPrice().subtract(discountPrice).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(List<ListProduct> listProduct, List<Voucher> listVoucher) {
        BigDecimal totalPrice = new BigDecimal(0);

        for (ListProduct items : listProduct) {
            BigDecimal productPrice = items.getPriceAfterDiscount().multiply(BigDecimal.valueOf(items.getProductAmount()));
            totalPrice = totalPrice.add(productPrice);
        }

        for (Voucher voucherItem: listVoucher) {
            if (!voucherItem.getProductVoucherList().isEmpty() && voucherItem.getVoucherType().equals(Constant.SINGLETYPE)) {
                for (ProductVoucher productVoucherItem: voucherItem.getProductVoucherList()) {
                    if (listProduct.stream().anyMatch(x -> x.getId() == productVoucherItem.getProduct().getId())){
                        totalPrice = checkVoucher(totalPrice, voucherItem);
                    }
                }
            }
            if (voucherItem.getVoucherType().equals(Constant.ALLTYPE)) {
                totalPrice = checkVoucher(totalPrice, voucherItem);
            }
        }
        return totalPrice.setScale(0, RoundingMode.HALF_EVEN);
    }

    private BigDecimal checkVoucher(BigDecimal totalPrice, Voucher voucher) {
        if (voucher.getDiscountType().equals(Constant.PERCENT)) {
            BigDecimal discountPrice = totalPrice.multiply(BigDecimal.valueOf((Long.parseLong(voucher.getDiscountValue()) / 100)));
            totalPrice = totalPrice.subtract(discountPrice);
        }
        if (voucher.getDiscountType().equals(Constant.FLAT)) {
            totalPrice = totalPrice.subtract(new BigDecimal(voucher.getDiscountValue()));
        }
        return totalPrice;
    }

    public BigDecimal calculatePrice(ProductResponse productResponse) {
        BigDecimal discountPrice = productResponse.getPrice().multiply(BigDecimal.valueOf((productResponse.getDiscount() / 100)));
        return productResponse.getPrice().subtract(discountPrice).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculatePrice(List<ListProduct> listProduct) {
        BigDecimal totalPrice = new BigDecimal(0);
        for (ListProduct items : listProduct) {
            BigDecimal productPrice = items.getPriceAfterDiscount().multiply(BigDecimal.valueOf(items.getProductAmount()));
            totalPrice = totalPrice.add(productPrice);
        }
        return totalPrice.setScale(0, RoundingMode.HALF_EVEN);
    }

    public List<ProductImageResponse> getProductImage(int productId) throws ApplicationException {
        Optional<List<ProductImage>> productImageList = productImageRepository.findByProductId(productId);
        if (productImageList.isPresent()) {
            List<ProductImageResponse> productImageResponseList = new ArrayList<>();
            ProductImageResponse productImageResponse = null;
            for (ProductImage productImage : productImageList.get()) {
                productImageResponse = new ProductImageResponse();
                productImageResponse.setImageId(productImage.getId());
                productImageResponse.setImageData(fileUtils.getFile(productImage.getImagePath()));
                productImageResponseList.add(productImageResponse);
            }
            return productImageResponseList;
        } else return null;
    }

    public AccountDetail accountToAccountDetail (Account account) throws ApplicationException {
        AccountDetail accountDetail = new AccountDetail();
        Optional<User> user;

        switch (account.getAccountType()) {
            case USER_TYPE:
                user = userRepository.findById(account.getOwnerId());
                user.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
                accountDetail.setOwnerId(user.get().getId());
                accountDetail.setAccount(account.getAccount());
                accountDetail.setPassword(account.getPassword());
                accountDetail.setRole(user.get().getRole());
                accountDetail.setActive(account.isActive());
                break;
            case EMPLOYEE_TYPE:
                break;
            case ADMIN_TYPE:
                break;
        }
        return accountDetail;
    }
}
