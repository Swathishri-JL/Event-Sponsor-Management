In this project, we will build a Spring Boot application to create a REST service for the 'Event Management System' application. The 'Event Management System' application is a bridge between events seeking sponsorship and potential sponsors looking for events to back. Users can navigate the platform to understand which sponsors are backing which events and the variety of events a particular sponsor might be associated with.

For the purposes of this application, we will define two primary entities: `Sponsor` and `Event`. The `Sponsor` entity has a Many-to-Many relationship with the `Event` entity.

Database contains three tables `sponsor`, `event`, and `event_sponsor` using the given database schema.

#### Sponsor Table

| Columns  |                 Type                  |
| :------: | :-----------------------------------: |
|    id    | INTEGER (Primary Key, Auto Increment) |
|   name   |                 TEXT                  |
| industry |                 TEXT                  |

#### Event Table

| Columns |                 Type                  |
| :-----: | :-----------------------------------: |
|   id    | INTEGER (Primary Key, Auto Increment) |
|  name   |                 TEXT                  |
|  date   |                 TEXT                  |

#### Junction Table

|  Columns  |         Type          |
| :-------: | :-------------------: |
| sponsorId | INTEGER (Foreign Key) |
|  eventId  | INTEGER (Foreign Key) |

#### API methods can be executed using platforms like postman.com API management platforms