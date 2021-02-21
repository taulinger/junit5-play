JUnit-Playwright proof-of-concept
========

Setup for a JUnit 5 / Playwright BDD-style testing project. Uses the JUnit ConsoleLauncher for a pretty printed output:

```
╷
└─ JUnit Jupiter ✔
   └─ Main menu test  ✔
      ├─ Given a main menu for mobile usage  ✔
      │  └─ When page loaded  ✔
      │     └─ Then the main menu should show a button in (Browser) ✔
      │        ├─ [1] type=Firefox ✔
      │        ├─ [2] type=Chromium ✔
      │        └─ [3] type=Webkit ✔
      └─ Given a main menu for desktop usage  ✔
         └─ When page loaded  ✔
            └─ Then the main menu should show a button in (Browser) ✔
               ├─ [1] type=Firefox ✔
               ├─ [2] type=Chromium ✔
               └─ [3] type=Webkit ✔

```




## Usage

Prerquisites
- JDK 11 or later must be installed (https://adoptopenjdk.net/)
- Maven 3 must be installed

Steps
- Clone the Github project:
```
git clone https://github.com/taulinger/junit-play
```
- Change to the git repository and run Maven:
```
mvn test
```