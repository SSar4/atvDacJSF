FROM postgres:10
ENV POSTGRES_DB Dac
ENV POSTGRES_USER s2
ENV POSTGRES_PASSWORD 12345
COPY create.sql /docker-entrypoint-initdb.d/
