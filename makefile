# Define the default target
.DEFAULT_GOAL := help

# Define variables
DOCKER_COMPOSE = docker-compose

# Targets
build: ## Build Docker containers
	$(DOCKER_COMPOSE) build

run-dev: ## Run Docker containers with dev profile
	$(DOCKER_COMPOSE) up -d

stop: ## Stop and remove Docker containers
	$(DOCKER_COMPOSE) down

logs: ## View Docker container logs
	$(DOCKER_COMPOSE) logs -f

start: build run-dev ## Build and run Docker containers
	@echo "Built and started Docker containers."

help: ## Display available targets
	@echo "Available targets:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)
