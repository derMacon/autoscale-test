### Useful cli commands
- `docker stack rm vossibility && docker stack deploy --compose-file docker-compose.yml vossibility`
- `docker stack services vossibility`
- `docker service update --force vossibility_prometheus`
- `docker service logs vossibility_prometheus`
