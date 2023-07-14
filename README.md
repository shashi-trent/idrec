# BiteSpeed Backend Task: Identity Reconciliation

### to run
<hr>

`docker-compose up --build`

<br>

### endpoint to test
<hr>

`POST http://127.0.0.1:8080/identify`

body
```json
{
    "email": "abc@email.com",
    "phoneNumber": "1234567"
}
```