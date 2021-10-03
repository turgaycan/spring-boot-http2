# spring-boot-http2

The application is related with sprint boot application with using undertow web server and http 2.

http2 is a binary protocol, you can get more detail information -> https://en.wikipedia.org/wiki/HTTP/2

# Http2 vs Http1.1

https://www.cloudflare.com/learning/performance/http2-vs-http1.1/

# The application tech stack

    Java11
    Spring Boot 2+
    Undertow 2+
    Gradle
    OkHttpClient 4+
    Log4j2
    Lombok
    H2 Db
    
    Docker 
      - build.sh
      - release.sh

# Add domain hosts file using vim or nano editor

    127.0.0.1 localhost turgay.dev
    
# Self-signed cert creation

    keytool -genkey -keyalg RSA -alias selfsigned -keystore secure.jks -storepass secret -validity 365
-- 


    What is your first and last name?
    [Unknown]:  turgay can
    What is the name of your organizational unit?
    [Unknown]:  turgay
    What is the name of your organization?
    [Unknown]:  turgay.dev
    What is the name of your City or Locality?
    [Unknown]:  istanbul
    What is the name of your State or Province?
    [Unknown]:  bakirkoy
    What is the two-letter country code for this unit?
    [Unknown]:  tr
    Is CN=turgay can, OU=turgay, O=turgay.dev, L=istanbul, ST=bakirkoy, C=tr correct?
    [no]:  yes

    Enter key password for <selfsigned>
    (RETURN if same as keystore password):  
    Re-enter new password:
---

password should be same as in the command "secret"

---

    keytool -certreq -alias selfsigned -keystore secure.jks -storepass secret -file turgay.dev.csr

https://documentation.commvault.com/commvault/v11/article?p=3714.htm

JKS -> Java Key Store 

PKCS12 came standard in the JDK with Java 9. Although I have used JKS within the application, I recommend using PKCS12.

https://en.wikipedia.org/wiki/PKCS_12

# OkHttpClient not validate certificates

https://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp

# OkHttpClient creates client pem file from self-signed server cert and validate certificates

https://jebware.com/blog/?p=340

Client certificate using via chrome browser. (if the browser cannot show the pem encoded chain, please configure the security level for a temporary time)

# Docker

**build.sh :** build a docker images via Dockerfile

**release.sh :** version the code and docker image tag and then push the local docker registry 

local docker registry

    docker run -d -p 5000:5000 --name registry registry:2

https://docs.docker.com/registry/

run docker container via image

    docker run -d -p 443:8443 --name springhttp2 localhost:5000/turgaycan.dev/spring-boot-http2:1.0.2


# Client tool - Insomnia.rest

https://insomnia.rest/

The application flow for testing

**test 1**

![client http2 - server http2](docs/http2-server-flow-client_http2.png)

**request**

```javascript
curl--request GET \
--url https://turgay.dev/rest/v10/employees
```

**response**

```javascript
{
    "code": "OK",
        "description": "SUCCESSFUL",
        "employees": [
        {
            "id": 4,
            "fullname": "Muhammed Nursoy",
            "email": "muhammed@nursoy.com",
            "salary": 35100.14,
            "startDate": 1497042000000
        },
        {
            "id": 3,
            "fullname": "Fatih Bozik",
            "email": "fatih@bozik.com",
            "salary": 25000.14,
            "startDate": 1497042000000
        },
        {
            "id": 2,
            "fullname": "Serdar Kuzucu",
            "email": "serdar@kuzucu.com",
            "salary": 15000.37,
            "startDate": 1365541200000
        },
        {
            "id": 1,
            "fullname": "Turgay Can",
            "email": "turgay@can.com",
            "salary": 10000.61,
            "startDate": 1504126800000
        }
    ]
}
```
**Insomnia rest client timeline & http trace**

