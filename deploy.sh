#!/bin/bash
cd /home/ubuntu/finsight
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d --remove-orphans
