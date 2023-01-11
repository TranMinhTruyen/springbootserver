package com.ggapp.controller;
import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import static com.ggapp.common.enums.MessageResponse.CREATE_PRODUCT_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.DELETE_PRODUCT_IMAGE_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.DELETE_PRODUCT_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.GET_PRODUCT_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.UPDATE_PRODUCT_SUCCESSFUL;

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


	/**
	 *
	 * @param productRequest
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
	@PostMapping(value = "createProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse createProduct(@RequestBody ProductRequest productRequest) throws ApplicationException {
		this.getAuthentication();
		ProductResponse productResponse = productService.createProduct(productRequest, this.customUserDetail);
		return this.returnBaseReponse(productResponse, CREATE_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param id
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")}
	)
	@GetMapping(value = "getProductById")
	@PreAuthorize("permitAll()")
	public BaseResponse getProductById(@RequestParam int id) throws ApplicationException {
		ProductResponse productResponse = productService.getProductById(id);
		return this.returnBaseReponse(productResponse, GET_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param page
	 * @param size
	 * @param name
	 * @param brand
	 * @param category
	 * @param fromPrice
	 * @param toPrice
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")}
	)
	@GetMapping(value="getProductByKeyword")
	@PreAuthorize("permitAll()")
	public BaseResponse getProductByKeyword(@RequestParam int page,
												@RequestParam int size,
												@RequestParam(required = false) String name,
												@RequestParam(required = false) String brand,
												@RequestParam(required = false) String category,
												@RequestParam(required = false, defaultValue = "0") float fromPrice,
												@RequestParam(required = false, defaultValue = "0") float toPrice) throws ApplicationException {
		CommonResponsePayload commonResponsePayload = productService.getProductByKeyWord(page, size, name, brand,
				category, fromPrice, toPrice);
		return this.returnBaseReponse(commonResponsePayload, GET_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param page
	 * @param size
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")}
	)
	@GetMapping(value="getAllProduct")
	@PreAuthorize("permitAll()")
	public BaseResponse getAllProduct(@RequestParam int page, @RequestParam int size) throws ApplicationException {
		CommonResponsePayload commonResponsePayload = productService.getAllProduct(page, size);
		return this.returnBaseReponse(commonResponsePayload, GET_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param id
	 * @param productRequest
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@PutMapping(value = "updateProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse updateProduct(@RequestParam int id, @RequestBody ProductRequest productRequest) throws ApplicationException {
		ProductResponse productResponse = productService.updateProduct(id, productRequest);
		return this.returnBaseReponse(productResponse, UPDATE_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param productId
	 * @param imageId
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@DeleteMapping(value = "deleteImageOfProduct")
	public BaseResponse deleteImageOfProduct(@RequestParam int productId, @RequestParam int[] imageId) throws ApplicationException {
		productService.deleteLogicImageOfProduct(productId, imageId);
		return this.returnBaseReponse(null, DELETE_PRODUCT_IMAGE_SUCCESSFUL);
	}


	/**
	 *
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@DeleteMapping(value = "logicalDeleteProduct")
	public BaseResponse logicalDeleteProduct(@RequestParam int[] id) throws ApplicationException {
		productService.logicalDeleteProduct(id);
		return this.returnBaseReponse(null, DELETE_PRODUCT_SUCCESSFUL);
	}


	/**
	 *
	 * @param id
	 * @return BaseResponse
	 * @throws ApplicationException
	 */
	@Operation(responses = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Server error"),
			@ApiResponse(responseCode = "403", description = "Forbidden")},
			security = {@SecurityRequirement(name = "Authorization")}
	)
	@DeleteMapping(value = "physicalDeleteProduct")
	public BaseResponse physicalDeleteProduct(@RequestParam int[] id) throws ApplicationException {
		productService.physicalDeleteProduct(id);
		return this.returnBaseReponse(null, DELETE_PRODUCT_SUCCESSFUL);
	}
}
