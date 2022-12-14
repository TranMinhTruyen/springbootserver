package com.ggapp.controller;
import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.services.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController(value = "BrandResource")
@Tag(name = "BrandResource")
@CrossOrigin("*")
@RequestMapping("api/brand")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMP')")
public class BrandResource {

	@Autowired
	private BrandService brandService;

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PostMapping(value = "createBrand", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse createBrand(@RequestBody BrandRequest brandRequest) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BaseResponse baseResponse = new BaseResponse();
		BrandResponse result = brandService.createBrand(brandRequest, customUserDetail);
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Created brand successfully");
		baseResponse.setPayload(result);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getBrandByKeyword")
	@PreAuthorize("permitAll()")
	public BaseResponse getBrandByKeyword(@RequestParam int page,
											   @RequestParam int size,
											   @RequestParam(required = false) String keyword){
		CommonResponsePayload commonResponsePayload = brandService.getBrandbyKeyword(page, size, keyword);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get brand successfully");
		baseResponse.setPayload(commonResponsePayload);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
	@GetMapping(value="getAllBrand")
	@PreAuthorize("permitAll()")
	public BaseResponse getAllBrand(@RequestParam int page,
										@RequestParam int size){
		CommonResponsePayload commonResponsePayload = brandService.getAllBrand(page, size);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get brand successfully");
		baseResponse.setPayload(commonResponsePayload);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@PutMapping(value = "updateBrand", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse updateBrand(@RequestParam long id, @RequestBody BrandRequest brandRequest) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BrandResponse brandResponse = brandService.updateBrand(id, brandRequest, customUserDetail);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Get brand successfully");
		baseResponse.setPayload(brandResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "logicDeleteBrand")
	public BaseResponse logicDeleteBrand(@RequestParam long id) throws ApplicationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
		BrandResponse brandResponse = brandService.logicDeleteBrand(id, customUserDetail);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Logic deleted brand successfully");
		baseResponse.setPayload(brandResponse);
		return baseResponse;
	}

	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
			security = {@SecurityRequirement(name = "Authorization")})
	@DeleteMapping(value = "physicalDeleteBrand")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BaseResponse physicalDeleteBrand(@RequestParam long id) throws ApplicationException {
		brandService.physicalDeleteBrand(id);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(HttpStatus.OK.value());
		baseResponse.setStatusname(HttpStatus.OK.name());
		baseResponse.setMessage("Physic deleted brand successfully");
		baseResponse.setPayload(null);
		return baseResponse;
	}
}
