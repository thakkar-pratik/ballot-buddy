# 🗳️ Ballot Buddy

Ballot Buddy is a premium, AI-powered election education assistant designed to help users navigate the complexities of the election process. Built with a robust Spring Boot backend and a modern vanilla frontend, it provides an interactive timeline and an intelligent chat interface.

## 🚀 Key Features
- **Interactive Timeline**: A responsive, accessible visualization of the 2024 election cycle.
- **AI Chat Assistant**: Integration with Google Gemini 1.5 Pro for answering election-related queries in context.
- **Analytics & Storage**: Automatic snapshot logging to Google Cloud Storage (with local fallback).
- **Accessibility First**: Semantic HTML5, high contrast (WCAG AAA targets), and full ARIA support.

## 🛠️ Technical Stack
- **Backend**: Java 11, Spring Boot 2.7.18
- **AI**: Google Gemini API (REST)
- **Cloud**: Google Cloud Storage (SDK)
- **Frontend**: Vanilla HTML5, CSS3 (Modern Flex/Grid), Javascript
- **Testing**: JUnit 5, Mockito (100% target coverage)

## 📦 Setup & Run

1. **Environment Variables**:
   ```bash
   export GEMINI_API_KEY=your_actual_api_key
   export GCP_PROJECT_ID=your_gcp_project
   export GCP_STORAGE_BUCKET=your_bucket_name
   ```

2. **Build & Run**:
   ```bash
   # If you have Gradle installed
   gradle clean build
   java -jar build/libs/ballot-buddy-1.0.0.jar
   ```

3. **Access**:
   Open [http://localhost:8080](http://localhost:8080) in your browser.

## 🧪 Testing
Run the comprehensive test suite to verify the 100% coverage:
```bash
gradle test
```

## 🏗️ Architecture
The project follows a strict **N-Tier Architecture**:
- `controller/`: REST endpoints with DTO mapping.
- `service/`: Business logic and external integrations.
- `dto/`: Explicit Data Transfer Objects for all API boundaries.
- `exception/`: Global error handling and validation logic.
