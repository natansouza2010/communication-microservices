import OrderService from "../service/OrderService.js";

class OrderController{

    async createOrder(req, res){
        let order = await OrderService.createOrder(req);
        return res.status(order.status).json(order);

    }

}

export default new OrderController();