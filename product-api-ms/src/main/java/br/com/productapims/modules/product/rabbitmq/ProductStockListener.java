package br.com.productapims.modules.product.rabbitmq;

import br.com.productapims.modules.product.dto.ProductStockDTO;
import br.com.productapims.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {
    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    //Cria o Listener e recebe as imagens
    public void recieveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
        log.info("Recebendo Mensagem: {}", new ObjectMapper().writeValueAsString(product));
        productService.updateProductStock(product);

    }
}
