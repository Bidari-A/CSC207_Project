# Project Blueprint – Travel Buddy
### Team Name: Team Banana
### Domain: Travel Planning
A desktop application that allows users to create accounts to review and store the travel plans that
they create and edit, recommend them locations, experiences.

## User Stories
1. As a traveller, I want to create my own account and log in to it, so that I can keep track of my
   trips and experiences.
2. As a traveller, I want to be recommended travel spots, experiences, accommodations, and
   hotels, and see their respective costs and estimated travel times, so that I can plan a full and
   affordable trip.
3. As a traveller, I want to be able to create and delete my own travel itinerary/plan to plan or
   change the places I will visit during my vacation.
4. As a user, I want to be able to view my previous travel history and details.
5. As a user, I want to complete the trip I am currently on.

## Use Cases
   ### Use Case 1: Creating An Account
   ####  Main flow:
   - User presses the ” create account “ button on the login page.
   - User redirected to create account page.
   - User enters desired username and password.
   - The system stores the user data in a database.
   - User is redirected to the login page.
   #### Alternative Flow:
   - If username exists - prompt user to enter a non-existing username.
   ### Use Case 2: Logging In
   ####  Main Flow:
   - User enters username and password.
   - System checks if the username and password match in the database.
   - Redirect the user to the main page.
   #### Alternative Flow:
   - If username and password do not match → prompt user to re-enter information.
   ### Use Case 3: Creating Trip
   #### Main Flow:
   - User presses the ‘New Trip’ button on the dashboard page.
   - System redirects user to the ‘generate trip’ page.
   - User is prompted to enter trip name, name of the city, date they are traveling to and from
   the city.
   - User presses ‘generate trip’.
   - System calls on external API to generate trip locations, accommodations, flights, and
   updates the user current trip.
   - System redirects user to the ‘details’ page of the trip.
   #### Alternative Flows:
   - If the city doesn’t exist, the system prompts user to re-enter a valid city.
   - If dates is in the wrong format, system prompts user to re-enter dates.
   ### Use Case 4: Delete Current Trip
   #### Main Flow:
   - Precondition: User must have created a trip
   - User presses ‘delete trip’ button on the dashboard page.
   - System removes the trip from the user information and database.
   - Dashboard page refreshes and displays no current trip information.
   ### Use Case 5: View Current Trip Details
   #### Main Flow:
   - Precondition: User must have created a trip
   - User presses ‘Details’ on the dashboard page.
   - User is redirected to the current trip’s ‘detail’ page.
   - User is shown all information (destinations, flights, accommodation, city name, trip
   date).
   - User can press ‘back’ button to be redirected to dashboard page.
   ### Use Case 6: Complete Trip
   #### Main Flow:
   - Precondition: User must have created a trip
   - User presses ‘complete trip’ button on the dashboard page.
   - System updates user travel history in the database.
   - Dashboard page refreshes and displays no current trip information.
   ### Use Case 7: View Travel History Details
   #### Main Flow:
   - User presses ‘Trip list’ button.
   - System redirects user to travel history page.
   - User presses ‘details’ button next to desired trip name.
   - System redirects user to the trip’s detail page.
   - User can press ‘back’ button to be redirected to travel history page.
   ### Use Case 8: Delete Travel History
   #### Main Flow:
   - User presses ‘Trip list’ button.
   - System redirects user to travel history page.
   - User presses ‘delete’ button next to desired trip name.
   3- System deletes trip information from user’s trip history in database.
   - Travel history page refreshes and removes the display of the deleted trip.
   ### Use Case 9: Logging out
   #### Main Flow:
   - Precondition: User must be logged in
   - User presses ‘log out’ button on the dashboard page.
   - System updates that the user has logged out.
   - System redirects the user to the log-in page.

## MVP Table
| Use Case / User Story         | Lead Developer |
|-------------------------------|----------------|
| Creating Trip                 | Ava            |
| View Travel History Details   | Muhammad       |
| Delete Current Trip           | Meilin Han     |
| Delete Travel History         | Qilin Zeng     |
| View Current Trip Details     | Winston        |
| Complete Trip                 | Jayla          |

## Proposed Entitles

### Class Diagram

#### User
| Attribute      | Type          |
|----------------|----------------|
| username       | String         |
| password       | String         |
| current_trip   | Trip           |
| trip_history   | List<Trip>     |

---

#### Trip
| Attribute          | Type                      |
|--------------------|---------------------------|
| name               | String                    |
| cityName           | String                    |
| dates              | List<Date>                |
| attractions        | List<Destination>         |
| accommodations     | List<Accommodation>       |
| flights            | List<Flight>              |

---

#### Destination
| Attribute      | Type    |
|----------------|----------|
| name           | String   |
| location       | String   |
| description    | String   |
| time           | Float    |
| price          | Float    |

---

#### Accommodation
| Attribute  | Type   |
|-------------|---------|
| name        | String  |
| address     | String  |
| price       | Float   |
| rating      | Float   |

---

#### Flight
| Attribute     | Type  |
|----------------|--------|
| airlineName    | String |
| date           | Date   |
| price          | Float  |

---

**Relationships:**
- `User` → `Trip` (1 current_trip, many in trip_history)
- `Trip` → `Destination` (List)
- `Trip` → `Accommodation` (List)
- `Trip` → `Flight` (List)

(exact picture: referring to the Entitles.png in the same folder)

## Proposed APIs
- Google Gemini: https://ai.google.dev/gemini-api/docs
- SerpAPI (Google Flights): https://serpapi.com/users/welcome
These APIs collectively support destination search, route computation, flight and hotel discovery,
and AI-assisted travel plan recommendations