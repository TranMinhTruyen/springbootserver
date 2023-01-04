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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ggapp.common.enums.MessageResponse.CREATED_ORDER_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.GET_ORDER_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.UPDATED_ORDER_SUCCESSFUL;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "OrderResource")
@RestController(value = "OrderResource")
@CrossOrigin("*")
@RequestMapping("api/order")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMP')")
public class OrderResource extends CommonResource{

	@Autowired
	private OrderService orderService;



	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PostMapping(value = "createOrder")
	public BaseResponse createOrder(@RequestBody UserOrderRequest userOrderRequest) throws Exception {
		this.getAuthentication();
		OrderResponse orderResponse;
		if (userOrderRequest.getProductId() != null && userOrderRequest.getProductId().length > 0) {
			orderResponse = orderService.createOrderByProductId(this.customUserDetail, userOrderRequest.getProductId(),
					userOrderRequest.getStoreId());
		}
		else {
			orderResponse = orderService.createOrderByCart(this.customUserDetail);
		}
		return this.returnBaseReponse(orderResponse, CREATED_ORDER_SUCCESSFUL);
	}



	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@GetMapping(value="user/getOrder")
	public BaseResponse getOrder(@RequestParam int page, @RequestParam int size){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		CommonResponsePayload commonResponsePayload = orderService.getOrderByCustomerId(this.customUserDetail, page, size);
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get order success");
		baseResponse.setPayload(commonResponsePayload);
		return baseResponse;
	}


	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@GetMapping(value="emp/getOrderByCustomerId")
	public BaseResponse getOrderByCustomerId(@RequestParam int page,
												 @RequestParam int size) {
		this.getAuthentication();
		CommonResponsePayload commonResponsePayload = orderService.getOrderByCustomerId(this.customUserDetail, page, size);
		return this.returnBaseReponse(commonResponsePayload, GET_ORDER_SUCCESSFUL);
	}


	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PutMapping(value = "user/userUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse userUpdateOrder(@RequestBody UserOrderRequest userOrderRequest) throws ApplicationException {
		this.getAuthentication();
		OrderResponse orderResponse = orderService.updateOrderByUser(this.customUserDetail, userOrderRequest);
		return this.returnBaseReponse(orderResponse, UPDATED_ORDER_SUCCESSFUL);
	}


	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PutMapping(value = "emp/empUpdateOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse empUpdateOrder(@RequestParam int id, @RequestBody OrderRequest orderRequest) throws ApplicationException {
		this.getAuthentication();
		OrderResponse orderResponse = orderService.updateOrderByEmp(id, this.customUserDetail,orderRequest);
		return this.returnBaseReponse(orderResponse, UPDATED_ORDER_SUCCESSFUL);
	}



	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	public BaseResponse deleteOrder(@RequestParam long id) throws ApplicationException {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Delete order success");
		baseResponse.setPayload(null);
		return baseResponse;
	}
}
