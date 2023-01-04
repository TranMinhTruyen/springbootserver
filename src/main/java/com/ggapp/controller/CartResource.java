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
import static com.ggapp.common.enums.MessageResponse.DELETED_CART_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.GET_CART_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.REMOVED_PRODUCT_FROM_CART_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.UPDATED_PRODUCT_AMOUNT_SUCCESSFUL;

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


	/**
	 *
	 * @param productId
	 * @param storeId
	 * @param productAmount
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
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


	/**
	 *
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@GetMapping(value="getCartById")
	public BaseResponse getCartById() throws ApplicationException {
		this.getAuthentication();
		CartResponse cartResponse = cartService.getCartOwner(this.customUserDetail);
		return this.returnBaseReponse(cartResponse, GET_CART_SUCCESSFUL);
	}


	/**
	 *
	 * @param productId
	 * @param storeId
	 * @param amount
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PutMapping(value = "updateProductAmount")
	public BaseResponse updateProductAmount(@RequestParam int productId,
											@RequestParam int storeId,
											@RequestParam int amount) throws ApplicationException {
		this.getAuthentication();
		CartResponse cartResponse = cartService.updateProductAmountInCart(this.customUserDetail, productId, storeId, amount);
		return this.returnBaseReponse(cartResponse, UPDATED_PRODUCT_AMOUNT_SUCCESSFUL);
	}


	/**
	 *
	 * @param productId
	 * @param storeId
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@DeleteMapping(value = "removeProductFromCart")
	public BaseResponse updateProductList(@RequestParam int productId, @RequestParam int storeId) throws ApplicationException {
		this.getAuthentication();
		CartResponse cartResponse = cartService.removeProductFromCart(this.customUserDetail, productId, storeId);
		return this.returnBaseReponse(cartResponse, REMOVED_PRODUCT_FROM_CART_SUCCESSFUL);
	}


	/**
	 *
	 * @param storeId
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@DeleteMapping(value = "deleteCart")
	public BaseResponse deleteCart(@RequestParam int storeId) throws ApplicationException {
		this.getAuthentication();
		cartService.deleteCart(this.customUserDetail, storeId);
		return this.returnBaseReponse(null, DELETED_CART_SUCCESSFUL);
	}
}
