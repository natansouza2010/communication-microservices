import express from "express";
import {connectMongoDb} from "./src/config/db/mongoDbConfig.js";
import Order from "./src/modules/sales/model/Order.js";
import checkToken from './src/config/auth/checkToken.js'
import {createInitialData} from './src/config/db/initialData.js'
import orderRoutes from './src/modules/sales/routes/OrderRoutes.js';
import { connectRabbitMq } from './src/config/rabbitmq/rabbitConfig.js';
import {sendProductStockUpdateQueue} from './src/modules/product/rabbitmq/productStockUpdateSender.js';
import tracing from './src/config/tracing.js';
const app = express();

const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

// app.get('/teste', (req,res)=>{
//     try{
//         sendProductStockUpdateQueue([
//             {
//                 productId: 1001, 
//                 quantity: 3,
//             },
//             {
//                 productId: 1002, 
//                 quantity: 2,
//             },
//             {
//                 productId: 1003, 
//                 quantity: 2,
//             },

//         ])
//         return res.status(200).json({status: 200});
//     }catch(err){
//         console.log(err);
//         return res.status(500).json({error: true });
//     }

// });
app.use(express.json());
app.use(tracing)
app.use(checkToken);
app.use(orderRoutes);

app.get('/api/status', async (req, res)=>{
    let teste = await Order.find();
    console.log(teste);
    return res.status(200).json({
        service: 'Sales-api',
        status: 'up',
        httpStatus: 200,

    });
})

app.listen( PORT, () =>{
    console.info(`Server start successyfully at pot ${PORT}`);
})