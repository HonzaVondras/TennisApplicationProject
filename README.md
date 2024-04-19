# Inqool-project
This is Java server Application for managing tennis court reservations.

This apllication offers creating Courts, Reservations and managing Users and saving them to in-memory H2 database. Communication with the application is via REST endpoints as described bellow.

Court Managmnet

GET
/api/getAllCourts

Returns all courts in the database

///////////////////////////////////////////////////////////////


GET
/api/getCourtById/{id}

Returns court with {id}

///////////////////////////////////////////////////////////////

POST
/api/addCourt @RequestBody Court

Saves Court to the database

///////////////////////////////////////////////////////////////

PUT
/api/updateCourt/{id} @RequestBody Court

Updates court in database with {id} to match information with Court

///////////////////////////////////////////////////////////////

DELETE
/api/deleteCourtById/{id}

Changes the column deleted of court with {id} to true, effectively removing it from all other queries except getCourtById/{id}

///////////////////////////////////////////////////////////////

DELETE
/api/deleteAllCourts

Does the same as deleteCourtById for all Courts

///////////////////////////////////////////////////////////////


Reservation and User Managment


GET
/api/getAllReservations

Returns all courts in the database

///////////////////////////////////////////////////////////////

GET
/api/getReservationById/{id}

Returns reservation with {id}

///////////////////////////////////////////////////////////////


POST
/api/addReservation @RequestBody Reservation

Checks if it collides with another reservation on the same court. If no creates reservation and returns price, that depends on SurfaceType of the court and if it is game for four players. Always creates new User if there is no User with the same phoneNumber.

///////////////////////////////////////////////////////////////

POST

/api/updateReservation/{id} @RequestBody Reservation

Updates reservation in database with {id} to match information with Reservation

///////////////////////////////////////////////////////////////

DELETE
/api/deleteReservationById/{id}

Changes the column deleted of reservation with {id} to true, effectively removing it from all other queries except getReservationById/{id}

///////////////////////////////////////////////////////////////

DELETE
/api/deleteAllReservations

Performs deleteReservationById on all reservations in the database

///////////////////////////////////////////////////////////////

GET
/api/getReservationsByCourtId/{id}

return all reservations that are made to court with {id}

///////////////////////////////////////////////////////////////

GET
/api/getReservationsByPhoneNumber/{phoneNumber}

return all reservations that are made with {phoneNumber}

///////////////////////////////////////////////////////////////

GET
/api/getPresentReservationsByPhoneNumber/{phoneNumber}

return all reservations that are made with {phoneNumber} and aren't already past their due.

///////////////////////////////////////////////////////////////

Example of Court in JSON:

{ "name": "Court A", "surface": "CLAY", "deleted": "false"} (id is generated automaticaly)

Example of Reservation in JSON:

{ "courtId": 1, "phoneNumber": 123456789, "fullName": "Karel Novák", "startTime": "2024-04-16T14:30:00", "endTime": "2024-04-16T16:00:00", "fourPlayers": "false", "deleted": "false" }

///////////////////////////////////////////////////////////////

How to install and run application:

Just download the tennis folder and then run the TennisApplication Java file with device that has java 17 on it





