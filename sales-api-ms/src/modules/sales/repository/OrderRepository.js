import Order from '../model/Order.js';

class OrderRepository{
    async save(order){
        try{
            return await Order.create(order);

        }catch(err){
            console.error(err.message);
            return null;
        }
    }

    async findById(id){
        try{
            return await Order.findById(id);
        }catch(err){
            console.error(err.message);
            return null;
        }
    }
    
    async findAll(){
        try{
            return await Order.find();
        }catch(err){
            console.error(err.message);
            return null;
        }
    }

    // async findById(id){
    //     try{
    //         return await User.findOne({where : {id}})
    //     }catch(err){
    //         console.error(err.message);
    //         return null;
    //     }
    // }


    // async findByEmail(email){
    //     try{
    //         return await User.findOne({ where: {email} });
    //     }catch(err){
    //         console.error(err.message);
    //         return null;
    //     }
    // }



}

export default new OrderRepository();