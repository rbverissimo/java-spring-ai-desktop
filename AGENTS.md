# Coltranic (Coltran AI) - Project Context & Coding Standards

## Project Overview
Coltranic is a desktop-grade, locally hosted AI orchestration tool and chatbot. It is designed to run securely and autonomously on the end-user's machine. 
*   **Core Directive:** Zero cloud dependency for core features. All AI models, vector stores, and execution environments must be orchestrated locally.
*   **Target Environment:** Containerized for development to avoid local OS dependency conflicts. The final build runs locally on the user's OS.

## Backend Standards (Java 25)
*   **Style:** Write senior-level, production-ready Java. Use modern Java 25 language features (pattern matching, switch expressions, records, virtual threads).
*   **AI Orchestration:** When dealing with LLM integrations, prioritize robust local interaction patterns (e.g., managing local Ollama processes, handling streaming responses, and local context windows).
*   **Performance:** Assume the user's machine will be heavily taxed by local AI models. Backend code must be highly concurrent and memory-efficient. Use Virtual Threads for blocking I/O tasks like model inference calls.
*   **No Boilerplate:** Omit obvious comments and unnecessary getters/setters. Rely on Records for data transfer objects.

## Frontend Standards (React & TypeScript)
*   **Paradigm:** Strict declarative programming. 
*   **Styling:** Mobile-first, responsive design using Tailwind CSS. Avoid custom CSS files unless absolutely necessary.
*   **Typing:** Strict TypeScript. Do not use `any`. Define comprehensive interfaces for all state objects and API responses.
*   **State Management:** Optimize for streaming text updates (crucial for local AI chatbot UI). Ensure smooth rendering of Markdown and code blocks within the chat interface without triggering unnecessary DOM repaints.

## Workflow & Git Constraints
*   **Testing:** Before proposing structural changes, ensure unit tests exist or provide them.
*   **Dependencies:** Do not introduce new heavy dependencies (NPM or Maven/Gradle) without explicit authorization. Favor native standard library solutions first.