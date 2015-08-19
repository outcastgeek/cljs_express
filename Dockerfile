# Dockerfile for running Nodejs Code

FROM ubuntu:15.04

MAINTAINER outcastgeek <outcastgeek+docker@gmail.com>

RUN echo "Running apt-get update"
RUN apt-get update --fix-missing
RUN echo "- done."

RUN echo "Installing Essential Tools"
RUN apt-get install -y runit locales \
                       curl wget git \
		                   build-essential \
		                   ca-certificates \
		                   libncurses5-dev \
		                   openssl libssl-dev \
		                   fop xsltproc \
                       unixodbc-dev pkg-config \
                       autoconf automake
RUN echo "- done."

RUN echo "Installing SODIUM ..."

ENV SODIUM_VERSION 1.0.3

RUN wget https://download.libsodium.org/libsodium/releases/libsodium-${SODIUM_VERSION}.tar.gz -O  /usr/src/libsodium-${SODIUM_VERSION}.tar.gz

RUN cd /usr/src \
    && tar zxf libsodium-${SODIUM_VERSION}.tar.gz \
    && cd libsodium-${SODIUM_VERSION} \
    && ./configure \
    && make \
    && make install \
    && cd / && rm -rf /usr/src/libsodium-${SODIUM_VERSION}

RUN  echo "- done."

RUN echo "Installing ZeroMQ ..."

ENV ZMQ_VERSION 4.1.2

RUN wget http://download.zeromq.org/zeromq-${ZMQ_VERSION}.tar.gz -O  /usr/src/zeromq-${ZMQ_VERSION}.tar.gz

RUN cd /usr/src \
    && tar zxf zeromq-${ZMQ_VERSION}.tar.gz \
    && cd zeromq-${ZMQ_VERSION} \
    && ./configure \
    && make \
    && make install \
    && ldconfig \
    && cd / && rm -rf /usr/src/zeromq-${ZMQ_VERSION}

RUN  echo "- done."

RUN echo "Installing Nodejs and NPM"

ENV NODE_VERSION v0.12.7

RUN mkdir /nodejs && curl http://nodejs.org/dist/${NODE_VERSION}/node-${NODE_VERSION}-linux-x64.tar.gz | tar xvzf - -C /nodejs --strip-components=1

RUN echo "Add Node.js installation to PATH, and set"

ENV PATH $PATH:/nodejs/bin

COPY . /code

WORKDIR /code

RUN echo "Installing Node Libraries"
RUN npm install

RUN echo "Cleanup"
RUN rm -r /tmp/*

RUN echo "- done."

EXPOSE 8080

# CMD ["/bin/bash"]
# CMD ["node", "run.js"]

RUN echo "Add Services"
ADD app.service /etc/service/app/run
RUN echo "- All Good!!!!"

CMD ["/usr/bin/runsvdir", "/etc/service"]
