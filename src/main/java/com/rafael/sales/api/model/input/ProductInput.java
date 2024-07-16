package com.rafael.sales.api.model.input;

import com.rafael.sales.core.validation.FileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Getter
@Setter
public class ProductInput {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    private BigInteger quantity;

    @NotNull
    @FileSize(max = "500KB")
    private MultipartFile file;

}
