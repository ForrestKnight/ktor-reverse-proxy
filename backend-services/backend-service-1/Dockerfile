# Use official Node.js image
FROM node:18
WORKDIR /app

# Copy package files and install dependencies
COPY package.json ./
RUN npm install

# Copy the rest of the files
COPY . .

# Expose port 8000 and run the server
EXPOSE 8000
CMD ["node", "server.js"]
