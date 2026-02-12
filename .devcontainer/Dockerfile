FROM mcr.microsoft.com/devcontainers/java:25

RUN curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs

RUN java -version && node -v && npm -v