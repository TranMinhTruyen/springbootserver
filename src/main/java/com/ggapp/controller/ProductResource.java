package com.ggapp.controller;
import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "ProductResource")
@RestController(value = "ProductResource")
@CrossOrigin("*")
@RequestMapping("api/product")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMP')")
public class ProductResource extends CommonResource{

	@Autowired
	private ProductService productService;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse createProduct(@RequestBody ProductRequest productRequest) throws Exception {
		this.getAuthentication();
		ProductResponse productResponse = productService.createProduct(productRequest, this.customUserDetail);
		return this.returnBaseReponse(productResponse, "Create product successfully", HttpStatus.OK);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value = "getProductById")
	@PreAuthorize("permitAll()")
	public BaseResponse getProductById(@RequestParam int id) throws Exception {
		ProductResponse productResponse = productService.getProductById(id);
		return this.returnBaseReponse(productResponse, "Get product successfully", HttpStatus.OK);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getProductByKeyword")
	@PreAuthorize("permitAll()")
	public ResponseEntity<BaseResponse> getProductByKeyword(@RequestParam int page,
												@RequestParam int size,
												@RequestParam(required = false) String name,
												@RequestParam(required = false) String brand,
												@RequestParam(required = false) String category,
												@RequestParam(required = false, defaultValue = "0") float fromPrice,
												@RequestParam(required = false, defaultValue = "0") float toPrice) throws Exception {
		CommonResponsePayload commonResponsePayload = productService.getProductByKeyWord(page, size, name, brand,
				category, fromPrice, toPrice);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get product successfully");
		baseResponse.setPayload(commonResponsePayload);
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getAllProduct")
	@PreAuthorize("permitAll()")
	public ResponseEntity<BaseResponse> getAllProduct(@RequestParam int page, @RequestParam int size) throws Exception {
		CommonResponsePayload commonResponsePayload = productService.getAllProduct(page, size);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get product successfully");
		baseResponse.setPayload(commonResponsePayload);
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateProduct(@RequestParam int id, @RequestBody ProductRequest productRequest) throws Exception {
		ProductResponse productResponse = productService.updateProduct(id, productRequest);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get product successfully");
		baseResponse.setPayload(productResponse);
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteImageOfProduct")
	public ResponseEntity<BaseResponse> deleteImageOfProduct(@RequestParam int productId, @RequestParam int[] imageId) throws Exception {
		productService.deleteLogicImageOfProduct(productId, imageId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Delete image successfully");
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}


	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "deleteProduct")
	public ResponseEntity<?>deleteProduct(@RequestParam int[] id) throws Exception {
		productService.deleteProduct(id);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Delete product successfully");
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}
}
