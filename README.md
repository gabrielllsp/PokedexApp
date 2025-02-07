## Pokédex Android
# Introduction
This project was developed to meet a requirement from an Android team, with the goal of creating an app to explore the world of Pokémon.

All information related to Pokémon is provided by the PokéApi, available at:
https://pokeapi.co/

Through this API, you can access the complete list of all existing Pokémon:
https://pokeapi.co/api/v2/pokemon/

You can use the limit and offset query parameters to paginate the results.

Additionally, the server returns details of each Pokémon, using either the name or ID as a parameter:
https://pokeapi.co/api/v2/pokemon/pikachu/
https://pokeapi.co/api/v2/pokemon/25/

# Objective
The purpose of this app is to allow users to explore and view details about Pokémon. The app displays a list with at least 150 Pokémon, showing basic information (name and ID). By clicking on a Pokémon, the user can see more details, such as additional attributes and images.

# Specifications
Language: Kotlin
Architecture: A solid and scalable architecture was chosen, following best practices for Android apps.
API: All data is retrieved from PokéApi, with features like pagination, detail display, and name-based search.
Libraries Used
AndroidX: Used to provide essential components for building modern, scalable user interfaces, such as the androidx.lifecycle library for lifecycle management and androidx.compose for declarative UI creation.
Retrofit: Used to facilitate communication with the Pokémon API, making HTTP requests and managing the conversion of data into Kotlin objects with the help of converter.gson.
Coroutines: Used to simplify the management of asynchronous operations, providing a more efficient way to handle network calls and other long-running tasks.
Coil: Used for efficient image loading and display, especially useful for showing Pokémon images.
Timber: Used for logging, making it easier to track issues during development and app execution.
Dagger Hilt: Used for dependency injection, making the code more modular and testable, and simplifying the setup of complex dependencies in the app.
Navigation Compose: Used to manage navigation within the app in a simple and intuitive way, leveraging Jetpack Compose's declarative approach.
Material3: Used to create user interfaces with modern and appealing components, following Material Design 3 guidelines.
JUnit and Espresso: Used for unit and UI tests to ensure the app functions correctly under various scenarios.
