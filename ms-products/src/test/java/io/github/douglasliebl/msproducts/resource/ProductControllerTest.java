package io.github.douglasliebl.msproducts.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.exception.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.entity.Product;
import io.github.douglasliebl.msproducts.services.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    
    static String PRODUCT_URL = "/product";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService service;

    @Test
    @DisplayName("Should successfully register an product.")
    public void registerProductTest() throws Exception {
        // given
        ProductInsertDTO productInsertDTO = getProductInsertDTO();
        ProductDTO productResponse = getProductResponse();

        BDDMockito.given(service.createProduct(Mockito.any(ProductInsertDTO.class)))
                .willReturn(productResponse);

        String json = new ObjectMapper().writeValueAsString(productInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(PRODUCT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(productResponse.getId()))
                .andExpect(jsonPath("name").value(productResponse.getName()))
                .andExpect(jsonPath("description").value(productResponse.getDescription()))
                .andExpect(jsonPath("price").value(productResponse.getPrice()));
    }

    @Test
    @DisplayName("Should throw an exception when manufacturer isn't registered.")
    public void manufacturerNotFoundWhenRegister() throws Exception {
        // given
        ProductInsertDTO productInsertDTO = getProductInsertDTO();

        BDDMockito.given(service.createProduct(Mockito.any(ProductInsertDTO.class)))
                .willThrow(new ResourceNotFoundException("Manufacturer not registered."));

        String json = new ObjectMapper().writeValueAsString(productInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(PRODUCT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("Manufacturer not registered."))
                .andExpect(jsonPath("path").value(PRODUCT_URL));
    }

    @Test
    @DisplayName("Should throw an exception when some category isn't registered.")
    public void categoryNotFoundWhenRegister() throws Exception {
        // given
        ProductInsertDTO productInsertDTO = getProductInsertDTO();

        BDDMockito.given(service.createProduct(Mockito.any(ProductInsertDTO.class)))
                .willThrow(new ResourceNotFoundException("Categories not found: [1, 2]"));

        String json = new ObjectMapper().writeValueAsString(productInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(PRODUCT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("Categories not found: [1, 2]"))
                .andExpect(jsonPath("path").value(PRODUCT_URL));
    }

    @Test
    @DisplayName("Should successfully update an product.")
    public void updateProductTest() throws Exception {
        // given
        ProductUpdateDTO productUpdateDTO = getUpdateDTO();
        ProductDTO response = getProductResponse();
        response.setName(productUpdateDTO.getName());
        response.setDescription(productUpdateDTO.getDescription());
        response.setPrice(productUpdateDTO.getPrice());

        BDDMockito.given(service.update(Mockito.anyLong(), Mockito.any(ProductUpdateDTO.class)))
                .willReturn(response);

        String json = new ObjectMapper().writeValueAsString(productUpdateDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(PRODUCT_URL.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);


        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(response.getName()))
                .andExpect(jsonPath("description").value(response.getDescription()))
                .andExpect(jsonPath("price").value(response.getPrice()));
    }

    @Test
    @DisplayName("Should thrown an exception when trying to update with null data.")
    public void updateProductThrowsExceptionTest() throws Exception {
        // given
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();


        BDDMockito.given(service.update(Mockito.anyLong(), Mockito.any(ProductUpdateDTO.class)))
                .willThrow(new IllegalArgumentException("Update data cannot be null"));

        String json = new ObjectMapper().writeValueAsString(productUpdateDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(PRODUCT_URL.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);


        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("error").value("Update data cannot be null"))
                .andExpect(jsonPath("path").value(PRODUCT_URL.concat("/1")));
    }

    @Test
    @DisplayName("Should get an product by id.")
    public void getProductTest() throws Exception {
        // given
        ProductDTO response = getProductResponse();

        BDDMockito.given(service.getById(Mockito.anyLong()))
                .willReturn(response);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(response.getId()))
                .andExpect(jsonPath("name").value(response.getName()))
                .andExpect(jsonPath("description").value(response.getDescription()))
                .andExpect(jsonPath("price").value(response.getPrice()));
    }

    @Test
    @DisplayName("Should throw an exception when product not found.")
    public void getProductNotRegisteredTest() throws Exception {
        // given
        final Long id = 1L;

        BDDMockito.given(service.getById(Mockito.anyLong()))
                .willThrow(new ResourceNotFoundException("Product not found with id: " + id));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("Product not found with id: " + id))
                .andExpect(jsonPath("path").value(PRODUCT_URL.concat("/1")));
    }

    @Test
    @DisplayName("Should successfully delete an product.")
    public void deleteProductTest() throws Exception {
        // given
        final String response = "Product successfully deleted.";

        BDDMockito.given(service.delete(Mockito.anyLong()))
                .willReturn(response);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(PRODUCT_URL.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete an not registered product.")
    public void deleteProductNotRegisteredTest() throws Exception {
        // given
        final Long id = 1L;

        BDDMockito.given(service.delete(Mockito.anyLong()))
                .willThrow(new ResourceNotFoundException("Product not found with id: " + id));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(PRODUCT_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("Product not found with id: " + id))
                .andExpect(jsonPath("path").value(PRODUCT_URL.concat("/1")));
    }

    @Test
    @DisplayName("Should return paginated products.")
    public void returnPaginatedProductsTest() throws Exception {
        // given
        ProductDTO product = ProductDTO.builder()
                .id(1L)
                .manufacturerName(new Manufacturer().getName())
                .build();

        BDDMockito.given(service.find(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<ProductDTO>(Collections.singletonList(product), PageRequest.of(0, 10), 1));

        String query = "?name=Product&page=0&size=10";

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat(query))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer dhfajkdhf");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    private static ProductUpdateDTO getUpdateDTO() {
        return ProductUpdateDTO.builder()
                .name("Updated Product")
                .description("Updated Product")
                .price(BigDecimal.valueOf(200.00))
                .build();
    }

    private static ProductDTO getProductResponse() {
        return ProductDTO.builder()
                .id(1L)
                .name("Product")
                .manufacturerName("Product Manufacturer")
                .description("Product Description")
                .price(BigDecimal.valueOf(199.99))
                .categories(Set.of(new Category()))
                .build();
    }

    private static ProductInsertDTO getProductInsertDTO() {
        return ProductInsertDTO.builder()
                .name("Product")
                .manufacturerId(1L)
                .description("Product Description")
                .price(BigDecimal.valueOf(199.99))
                .categories(Set.of(1L, 2L, 3L))
                .build();
    }
    

}
