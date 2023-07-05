### Requirements
1. Accept only challenges that last less than three minutes,
2. As stated in `https://lichess.org/page/api-tips`, make one request at a time.
3. Should be able to forcefully accept the next challenge from a specific user and stop the current game.
4. Application should be lightweight and not contain any unnecessary dependencies.

### Run with Docker
`docker build -t lichess-bot .` \
`docker run lichess-bot`
