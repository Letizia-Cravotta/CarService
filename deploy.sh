#!/bin/bash

# This script automates the full build and deployment of the CarService
# application and its monitoring stack to Minikube.
#
# It will:
# 1. Point the Docker CLI to Minikube's internal Docker daemon.
# 2. Build the backend and frontend Docker images.
# 3. Create all necessary K8s secrets and configmaps.
# 4. Apply all K8s manifests (Deployments, Services, PVCs, RBAC) from the 'k8s/' directory.
# 5. Wait for the database and backend to be fully deployed.
# 6. List all Minikube services with their URLs.
#
# Run from the 'CarService' backend project root:
#   chmod +x deploy.sh
#   ./deploy.sh

# --- Configuration ---
# Set to 'true' to skip Docker builds (if no code has changed)
SKIP_BUILDS=false

# --- Script ---

# Exit immediately if a command exits with a non-zero status.
set -e

echo "--- Starting Full CarService Deployment ---"

# --- Step 1: Set Minikube Docker Environment ---
echo "[1/6] Pointing Docker CLI to Minikube's Docker daemon..."
eval $(minikube -p minikube docker-env)
echo "Docker environment set."


# --- Step 2: Build Docker Images ---
echo "[2/6] Docker Build Step"
read -p "Skip Docker builds (if no code has changed)? (y/n): " user_skip_builds

# Convert to lowercase
user_skip_builds=$(echo "$user_skip_builds" | tr '[:upper:]' '[:lower:]')

if [ "$user_skip_builds" != "y" ]; then
  echo "Building local Docker images..."

  echo "Building backend (car-service-backend:latest)..."
  # Assumes script is run from the backend root ('CarService')
  docker build -t car-service-backend:latest .

  echo "Building frontend (car-service-frontend:latest)..."
  # Assumes frontend project is at '../CarServiceFrontend'
  docker build -t car-service-frontend:latest ../CarServiceFrontend

  echo "Docker images built."
else
  echo "[2/6] Skipping Docker builds as requested."
fi


# --- Step 3: Apply Prerequisite K8s Resources ---
echo "[3/6] Applying K8s Secrets and ConfigMaps..."

echo "Applying Grafana SMTP secret (grafana-smtp-secret)..."
kubectl create secret generic grafana-smtp-secret \
  --from-literal=smtp-password='rmfgkcwxrlddilch' \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Applying Prometheus ConfigMap (prometheus-config)..."
# Assumes prometheus.yml is at './prometheus/prometheus.yml'
kubectl create configmap prometheus-config \
  --from-file=./prometheus/prometheus.yml \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Applying Postgres secret (postgres-secret)..."
# WARNING: Your 'k8s/postgres.yaml' MUST be configured to use this secret.
kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_DB='car_db' \
  --from-literal=POSTGRES_USER='admin' \
  --from-literal=POSTGRES_PASSWORD='password' \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Prerequisites applied."


# --- Step 4: Apply Core Deployment Manifests ---
echo "[4/6] Applying all K8s manifests from 'minikube/' directory..."
# This applies all .yaml files in the minikube/ directory
# It handles Deployments, Services, PVCs, RBAC, etc.
kubectl apply -f minikube/
echo "All manifests applied."


# --- Step 5: Wait for Critical Deployments ---
echo "[5/6] Waiting for key deployments to be ready..."

POSTGRES_DEPLOYMENT_NAME="postgres-deployment"
BACKEND_DEPLOYMENT_NAME="my-spring-deployment"

echo "Waiting for Postgres ($POSTGRES_DEPLOYMENT_NAME)..."
kubectl rollout status deployment/$POSTGRES_DEPLOYMENT_NAME --timeout=3m

echo "Waiting for Backend ($BACKEND_DEPLOYMENT_NAME)..."
kubectl rollout status deployment/$BACKEND_DEPLOYMENT_NAME --timeout=3m

echo "Deployments are ready."


# --- Step 6: Show Results ---
echo "[6/6] Deployment complete! Your services are:"
minikube service list

echo "---"
echo "You can now access your services at these URLs:"
echo "NOTE: 'minikube service --url' does not work well in this environment."
echo "Building URLs manually from NodePorts..."
echo ""

# Get the Minikube IP address
MINIKUBE_IP=$(minikube ip)

# --- Manually constructing URLs ---
# These NodePorts come from your .yaml files (e.g., prometheus-k8s.yaml)
#
# HOW TO FIX: If a URL is wrong, open its .yaml file in 'minikube/',
# find 'kind: Service', 'type: NodePort', and copy the 'nodePort:' value here.
#
GRAFANA_NODEPORT="30000"
PROMETHEUS_NODEPORT="30090"
# For the app services, we fetch the NodePort dynamically
# This is safer because your YAMLs don't specify a port (so K8s assigns one)
echo "Finding dynamically assigned NodePorts..."
BACKEND_NODEPORT=$(kubectl get service my-spring-deployment -o=jsonpath='{.spec.ports[0].nodePort}')
FRONTEND_NODEPORT=$(kubectl get service my-frontend-service -o=jsonpath='{.spec.ports[0].nodePort}')

echo "Grafana:    http://$MINIKUBE_IP:$GRAFANA_NODEPORT"
echo "Prometheus: http://$MINIKUBE_IP:$PROMETHEUS_NODEPORT"
echo "Backend:    http://$MINIKUBE_IP:$BACKEND_NODEPORT"
echo "Frontend:   http://$MINIKUBE_IP:$FRONTEND_NODEPORT"
echo "--- Deployment Finished ---"