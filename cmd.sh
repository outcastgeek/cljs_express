#!/bin/bash

BASEDIR=$(dirname $0)

case $1 in

  clean_host)
    docker rm $(docker ps -a -q)
    docker rmi $(docker images | grep "^<none>" | awk "{print $3}")
    ;;
  check_local_port)
    lsof -i :$2
    ;;
  wipe_clean)
    # Delete all containers
    docker rm $(docker ps -a -q)
    # Delete all images
    docker rmi $(docker images -q)
    ;;
  install_phx)
    mix archive.install https://github.com/phoenixframework/phoenix/releases/download/v0.15.0/phoenix_new-0.15.0.ez
    #mix archive.install hex phoenix # this is the future
    ;;
  phx_dev_release)
    MIX_ENV=dev mix do phoenix.digest, release
    ;;
  phx_prod_release)
    MIX_ENV=prod mix do phoenix.digest, release
    ;;
  new_phx)
    mix phoenix.new $2
    ;;
  phx_dev)
     #PORT=$2 iex -S mix do deps.get, compile, compile.protocols, phoenix.routes, phoenix.digest, phoenix.server
     PORT=$2 iex -S mix phoenix.server
    ;;
  phx_prod)
    #MIX_ENV=prod PORT=$2 elixir --detached -S mix phoenix.server
    echo "Starting Application"
    cd $BASEDIR && elixir -S mix phoenix.server
    ;;
  phx_db_migrate)
    echo "Running DB Migrations"
    cd $BASEDIR && mix ecto.migrate
    echo "Done with the DB Migrations"
    ;;
  phx_db_seed)
    echo "Seeding DB"
    cd $BASEDIR && mix run seeds.exs
    echo "Done Seeding the DB"
    ;;
  esac
exit 0
