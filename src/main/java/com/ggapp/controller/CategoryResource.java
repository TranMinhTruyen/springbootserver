package com.ggapp.controller;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Tag(name = "CategoryResource")
@RestController(value = "CategoryResource")
@CrossOrigin("*")
@RequestMapping("api/category")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMP')")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "createCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        if (categoryService.isExists(categoryRequest.getName())) {
            return new ResponseEntity<>("Category is exists", HttpStatus.UNAUTHORIZED);
        }
        if (categoryService.createCategory(categoryRequest, customUserDetail))
            return new ResponseEntity<>(categoryRequest, HttpStatus.OK);
        else
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @GetMapping(value = "getCategoryByKeyword")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BaseResponse> getCategoryByKeyword(@RequestParam int page,
                                                             @RequestParam int size,
                                                             @RequestParam(required = false) String keyword) {


        CommonResponsePayload commonResponsePayload = categoryService.getCategoryByKeyword(page, size, keyword);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Get category successfully");
        baseResponse.setPayload(commonResponsePayload);
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    @GetMapping(value = "getAllCategory")
    public ResponseEntity<BaseResponse> getAllCategory(@RequestParam int page,
                                                       @RequestParam int size) {
        CommonResponsePayload commonResponsePayload = categoryService.getAllCategory(page, size);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Get category successfully");
        baseResponse.setPayload(commonResponsePayload);
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(value = "updateCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@RequestParam long id, @RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))) {
            CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest, customUserDetail);
            if (categoryResponse != null) {
                return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
            } else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(value = "deleteCategory")
    public ResponseEntity<?> deleteBrand(@RequestParam long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))) {
            if (categoryService.deleteCategory(id)) {
                return new ResponseEntity<>("category is deleted", HttpStatus.OK);
            } else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }
    }
}
