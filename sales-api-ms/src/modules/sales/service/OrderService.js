import OrderRepository from '../repository/OrderRepository.js';
import {sendProductStockUpdateQueue} from '../../product/rabbitmq/productStockUpdateSender.js'
import {ACCEPTED, REJECTED, PENDING} from '../status/OrderStatus.js'
import OrderException from '../exception/OrderException.js'; 
import {INTERNAL_SERVER_ERROR, BAD_REQUEST} from '../../../config/constants/httpStatus.js'

class OrderService {
    async createOrder(req){
        try{
            let orderData = req.body;
            this.validateOrderData(orderData);
            const { authUser } = req;
            let order = {
                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData,
            }
            let createdOrder = await OrderRepository.save(order);
            sendProductStockUpdateQueue(order.products);

            return {

                status: httpStatus.SUCESS, 
                createdOrder,
                
            }
        }catch(err){
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,

            }
        }
    }

    validateOrderData(data) {
        if(!data || !data.products){
            throw new OrderException(BAD_REQUEST ,'The products must be informed.')
        }
        
    }

}

return new OrderService();