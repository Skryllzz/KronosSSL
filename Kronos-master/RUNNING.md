# Running Kronos Locally

Requires **JDK 21** for the server and **JDK 8** (Corretto recommended) for the client.

## 1. Update Server (Terminal 1)

```bash
./gradlew --console=plain kronos-update-server:run
```

Listens on port **7304**. Expected output on success:
```
Update Server is now listening on 127.0.0.1:7304
```

## 2. Game Server (Terminal 2)

```bash
./gradlew --console=plain kronos-server:run
```

Listens on port **13302**. Uses `world_stage=DEV` by default — no database required, admin rights granted on account creation. Expected output on success:
```
KronosPK World (1) Server is now listening on 127.0.0.1:13302
Started server in ~13000ms.
```

> `--console=plain` is required to see actual server log output. Without it, Gradle's ANSI progress bar swallows stdout.

## 3. RuneLite Client (Terminal 3)

```bash
cd runelite
JAVA_HOME=/Users/andrewwellington/Library/Java/JavaVirtualMachines/corretto-1.8.0_422/Contents/Home \
  ./gradlew :runelite-client:run --no-daemon
```

> The client Gradle project is separate — run `./gradlew` from inside the `runelite/` directory with JDK 8.

## Notes

- Start servers **before** the client.
- `server.properties` lives in `kronos-server/` and `kronos-update-server/` — both are set to DEV mode out of the box.
- To reset to a clean state, just delete the player save files in `kronos-server/Data/`.

## Cache Versions

Two cache versions are available:

| Directory | Revision | Notes |
|---|---|---|
| `Cache/` | 184 (original) | Original Kronos cache — use with rev 184 client |
| `Cache-217/` | 217 | Extracted from Zenyte — use with rev 217 client (current default) |

To switch, edit `cache_path` in `kronos-server/server.properties`:
```
cache_path=../Cache-217   # rev 217 (current)
cache_path=../Cache       # rev 184 (original)
```
