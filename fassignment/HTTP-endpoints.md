
```json
GET     /paintings
GET     /paintings/{id}
GET     /paintings/{id}/image
POST    /paintings
POST    /paintings/{id}
DELETE  /paintings
DELETE  /paintings/{id}

GET     /questions
GET     /questions/{id}
GET     /questions/byPainting/{id}
GET     /questions/{id}/image
POST    /questions/{id}
PUT     /questions/{id}
DELETE  /questions
DELETE  /questions/{id}

GET     /answers
GET     /answers/{id}
GET     /answers/byQuestion/{id}
GET     /answers/{id}/image
POST    /answers/{id}
PUT     /answers/{id}
DELETE  /answers
DELETE  /answers/{id}

GET     /filesInDatabase
GET     /filesInDatabase/{id}
POST    /filesInDatabase/{id}
PUT     /filesInDatabase/{id}
DELETE  /filesInDatabase
DELETE  /filesInDatabase/{id}

GET     /filesOnDisk/{id}
POST    /filesOnDisk/{id}
DELETE  /filesOnDisk/{id}

GET     /audioFilesInDatabase
GET     /audioFilesInDatabase/{id}
POST    /audioFilesInDatabase/{id}
PUT     /audioFilesInDatabase/{id}
DELETE  /audioFilesInDatabase
DELETE  /audioFilesInDatabase/{id}


GET     /users
GET     /users/{id}
POST    /users/signup
POST    /users/signin
PUT     /users/{id}
DELETE  /users
DELETE  /users/{id}



```