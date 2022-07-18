import OrderRepository from '../repository/OrderRepository.js';
import {sendProductStockUpdateQueue} from '../../product/rabbitmq/productStockUpdateSender.js'
import {ACCEPTED, REJECTED, PENDING} from '../status/OrderStatus.js'
import OrderException from '../exception/OrderException.js'; 
import {INTERNAL_SERVER_ERROR, BAD_REQUEST, SUCESS} from '../../../config/constants/httpStatus.js'
import ProductClient from '../../../modules/product/client/ProductClient.js';

class OrderService {
    async createOrder(req){
        try{
            let orderData = req.body;
            this.validateOrderData(orderData);
            const { authUser } = req;
            const {authorization} = req.headers;
            let order = this.createInitialOrderData(orderData, authUser);
            await this.validateProductStock(order, authorization);
          
            let createdOrder = await OrderRepository.save(order);
            this.sendMessage(createdOrder)
            

            return {

                status: SUCESS, 
                createdOrder,
                
            }
        }catch(err){
            return {
                status: err.status ? err.status : INTERNAL_SERVER_ERROR,
                message: err.message,

            }
        }
    }

    createInitialOrderData(orderData, authUser){
        return {
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData.products,
        }
    }

    async updateOrder(orderMessage) {
        console.log(`Ferrou ${orderMessage}`);
        try {
          const order = JSON.parse(orderMessage);
          console.log(`o que bugou  ${order.salesId} `);
          if (order.salesId && order.status) {
            console.log("Aoba entrouuaasdsa")
            let existingOrder = await OrderRepository.findById(order.salesId);
            if (existingOrder && order.status !== existingOrder.status) {
              existingOrder.status = order.status;
              existingOrder.updatedAt = new Date();
              await OrderRepository.save(existingOrder);
            }
          } else {
            console.warn("The order message was not complete.");
          }
        } catch (err) {
          console.error("Could not parse order message from queue.");
          console.error(err.message);
        }
      }
    validateOrderData(data) {
        if(!data || !data.products){
            throw new OrderException(BAD_REQUEST ,'The products must be informed.')
        }
        
    }

    async validateProductStock(order, token){
        let stockIsOk = await ProductClient.checkProducStock(order, token);
        if (!stockIsOk) {
        throw new OrderException(
            BAD_REQUEST,
            "The stock is out for the products."
            );
        }

    }

    sendMessage(createdOrder){
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products,

        }

        sendProductStockUpdateQueue(message)
    }

    async findById(req) {
        try {
          const { id } = req.params;
          this.validateInformedId(id);
          const existingOrder = await OrderRepository.findById(id);
          if (!existingOrder) {
            throw new OrderException(BAD_REQUEST, "The order was not found.");
          }
          return {
            status: SUCESS,
            existingOrder,
          };
        } catch (err) {
          return {
            status: err.status ? err.status : INTERNAL_SERVER_ERROR,
            message: err.message,
          };
        }
      }
    
      validateInformedId(id) {
        if (!id) {
          throw new OrderException(BAD_REQUEST, "The order ID must be informed.");
        }
      }

}

export default new OrderService();