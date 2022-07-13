import express from "express";
import {connectMongoDb} from "./src/config/db/mongoDbConfig.js";
import Order from "./src/modules/sales/model/Order.js";
import {createInitialData} from './src/config/db/initialData.js'
import checkToken from "./src/config/auth/checkToken.js";
import { connectRabbitMq } from './src/config/rabbitmq/rabbitConfig.js';
const app = express();

const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

app.use(checkToken);

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