FROM localstack/localstack

ENV SERVICES=kinesis
ENV AWS_ACCESS_KEY_ID=docker
ENV AWS_SECRET_ACCESS_KEY=docker
ENV AWS_DEFAULT_REGION=us-east-1

COPY init-localstack-content.sh /usr/local/bin

RUN echo -e "\n\
[program:init]\n\
command=/usr/local/bin/init-localstack-content.sh" \
>> /etc/supervisord.conf
