# Request Grammar

### Explaination
To specify a request it is possible to generate a message body following the given syntax and sending it as a post request to the supplier backend api endpoint. Each batch entry will be executed before the next one will be processed.

### Specification
```
request     := batch*
batch       := serviceName { instruction | instruction,* };
serviceName := SPRING | NODE
instruction := BENCHMARK ( messageCnt, duration ) | WAIT ( duration )
messageCnt  := [0-9]+
duration    := [0-9]+
```

### Examples
```
NODE{BENCHMARK(100, 0)};
SPRING{BENCHMARK(160, 2000), BENCHMARK(230, 0)};
NODE{BENCHMARK(10, 100)};
NODE{BENCHMARK(100, 0)};
SPRING{BENCHMARK(60, 2000), BENCHMARK(20, 0)};
NODE{BENCHMARK(10, 100)};
SPRING{BENCHMARK(60, 0)};
```
