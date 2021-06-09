# FROM node:10-alpine
# RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
# WORKDIR /home/node/app
# COPY package*.json ./
# USER node
# RUN npm install
# COPY --chown=node:node . .
# EXPOSE 3000
# CMD [ "node", "index.js" ]


# # stage 1 building the code
# FROM node:10.15.3-alpine as builder
# WORKDIR /home/node/app
# COPY package*.json ./
# RUN npm install
# COPY . .
# RUN npm run build

# # stage 2
# FROM node:10.15.3-alpine as builder
# WORKDIR /home/node/app
# COPY package*.json ./
# RUN npm install --production
# COPY --from=builder /home/node/app/dist ./dist

# CMD node dist/src/index.js



# Working theory...
# # stage 1 building the code
# FROM node:10.15.3-alpine AS builder
# WORKDIR /usr/app
# COPY package*.json ./
# RUN npm install
# COPY . .
# RUN npm run build

# # stage 2
# FROM node:10.15.3-alpine
# WORKDIR /usr/app
# COPY package*.json ./
# RUN npm install --production

# COPY --from=builder /usr/app/dist ./dist

# CMD node dist/src/index.js







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




