import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import UserException from "../exception/UserException.js";

import bcrypt from "bcrypt";
import jwt from 'jsonwebtoken'
import * as secrets from "../../../config/constants/secret.js"

class UserService{

    async findByEmail(req){
        try{
            const { email } = req.params;
            this.validateRequestData(email);
            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user);

           
            return {
                status: httpStatus.SUCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                }
            }

        }catch(err){
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,

            }
        }


    }
    validateRequestData(email){
        if(!email){
            throw new UserException(httpStatus.BAD_REQUEST, 'User email was not informed');
        }
    }

    validateUserNotFound(user){
        if(!user){
            throw new UserException(httpStatus.BAD_REQUEST, 'User not found !')
        }
    }

    async getAcessToken(req){
        try{
            const {email, password} = req.body;
            this.validateAccessTokenData(email,password);
            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user);
            await this.validateAccessTokenData(password, user.password);
            const authUser = {id : user.id , name: user.name, email : user.email}
            const accessToken = jwt.sign({authUser}, secrets.API_SECRET ,{expiresIn: '1d'})
            return {
                status: httpStatus.SUCESS, 
                accessToken: accessToken,
                
            }

        }catch(err){
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,

            }
        }
       
    }

    validateAccessTokenData(email, password){
        if(!email || !password){
            throw new UserException(httpStatus.UNAUTHORIZED, 'Email and password must be informed.')
        }

    }

    async validatePassword(password, hashPassword){
        if(!await bcrypt.compare(password, hashPassword)){
            throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match.")
        }
    }
}

export default new UserService();