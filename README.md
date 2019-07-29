# Triage assistance service integrable into telehealth systems

## Setup with docker

1. Configure the environment variables with `.env` file in `docker/` directory.
You may want to rename `example.env` to `.env` to get started quickly.

1. Run the following script:
    ```bash
    ./run-docker.sh
    ```

## To-Dos and improvements

- [ ] Endpoint to query the pending cases queue size (as a fallback for the push notifications).
- [ ] Parameterize usernames, passwords, hostnames and ports.