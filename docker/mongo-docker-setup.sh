#!/usr/bin/env bash

echo "Creating db $MONGO_INITDB_MANAGEMENT_DATABASE and $MONGO_INITDB_QUEUE_DATABASE for user $MONGO_INITDB_USERNAME."

mongo "$MONGO_INITDB_QUEUE_DATABASE" \
      -u "$MONGO_INITDB_ROOT_USERNAME" \
      -p "$MONGO_INITDB_ROOT_PASSWORD" \
      --authenticationDatabase admin \
      --eval "db.createCollection('$MONGO_INITDB_PENDING_TRIAGES_COLLECTION');" \
&& \
mongo "$MONGO_INITDB_MANAGEMENT_DATABASE" \
        -u "$MONGO_INITDB_ROOT_USERNAME" \
        -p "$MONGO_INITDB_ROOT_PASSWORD" \
        --authenticationDatabase admin \
        --eval "db.createCollection('$MONGO_INITDB_MANAGEMENT_COLLECTION');" \
&& \
mongo admin \
        -u "$MONGO_INITDB_ROOT_USERNAME" \
        -p "$MONGO_INITDB_ROOT_PASSWORD" \
        --authenticationDatabase admin \
        --eval "db.createUser({
            user: '$MONGO_INITDB_USERNAME',
            pwd: '$MONGO_INITDB_PASSWORD',
            roles: [
                {role:'dbOwner', db: '$MONGO_INITDB_MANAGEMENT_DATABASE'},
                {role:'dbOwner', db: '$MONGO_INITDB_QUEUE_DATABASE'}
            ]
        });"