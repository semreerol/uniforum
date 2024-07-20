# UniForum

UniForum is a platform designed for university students to share posts, comment on posts, and like comments and posts, similar to Twitter. Users register by selecting their university and department.

## Project Structure

- `backend`: Contains the Spring Boot source code for the backend.

## Features

- User registration and login.
- Post sharing.
- Commenting on posts.
- Liking posts and comments.
- University and department selection during registration.

## Technologies Used

- **Backend**: Spring Boot
- **Database**: PostgreSQL

## Prerequisites

Make sure you have the following installed on your system:

- Java (JDK 8 or higher)
- Maven
- PostgreSQL

## Installation and Setup

Follow these steps to set up and run the project locally.

### Backend

1. Clone the repository:
    ```sh
    git clone https://github.com/bunyaminkalkan/uniforum.git
    cd uniforum/backend
    ```

2. Update the database configuration in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=YOUR_DATABASE_URL
    spring.datasource.username=YOUR_DATABASE_USERNAME
    spring.datasource.password=YOUR_DATABASE_PASSWORD
    ```

3. Build the Spring Boot application using Maven:
    ```sh
    mvn clean install
    ```

4. Run the Spring Boot application:
    ```sh
    mvn spring-boot:run
    ```

The Spring Boot application should now be running on `http://localhost:8080`.

## Frontend

*Under Construction*

## Usage

1. Open your browser and navigate to `http://localhost:8080` to access the backend API.
2. Use the API to perform user registration, login, and other available actions.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For any inquiries or feedback, please contact [bnkalkan41@gmail.com].
