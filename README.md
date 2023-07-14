# BiteSpeed Backend Task: Identity Reconciliation

### to run
<hr>

`docker-compose up --build`

<br>

### endpoint to test
<hr>

`POST http://127.0.0.1:8080/identify`

request-body
```json
{
    "email": "abc@email.com",
    "phoneNumber": "1234567"
}
```

<br>

### resume
<hr>

PFA `shashi-resume.pdf`

<br>

### dependencies
<hr>

`All dependencies are handled via docker`

note: to keep dependencies more simple, An in-memory h2 DB (a sql DB) is used instead of MySql. But it does not have any effect on repository or mapping/query level.
Both are very similar.