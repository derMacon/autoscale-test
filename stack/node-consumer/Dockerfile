# stage 1 building the code
FROM node:10.15.3 AS builder
WORKDIR /usr/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build 

# stage 2
FROM node:10.15.3-alpine
WORKDIR /usr/app
COPY package*.json ./
RUN npm install --production

COPY --from=builder /usr/app/dist ./dist

COPY --from=builder /usr/app/schema.sql .
COPY --from=builder /usr/app/specification.xsd .

CMD node dist/src/index.js