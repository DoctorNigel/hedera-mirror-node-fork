FROM node:18.12.1-bullseye-slim
LABEL maintainer="mirrornode@hedera.com"

# Setup
ENV DEBIAN_FRONTEND=noninteractive
ENV NODE_ENV production
EXPOSE 5551
HEALTHCHECK --interval=10s --retries=3 --start-period=25s --timeout=2s CMD wget -q -O- http://localhost:5551/health/liveness
WORKDIR /home/node/app/
COPY . ./

# Install OS updates, required packages, and dependencies
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y wget && \
    apt-get install -y build-essential python3 && \
    npm ci --only=production && \
    npm cache clean --force --loglevel=error && \
    chown -R node:node . && \
    apt-get remove -y build-essential python3 && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*
USER node

# Run
ENTRYPOINT ["node", "--experimental-specifier-resolution=node", "server.js"]
