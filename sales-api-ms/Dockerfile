FROM node:14
WORKDIR .
COPY package*.json ./
RUN npm install --production
COPY . . 
EXPOSE 8082
CMD ["node", "app.js"]