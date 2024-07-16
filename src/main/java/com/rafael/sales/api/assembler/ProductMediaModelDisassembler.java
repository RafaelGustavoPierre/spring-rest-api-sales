package com.rafael.sales.api.assembler;

import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.api.model.input.ProductMediaInput;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductMedia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMediaModelDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public ProductMedia toDomainObject(ProductMediaInput productMediaInput) {
        return modelMapper.map(productMediaInput, ProductMedia.class);
    }

    public void copyToDomainObject(ProductInput productInput, Product product) {
        ProductMedia productMedia = new ProductMedia();

        var fileName = productInput.getFile().getOriginalFilename();
        var contentType = productInput.getFile().getContentType();
        var size = productInput.getFile().getSize();

        productMedia.setFileName(fileName);
        productMedia.setContentType(contentType);
        productMedia.setSize(size);
    }

}
