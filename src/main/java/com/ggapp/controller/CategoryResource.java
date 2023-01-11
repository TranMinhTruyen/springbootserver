package com.ggapp.controller;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.services.CategoryService;
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

import static com.ggapp.common.enums.MessageResponse.CREATED_CATEGORY_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.GET_CATEGORY_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.LOGIC_DELETED_CATEGORY_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.PHYSIC_DELETED_CATEGORY_SUCCESSFUL;
import static com.ggapp.common.enums.MessageResponse.UPDATE_CATEGORY_SUCCESSFUL;

@Tag(name = "CategoryResource")
@RestController(value = "CategoryResource")
@CrossOrigin("*")
@RequestMapping("api/category")
public class CategoryResource extends CommonResource{

    @Autowired
    private CategoryService categoryService;


    /**
     *
     * @param categoryRequest
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
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('CREATED')")
    @PostMapping(value = "createCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createCategory(@RequestBody CategoryRequest categoryRequest) throws ApplicationException {
        this.getAuthentication();
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest, this.customUserDetail);
        return this.returnBaseReponse(categoryResponse, CREATED_CATEGORY_SUCCESSFUL);
    }


    /**
     *
     * @param page
     * @param size
     * @param keyword
     * @return BaseResponse
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")}
    )
    @GetMapping(value = "getCategoryByKeyword")
    @PreAuthorize("permitAll()")
    public BaseResponse getCategoryByKeyword(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false) String keyword) {
        CommonResponsePayload commonResponsePayload = categoryService.getCategoryByKeyword(page, size, keyword);
        return this.returnBaseReponse(commonResponsePayload, GET_CATEGORY_SUCCESSFUL);
    }


    /**
     *
     * @param page
     * @param size
     * @return BaseResponse
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")}
    )
    @GetMapping(value = "getAllCategory")
    public BaseResponse getAllCategory(@RequestParam int page, @RequestParam int size) {
        CommonResponsePayload commonResponsePayload = categoryService.getAllCategory(page, size);
        return this.returnBaseReponse(commonResponsePayload, GET_CATEGORY_SUCCESSFUL);
    }


    /**
     *
     * @param id
     * @param categoryRequest
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "updateCategory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateCategory(@RequestParam int id, @RequestBody CategoryRequest categoryRequest) throws ApplicationException {
        this.getAuthentication();
        CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest, customUserDetail);
        return this.returnBaseReponse(categoryResponse, UPDATE_CATEGORY_SUCCESSFUL);
    }


    /**
     *
     * @param id
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "logicDeleteCategory")
    public BaseResponse logicDeleteCategory(@RequestParam int id) throws ApplicationException {
        this.getAuthentication();
        CategoryResponse categoryResponse = categoryService.logicDeleteCategory(id, this.customUserDetail);
        return this.returnBaseReponse(categoryResponse, LOGIC_DELETED_CATEGORY_SUCCESSFUL);
    }


    /**
     *
     * @param id
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "physicDeleteCategory")
    public BaseResponse physicDeleteCategory(@RequestParam int id) throws ApplicationException {
        this.getAuthentication();
        categoryService.physicalDeleteBrand(id);
        return this.returnBaseReponse(null, PHYSIC_DELETED_CATEGORY_SUCCESSFUL);
    }
}
