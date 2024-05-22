# Optical Telephone Company

This project was initially created in Visual Basic 5. It is being migrated to Java with the aid of vite.js react using typescript as the frontend,
with spring-boot security, as the backend.

## Tests

Postman is being used to script all of the tests thus far. Writing component and integration tests is the focus in the future.

## Things to do

1. The process of data seeding must be finished.
2. [] To obtain all paid calls and call histories for users, fix the call history endpoint.
3. [] Resolve payment pay so that the admin user can oversee all payments.
4. [] Adjust the invoice page so that the admin user can control all of the invoices.
5. [] Resolve get all users so the admin user can oversee
6. [] Mend the connection between calls and invoicing
7. [] Fix data: When updating from one table or object to another, data is deleted. This requires Some mending.
8. [] Correct call status updates upon invoice
9. [] Update call statuses after payment
10. [] Create component tests.
11. [] Compose integration testing.
12. [] CI/CD for GitHub Action is currently not working
13. [] Make sure the test repositories are created and added to this application. Action pipeline on GitHub
14. [] Repository for components
15. [] Repository Integration assessments
16. [] To host the available Postman scripts, create a repository and provide a link to it here.
17. [] Add a successfully made call message
18. [] Add a successfully registered user message

## Seeding of data

To carry out the data seeding, run the script at./seed_data.sh.

### Front-end

Typescript is used to react, and vite.js is used in development.

### Spin and deploy every service using docker compose

By doing this, the frontend and backend will be deployed to your localhost.

#### Services to set up

Installing the following services is required:

1. The database used to store data is called PostgresSQL. Search for PostgresSQL on google and install locally (current version is 16.1).
2. [] Create a database called `otcDb` and store the password and username.
3. [] Install RabbitMQ to handle messaging for events.

#### Environmental variables

To successfully deploy the application, you must have these variables available on your commandline terminal, before executing the
docker compose up, command -

POSTGRES_DB=your_database
POSTGRES_PASSWORD=your_password
POSTGRES_USER=your_user
RABBITMQ_USERNAME=your_username
RABBITMQ_PASSWORD=your_password

To execute the application on the GitHub CI/CD pipeline, you will also need the variables set up in your GitHub action secret section.

#### Release the frontend and backend.

All of the necessary backend files are compiled by the Dockerfile located in the backend directory.
It is packaged for deployment into a war file.

Additionally, the frontend Dockerfile generates and packages the required files for deployment.

##### Use the Dockerfile to deploy the backend.

`cd otc-backend`
If is `ls` or list for the folders and files of the above directory, Dockerfile should be listed.

Use the following command to generate the backend Docker image:

`./mvnw clean package -DskipTests` (packages the backend without running the tests)

`docker image build -t otc-backend-springboot-docker-img .`

##### Frontend repo

Head of into the frontend directory - This command creates the frontend docker image, by executing the following -

Make sure to clone the frontend repo -

https://github.com/yvonnekw/otc_frontend.git


`cd otc-frontend`

Execute the following command to containarise the application and create a docker image -

`npm run build`

`docker build -t otc-frontend-vite-react-docker-img .`

##### Deploy the application

`docker compose up`

Open your web browser to see the application -

http://localhost:2000

#### After use, shut down the services.

`docker compose down`

### Github CI/CD

For the CI/CD process, this application uses GitHub activities.

Verify that the GitHub action secrets are configured with the relevant environmental variables as mentioned earlier.

### Tests

The tests are currently written in Postman. In the future, component and integration tests will be written.

## To pull and run the remote docker images

`docker pull yvonnetest/otc-vite-react-frontend-image`

`docker pull yvonnetest/otc-springboot-backend-image`

`docker images`

`docker run -p 8000:8000 yvonnetest/otc-springboot-backend-image:latest`

`docker run -p 2000:2000 yvonnetest/otc-vite-react-frontend-image:latest`

