FROM node:22-alpine3.21

WORKDIR /app

# Copy everything (no src at root)
COPY package.json .

# Download dependencies for all modules
RUN npm install

# Copy all sources now
COPY . .

# Build the client
RUN npm run build

# Run the client
ENTRYPOINT ["npm", "run", "start"]