package com.ggapp.controller;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.request.UserOrderRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.services.OrderService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "OrderResource")
@RestController(value = "OrderResource")
@CrossOrigin("*")
@RequestMapping("api/order")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMP')")
public class OrderResource {

	@Autowired
	private OrderService orderService;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createOrder")
	public BaseResponse createOrder(@RequestBody UserOrderRequest userOrderRequest) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		OrderResponse orderResponse;
		BaseResponse baseResponse = new BaseResponse();
		if (userOrderRequest.getProductId() != null && userOrderRequest.getProductId().length > 0) {
			orderResponse = orderService.createOrderByProductId(customUserDetail.getAccountDetail().getOwnerId(), userOrderRequest.getProductId(), userOrderRequest.getStoreId());
		}
		else {
			orderResponse = orderService.createOrderByCart(customUserDetail.getAccountDetail().getOwnerId());
		}
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Cart is created");
		baseResponse.setPayload(orderResponse);
		return baseResponse;
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="user/getOrder")
	public BaseResponse getOrder(@RequestParam int page, @RequestParam int size){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		CommonResponsePayload commonResponsePayload = orderService.getOrderByCustomerId(page, size, customUserDetail.getAccountDetail().getOwnerId());
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get order success");
		baseResponse.setPayload(commonResponsePayload);
		return baseResponse;
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@GetMapping(value="emp/getOrderByCustomerId")
	public BaseResponse getOrderByCustomerId(@RequestParam int page,
												 @RequestParam int size) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		CommonResponsePayload commonResponsePayload = orderService.getOrderByCustomerId(page, size, customUserDetail.getAccountDetail().getOwnerId());
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get order success");
		baseResponse.setPayload(commonResponsePayload);
		return baseResponse;
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "user/userUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse userUpdateOrder(@RequestBody UserOrderRequest userOrderRequest) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setAddress(userOrderRequest.getAddress());
		orderService.updateOrder(customUserDetail.getAccountDetail().getOwnerId(), orderRequest);
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Update order success");
		baseResponse.setPayload(orderRequest);
		return baseResponse;
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "emp/empUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse empUpdateOrder(@RequestParam long id, @RequestBody OrderRequest orderRequest) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		OrderRequest order = new OrderRequest();
		order.setAddress(orderRequest.getAddress());
		orderService.updateOrder(customUserDetail.getAccountDetail().getOwnerId(), order);
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Update order success");
		baseResponse.setPayload(order);
		return baseResponse;
	}



	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteOrder")
	public BaseResponse deleteOrder(@RequestParam long id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Delete order success");
		baseResponse.setPayload(null);
		return baseResponse;
	}
}
