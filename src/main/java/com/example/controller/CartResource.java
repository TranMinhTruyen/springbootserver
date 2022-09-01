package com.example.controller;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.dto.response.BaseResponse;
import com.example.common.dto.response.CartResponse;
import com.example.services.CartServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private CartServices cartServices;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createCartAndAddProductToCart")
	public BaseResponse createCartAndAddProductToCart(@RequestParam int productId,
														   @RequestParam(required = false, defaultValue = "1") long productAmount)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		if (!cartServices.isCartExists(customUserDetail.getUser().getId())) {
			CartResponse cartResponse = cartServices.createCart(customUserDetail.getUser().getId(), productId, productAmount);
			baseResponse.setStatus(HttpStatus.OK.value());
			baseResponse.setStatusname(HttpStatus.OK.name());
			baseResponse.setMessage("Cart is created");
			baseResponse.setPayload(cartResponse);
		}
		else {
			CartResponse cartResponse = cartServices.addProductToCart(customUserDetail.getUser().getId(), productId, productAmount);
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
		CartResponse cartResponse = cartServices.getCartById(customUserDetail.getUser().getId());
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
												@RequestParam long amount) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartServices.updateProductAmountInCart(customUserDetail.getUser().getId(), productId, amount);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Product amount is updated");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "removeProductFromCart")
	public BaseResponse updateProductList(@RequestParam int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		CartResponse cartResponse = cartServices.removeProductFromCart(customUserDetail.getUser().getId(), productId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Product is removed");
		baseResponse.setPayload(cartResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteCart")
	public BaseResponse deleteCart(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		cartServices.deleteCart(customUserDetail.getUser().getId());
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(200);
		baseResponse.setMessage("Cart is deleted");
		return baseResponse;
	}
}
