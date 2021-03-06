# Dockerfile for running Nodejs Code

FROM alpine:3.2

MAINTAINER outcastgeek <outcastgeek+docker@gmail.com>

ENV VERSION=v5.0.0 NPM_VERSION=3

ENV CONFIG_FLAGS="--fully-static" DEL_PKGS="libgcc libstdc++" RM_DIRS=/usr/include

#ENV CONFIG_FLAGS="--fully-static --without-npm" DEL_PKGS="libgcc libstdc++" RM_DIRS=/usr/include

RUN echo "Installing Nodejs and NPM"

RUN apk add --update curl make gcc g++ python linux-headers paxctl libgcc libstdc++ && \
  curl -sSL https://nodejs.org/dist/${VERSION}/node-${VERSION}.tar.gz | tar -xz && \
  cd /node-${VERSION} && \
  ./configure --prefix=/usr ${CONFIG_FLAGS} && \
  make -j$(grep -c ^processor /proc/cpuinfo 2>/dev/null || 1) && \
  make install && \
  paxctl -cm /usr/bin/node && \
  cd / && \
  if [ -x /usr/bin/npm ]; then \
    npm install -g npm@${NPM_VERSION} && \
    find /usr/lib/node_modules/npm -name test -o -name .bin -type d | xargs rm -rf; \
  fi && \
  apk del curl make gcc g++ python linux-headers paxctl ${DEL_PKGS} && \
  rm -rf /etc/ssl /node-${VERSION} ${RM_DIRS} \
    /usr/share/man /tmp/* /var/cache/apk/* /root/.npm /root/.node-gyp \
    /usr/lib/node_modules/npm/man /usr/lib/node_modules/npm/doc /usr/lib/node_modules/npm/html

COPY . /code

WORKDIR /code

RUN echo "Installing Node Libraries"
RUN npm install --production

RUN echo "Cleanup"
RUN rm -r /tmp/*

RUN echo "- done."

CMD ["npm", "start", "--production"]
