FROM node:alpine AS builder

WORKDIR /app

COPY package.json package-lock.json proxy.conf.json ./
RUN npm install

COPY . .
RUN npm run build -- --configuration production

FROM nginx:alpine

COPY --from=builder /app/dist/pet-care /usr/share/nginx/html/

COPY /default.conf /etc/nginx/conf.d/default.conf
