# Spring AI Chat Application

This project is a Spring Boot application that integrates with OpenAI and Redis to provide a chat service with session management.

## Features

- Chat with OpenAI using Spring AI integration
- Session-based chat history stored in Redis
- REST API endpoints for sending messages and clearing sessions

## Requirements

- Java 17+
- Maven
- Redis server
- OpenAI API key

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/spring-ai.git
   cd spring-ai
   ```

2. **Configure environment variables:**
   - Set your OpenAI API key in your environment:
     ```sh
     export OPENAI_API_KEY=your_openai_api_key
     ```
   - Ensure Redis is running on `localhost:6379` or update `src/main/resources/application.yml` as needed.

3. **Build and run the application:**
   ```sh
   ./mvnw spring-boot:run
   ```

4. **API Endpoints:**

   - **POST /ask**
     - Send a message to the chat.
     - Parameters: `sessionId` (query), `question` (body)
     - Example:
       ```sh
       curl -X POST "http://localhost:8080/ask?sessionId=123" -d "Hello!"
       ```

   - **DELETE /clear-session**
     - Clear chat session history.
     - Parameters: `sessionId` (query)
     - Example:
       ```sh
       curl -X DELETE "http://localhost:8080/clear-session?sessionId=123"
       ```

## Project Structure

- `src/main/java/com/kerki/spring_ai/` - Main application code
- `src/main/resources/application.yml` - Application configuration
- `src/test/java/com/kerki/spring_ai/` - Test code

## License

This project is licensed under the Apache License 2.0.
