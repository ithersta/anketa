FROM oven/bun:latest as dependencies
WORKDIR /usr/src/app

RUN mkdir -p /temp/dep
COPY package.json bun.lockb /temp/dep/
RUN cd /temp/dep && bun install --frozen-lockfile

FROM node:latest AS build
WORKDIR /usr/src/app
COPY --from=dependencies /temp/dep/node_modules node_modules
COPY . .

ENV NODE_ENV=production
RUN npm run build

FROM node:latest AS release
WORKDIR /usr/src/app
COPY --from=dependencies /temp/dep/node_modules node_modules
COPY --from=build /usr/src/app/build ./build
COPY --from=build /usr/src/app/package.json ./package.json
COPY --from=build /usr/src/app/static ./static

ENV NODE_ENV=production
EXPOSE 3000
ENTRYPOINT [ "node", "./build/index.js" ]
