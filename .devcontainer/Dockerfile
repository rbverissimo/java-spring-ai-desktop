FROM mcr.microsoft.com/devcontainers/java:25

RUN curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs zstd sqlite3

RUN curl -fsSL https://ollama.com/install.sh | sh

RUN java -version && node -v && npm -v