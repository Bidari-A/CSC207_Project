# CSC207_Project
## Project Name: Traveller Buddy

### Basic information

- Group Name: Team Banana
- Group Members:(order from MarkUs)
    - Zi Hei Jayla Lee
    - Chun Yin Liang
    - Muhammad Rafie
    - Qilin Zeng
    - Ava Bidari
    - Meilin Han
- Programming Language: Java 17 SDK (+ 24 SDK)
- Developing environment: Intellij + Git
- Brief function-description of Project
    - A record for the user's past trips
    - A planner for the user's next future trip
- Special features
    - Auto-recommendations of attractions based on the destination
    - Auto-match of flights and hotels
- Use flow
  create an account with username and password -> log into dashboard ->
  create a new trip and get recommendations on attractions/flights/hotels -> auto-set current trip brief info appears on the dashboard -> complete the trip -> save the old trip to the trip list(history) -> review the completed trips on the list -> log out

### Use stories

1. As a new user, I need to create an account and log into the software.
2. As a user, I need to be able to log out.
3. As a user, I need to create a new trip.
4. As a user, I want to get advice on attractions for the destination.
5. As a user, I need to match the flight rather than searching by hand.
6. As a user, I want to match the hotel rather than searching by hand.
7. As a user, I need to be able to see trip I am going to have next.
8. As a user, I want to delete the next trip.
9. As a user, I want to review the trips I completed before.
10. As a user, I want to delete the completed trip.
11. As a user, I need to switch between the views.

### Use cases(Specifically)

### Use Case 1: Creating An Account

### Main flow:

- User presses the ” create account “ button on the login page.
- User redirected to create account page.
- User enters desired username and password.
- The system stores the user data in a database.
- User is redirected to the login page.

### Alternative Flow:

- If username exists - prompt user to enter a non-existing username.

### Use Case 2: Logging In

### Main Flow:

- User enters username and password.
- System checks if the username and password match in the database.
- Redirect the user to the main page.

### Alternative Flow:

- If username and password do not match → prompt user to re-enter information.

### Use Case 3: Creating Trip

### Main Flow:

- User presses the ‘New Trip’ button on the dashboard page.
- System redirects user to the ‘generate trip’ page.
- User is prompted to enter trip name, name of the city, date they are traveling to and from
  the city.
- User presses ‘generate trip’.
- System calls on external API to generate trip locations, accommodations, flights, and
  updates the user current trip.
- System redirects user to the ‘details’ page of the trip.

### Alternative Flows:

- If the city doesn’t exist, the system prompts user to re-enter a valid city.
- If dates is in the wrong format, system prompts user to re-enter dates.

### Use Case 4: Delete Current Trip

### Main Flow:

- Precondition: User must have created a trip
- User presses ‘delete trip’ button on the dashboard page.
- System removes the trip from the user information and database.
- Dashboard page refreshes and displays no current trip information.

### Use Case 5: View Current Trip Details

### Main Flow:

- Precondition: User must have created a trip
- User presses ‘Details’ on the dashboard page.
- User is redirected to the current trip’s ‘detail’ page.
- User is shown all information (destinations, flights, accommodation, city name, trip
  date).
- User can press ‘back’ button to be redirected to dashboard page.

### Use Case 6: Complete Trip

### Main Flow:

- Precondition: User must have created a trip
- User presses ‘complete trip’ button on the dashboard page.
- System updates user travel history in the database.
- Dashboard page refreshes and displays no current trip information.

### Use Case 7: View Travel History Details

### Main Flow:

- User presses ‘Trip list’ button.
- System redirects user to travel history page.
- User presses ‘details’ button next to desired trip name.
- System redirects user to the trip’s detail page.
- User can press ‘back’ button to be redirected to travel history page.

### Use Case 8: Delete Travel History

### Main Flow:

- User presses ‘Trip list’ button.
- System redirects user to travel history page.
- User presses ‘delete’ button next to desired trip name.
- System deletes trip information from user’s trip history in database.
- Travel history page refreshes and removes the display of the deleted trip.

### Use Case 9: Logging out

### Main Flow:

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