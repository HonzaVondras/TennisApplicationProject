# Inqool-project
This is Java server Application for managing tennis court reservations.

It offers creating Courts, Reservations and managing Users and saving them to in-memory H2 database. Communication with the application is via REST endpoints described bellow.

Court Managmnet

GET
/api/getAllCourts
Returns all courts in the database

GET
/api/getCourtById/{id}
Returns court that has {id}

POST
/api/addCourt @RequestBody Court
Saves Court to the database

PUT

/api/updateCourt/{id} @RequestBody Court
Updates court in database with {id} to match information with Court

DELETE

