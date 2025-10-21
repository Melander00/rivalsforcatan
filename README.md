# To run

Docker files for both Server and Client

## Server
```bash
docker build -t catan-server .
docker run --rm -p 5000:5000 catan-server
```

## Client
Be sure to run the client in a TTY to make sure everything works correctly.
```bash
cd js-client
docker build -t catan-client .
docker run --rm -it -e HOST_PORT=5000 -e HOST_ADDRESS=<address> catan-client
```

If you are running the server locally you need to use `host.docker.internal`
instead of `localhost`.
```bash
docker run --rm -it -e HOST_PORT=5000 -e HOST_ADDRESS=host.docker.internal catan-client
```

