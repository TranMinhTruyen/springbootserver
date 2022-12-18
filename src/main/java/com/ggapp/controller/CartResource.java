package com.ggapp.controller;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "CartResource")
@RestController(value = "CartResource")
@CrossOrigin("*")
@RequestMapping("api/cart")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class CartResource {

	@Autowired
	private CartService cartService;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createCartAndAddProductToCart")
	public BaseResponse createCartAndAddProductToCart(@RequestParam long productId, @RequestParam long storeId,
														   @RequestParam(required = false, defaultValue = "1") long productAmount)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		if (!cartService.isCartExists(customUserDetail.getAccountDetail().getOwnerId())) {
			CartResponse cartResponse = cartService.createCart(customUserDetail.getAccountDetail().getOwnerId(), productId, storeId, productAmount);
			baseResponse.setStatus(HttpStatus.OK.value());
			baseResponse.setStatusname(HttpStatus.OK.name());
			baseResponse.setMessage("Cart is created");
			baseResponse.setPayload(cartResponse);
		}
		else {
			CartResponse cartResponse = cartService.addProductToCart(customUserDetail.getAccountDetail().getOwnerId(), productId, storeId, productAmount);
			baseResponse.setStatus(HttpStatus.OK.value());
			baseResponse.setStatusname(HttpStatus.OK.name());
			baseResponse.setMessage("Product is added");
			baseResponse.setPayload(cartResponse);
		}
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="getCartById")
	public BaseResponse getCartById() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartService.getCartById(customUserDetail.getAccountDetail().getOwnerId());
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Get cart success");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateProductAmount")
	public BaseResponse updateProductAmount(@RequestParam long productId,
											@RequestParam long storeId,
											@RequestParam long amount) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartService.updateProductAmountInCart(customUserDetail.getAccountDetail().getOwnerId(), productId, storeId, amount);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Product amount is updated");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "removeProductFromCart")
	public BaseResponse updateProductList(@RequestParam long productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartService.removeProductFromCart(customUserDetail.getAccountDetail().getOwnerId(), productId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Product is removed");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteCart")
	public BaseResponse deleteCart(@RequestParam long storeId) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		cartService.deleteCart(customUserDetail.getAccountDetail().getOwnerId(), storeId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Cart is deleted");
		return baseResponse;
	}
}
