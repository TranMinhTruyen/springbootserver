package com.ggapp.controller;
import com.ggapp.common.enums.MessageResponse;
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
import org.springframework.http.MediaType;
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

import static com.ggapp.common.enums.MessageResponse.CREATED_CART_SUCCESSFUL;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "CartResource")
@RestController(value = "CartResource")
@CrossOrigin("*")
@RequestMapping("api/cart")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class CartResource extends CommonResource {

	@Autowired
	private CartService cartService;

	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PostMapping(value = "createCartAndAddProductToCart")
	public BaseResponse createCartAndAddProductToCart(@RequestParam int productId,
													  @RequestParam int storeId,
													  @RequestParam(required = false, defaultValue = "1") int productAmount)
			throws ApplicationException {
		this.getAuthentication();
		CartResponse cartResponse = cartService.createCartAndAddProductToCart(this.customUserDetail, productId, storeId, productAmount);
		return this.returnBaseReponse(cartResponse, CREATED_CART_SUCCESSFUL);
	}

	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
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
	public BaseResponse updateProductAmount(@RequestParam int productId,
											@RequestParam int storeId,
											@RequestParam int amount) throws Exception {
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
	public BaseResponse updateProductList(@RequestParam int productId, @RequestParam int storeId) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartService.removeProductFromCart(customUserDetail.getAccountDetail().getOwnerId(), productId, storeId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Product is removed");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteCart")
	public BaseResponse deleteCart(@RequestParam int storeId) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		cartService.deleteCart(customUserDetail.getAccountDetail().getOwnerId(), storeId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Cart is deleted");
		return baseResponse;
	}
}