* Preparing request to https://turgay.dev/rest/v10/employees
* Current time is 2021-10-01T19:58:55.859Z
* Using libcurl/7.73.0 OpenSSL/1.1.1k zlib/1.2.11 brotli/1.0.9 zstd/1.4.9 libidn2/2.1.1 libssh2/1.9.0 nghttp2/1.42.0
* Using HTTP/2
* Disable timeout
* Enable automatic URL encoding
* Disable SSL validation
* Enable cookie sending with jar of 0 cookies
* Connection 1 seems to be dead!
* Closing connection 1
* TLSv1.3 (OUT), TLS alert, close notify (256):
* Found bundle for host turgay.dev: 0x7f98c71a4690 [can multiplex]
* Re-using existing connection! (#2) with host turgay.dev
* Connected to turgay.dev (127.0.0.1) port 443 (#2)
* Using Stream ID: 3 (easy handle 0x7f98c0920a00)

> GET /rest/v10/employees HTTP/2
> Host: turgay.dev
> user-agent: insomnia/2021.5.3
> accept: */*

< HTTP/2 200
< content-type: application/json
< date: Fri, 01 Oct 2021 19:58:55 GMT


* Received 480 B chunk
* Connection #2 to host turgay.dev left intact

--

**test 2**

![http2-cert-client-call-http2-server-via-okhttpclient](docs/http2-cert-client-call-http2-server-via-okhttpclient.png)

**request**

```javascript
curl --request GET \
--url https://turgay.dev/rest/v10/http2
```
**response**

```javascript
{
  "code": "OK",
  "description": "SUCCESSFUL",
  "value": "request protocol : HTTP/2.0, example.com response : '{\"code\":\"OK\",\"description\":\"SUCCESSFUL\",\"employees\":[{\"id\":4,\"fullname\":\"Muhammed Nursoy\",\"email\":\"muhammed@nursoy.com\",\"salary\":35100.14,\"startDate\":1497042000000},{\"id\":3,\"fullname\":\"Fatih Bozik\",\"email\":\"fatih@bozik.com\",\"salary\":25000.14,\"startDate\":1497042000000},{\"id\":2,\"fullname\":\"Serdar Kuzucu\",\"email\":\"serdar@kuzucu.com\",\"salary\":15000.37,\"startDate\":1365541200000},{\"id\":1,\"fullname\":\"Turgay Can\",\"email\":\"turgay@can.com\",\"salary\":10000.61,\"startDate\":1504126800000}]}'"
}
```

**Insomnia rest client timeline & http trace**

* Preparing request to https://turgay.dev/rest/v10/http2
* Current time is 2021-10-01T20:53:49.290Z
* Using libcurl/7.73.0 OpenSSL/1.1.1k zlib/1.2.11 brotli/1.0.9 zstd/1.4.9 libidn2/2.1.1 libssh2/1.9.0 nghttp2/1.42.0
* Using HTTP 1.1
* Disable timeout
* Enable automatic URL encoding
* Disable SSL validation
* Enable cookie sending with jar of 0 cookies
* Found bundle for host turgay.dev: 0x7f98c7c826e0 [can multiplex]
* Re-using existing connection! (#3) with host turgay.dev
* Connected to turgay.dev (127.0.0.1) port 443 (#3)
* Using Stream ID: 4b (easy handle 0x7f98c30c4000)

> GET /rest/v10/http2 HTTP/2
> Host: turgay.dev
> user-agent: insomnia/2021.5.3
> accept: */*

< HTTP/2 200
< content-type: application/json
< date: Fri, 01 Oct 2021 20:53:49 GMT


* Received 651 B chunk
* Connection #3 to host turgay.dev left intact

# Performance Metrics

h2load tool
---

**h2load tool**

http1.1 client vs http2 client -> http2 server

# REST API

**test 1 result**

http2 client : 15.29s, 653.91 req/s, 341.35KB/s

http1.1 client : 6.48s, 1542.87 req/s, 953.75KB/s

----

**test 2 result**

http2 client : 90.69s, 1102.70 req/s, 575.10KB/s

http1.1 client : 51.81s, 1930.08 req/s, 1.17MB/s

----

**test 1 scenario**

request : 10000, concurrent client : 100, max concurrent stream : 10

----
http2 client
---
    h2load -n10000 -c100 -m10 https://turgay.dev/rest/v10/employees

---
    starting benchmark...
    spawning thread #0: 100 total client(s). 10000 total requests
    TLS Protocol: TLSv1.3
    Cipher: TLS_AES_128_GCM_SHA256
    Server Temp Key: ECDH P-256 256 bits
    Application protocol: h2
    progress: 10% done
    progress: 20% done
    progress: 30% done
    progress: 40% done
    progress: 50% done
    progress: 60% done
    progress: 70% done
    progress: 80% done
    progress: 90% done
    progress: 100% done

    finished in 15.29s, 653.91 req/s, 341.35KB/s
    requests: 10000 total, 10000 started, 10000 done, 10000 succeeded, 0 failed, 0 errored, 0 timeout
    status codes: 10000 2xx, 0 3xx, 0 4xx, 0 5xx
    traffic: 5.10MB (5345400) total, 264.84KB (271200) headers (space savings 61.80%), 4.58MB (4800000) data
    min         max         mean         sd        +/- sd
    time for request:     9.17ms       2.21s       1.34s    289.10ms    75.82%
    time for connect:   113.83ms       2.93s    958.82ms    743.72ms    74.00%
    time to 1st byte:   149.74ms       4.37s       2.12s    932.03ms    74.00%
    req/s           :       6.54       61.33        7.40        5.45    99.00%

----
http1.1 client
---

    h2load -n10000 -c100 -m10 --h1 "https://turgay.dev/rest/v10/employees"

    starting benchmark...
    spawning thread #0: 100 total client(s). 10000 total requests
    TLS Protocol: TLSv1.3
    Cipher: TLS_AES_128_GCM_SHA256
    Server Temp Key: ECDH P-256 256 bits
    Application protocol: http/1.1
    progress: 10% done
    progress: 20% done
    progress: 30% done
    progress: 40% done
    progress: 50% done
    progress: 60% done
    progress: 70% done
    progress: 80% done
    progress: 90% done
    progress: 100% done
    
    finished in 6.48s, 1542.87 req/s, 953.75KB/s
    requests: 10000 total, 10000 started, 10000 done, 10000 succeeded, 0 failed, 0 errored, 0 timeout
    status codes: 10000 2xx, 0 3xx, 0 4xx, 0 5xx
    traffic: 6.04MB (6330000) total, 1.00MB (1050000) headers (space savings 0.00%), 4.58MB (4800000) data
    min         max         mean         sd        +/- sd
    time for request:   432.54ms    979.32ms    576.77ms     69.63ms    75.70%
    time for connect:   286.14ms    892.03ms    531.14ms    178.20ms    65.00%
    time to 1st byte:      1.04s       1.52s       1.21s    126.74ms    68.00%
    req/s           :      15.43       16.62       15.88        0.29    60.00%

**test 2 scenario**

request : 100000, concurrent client : 100, max concurrent stream : 10 **

----
http2 client
---
    h2load -n100000 -c100 -m10 "https://turgay.dev/rest/v10/employees"
---

    starting benchmark...
    spawning thread #0: 100 total client(s). 100000 total requests
    TLS Protocol: TLSv1.3
    Cipher: TLS_AES_128_GCM_SHA256
    Server Temp Key: ECDH P-256 256 bits
    Application protocol: h2
    progress: 10% done
    progress: 20% done
    progress: 30% done
    progress: 40% done
    progress: 50% done
    progress: 60% done
    progress: 70% done
    progress: 80% done
    progress: 90% done
    progress: 100% done
    
    finished in 90.69s, 1102.70 req/s, 575.10KB/s
    requests: 100000 total, 100000 started, 100000 done, 100000 succeeded, 0 failed, 0 errored, 0 timeout
    status codes: 100000 2xx, 0 3xx, 0 4xx, 0 5xx
    traffic: 50.93MB (53405413) total, 2.58MB (2701200) headers (space savings 61.95%), 45.78MB (48000000) data
    min         max         mean         sd        +/- sd
    time for request:    19.87ms       2.48s    884.38ms    279.97ms    75.76%
    time for connect:   144.84ms       1.19s    600.86ms    292.41ms    59.00%
    time to 1st byte:   181.54ms       1.99s       1.31s    397.22ms    62.00%
    req/s           :      11.03       12.24       11.21        0.24    83.00%

----
http1.1 client
---
    h2load -n100000 -c100 -m10 --h1 "https://turgay.dev/rest/v10/employees"
----
    starting benchmark...
    spawning thread #0: 100 total client(s). 100000 total requests
    TLS Protocol: TLSv1.3
    Cipher: TLS_AES_128_GCM_SHA256
    Server Temp Key: ECDH P-256 256 bits
    Application protocol: http/1.1
    progress: 10% done
    progress: 20% done
    progress: 30% done
    progress: 40% done
    progress: 50% done
    progress: 60% done
    progress: 70% done
    progress: 80% done
    progress: 90% done
    progress: 100% done
    
    finished in 51.81s, 1930.08 req/s, 1.17MB/s
    requests: 100000 total, 100000 started, 100000 done, 100000 succeeded, 0 failed, 0 errored, 0 timeout
    status codes: 100000 2xx, 0 3xx, 0 4xx, 0 5xx
    traffic: 60.37MB (63300000) total, 10.01MB (10500000) headers (space savings 0.00%), 45.78MB (48000000) data
    min         max         mean         sd        +/- sd
    time for request:    55.12ms       1.26s    507.40ms    121.32ms    79.83%
    time for connect:   146.91ms    985.35ms    606.55ms    219.76ms    61.00%
    time to 1st byte:   202.03ms       1.52s       1.23s    252.25ms    88.00%
    req/s           :      19.30       20.01       19.48        0.15    71.00%

# Static Page

Loading...

