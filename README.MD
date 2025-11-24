### Stockfish engine

Stockfish present in `runtime` is currently in version `17.1`, available to download under
address https://stockfishchess.org/download/.

### Run with Docker

`docker build -t lichess-bot .` \
`docker run -dp 8000:8000 -e LICHESS_BOT_TOKEN='<YOUR_TOKEN_HERE>' lichess-bot`
