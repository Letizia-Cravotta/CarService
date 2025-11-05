#!/bin/bash

# This script automates the full build and deployment of the CarService
# application and its monitoring stack to Minikube.
#
# It will:
# 1. Check if Minikube is running, and start it if not.
# 2. Point the Docker CLI to Minikube's internal Docker daemon.
# 3. Build the backend and frontend Docker images.
# 4. Create all necessary K8s secrets and configmaps.
# 5. Apply all K8s manifests (Deployments, Services, PVCs, RBAC) from the 'minikube/' directory.
# 6. Wait for the database and backend to be fully deployed.
# 7. List all Minikube services with their URLs.
#
# Run from the 'CarService' backend project root:
#   chmod +x deploy.sh
#   ./deploy.sh

# --- Configuration ---
SKIP_BUILDS=false
PID_FILE=".k8s-pids"

# --- Script ---

func_deploy() {

echo "--- Starting Full CarService Deployment ---"

# --- Step 1: Set Minikube Docker Environment ---
echo "[1/6] Pointing Docker CLI to Minikube's Docker daemon..."
eval $(minikube -p minikube docker-env)
echo "Docker environment set."


# --- Step 2: Build Docker Images ---
echo "[2/6] Docker Build Step"
read -p "Skip Docker builds (if no code has changed)? (y/n): " user_skip_builds

user_skip_builds=$(echo "$user_skip_builds" | tr '[:upper:]' '[:lower:]')

if [ "$user_skip_builds" != "y" ]; then
  echo "Building local Docker images..."

  echo "Building backend 1 (car-service-backend:latest)..."
  docker build -t car-service-backend:latest .

  echo "Building backend 2 (car-service-second-backend-image:latest)..."
  cd ../otherBackend
  docker build -t car-service-second-backend-image:latest .
  cd ../CarService

  echo "Building frontend (car-service-frontend:latest)..."
  cd ../CarServiceFrontend
  docker build -t car-service-frontend:latest .
  cd ../CarService

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
kubectl create configmap prometheus-config \
  --from-file=./prometheus/prometheus.yml \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Applying Postgres secret (postgres-secret)..."
kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_DB='car_db' \
  --from-literal=POSTGRES_USER='admin' \
  --from-literal=POSTGRES_PASSWORD='password' \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Prerequisites applied."


# --- Step 4: Apply Core Deployment Manifests ---
echo "[4/6] Applying all K8s manifests from 'minikube/' directory..."
kubectl apply -f minikube/

echo "Restarting deployments to apply new images..."
kubectl rollout restart deployment backend-deployment
kubectl rollout restart deployment second-backend-deployment
kubectl rollout restart deployment frontend-deployment

echo "All manifests applied and deployments restarted."

# --- Step 5: Wait for Critical Deployments ---
echo "[5/6] Waiting for key deployments to be ready..."

POSTGRES_DEPLOYMENT_NAME="postgres-deployment"
BACKEND_DEPLOYMENT_NAME="backend-deployment"
SECOND_BACKEND_DEPLOYMENT_NAME="second-backend-deployment"

echo "Waiting for Postgres ($POSTGRES_DEPLOYMENT_NAME)..."
kubectl rollout status deployment/$POSTGRES_DEPLOYMENT_NAME --timeout=3m

echo "Waiting for Backend ($BACKEND_DEPLOYMENT_NAME)..."
kubectl rollout status deployment/$BACKEND_DEPLOYMENT_NAME --timeout=3m

echo "Waiting for Backend 2($SECOND_BACKEND_DEPLOYMENT_NAME)..."
kubectl rollout status deployment/$SECOND_BACKEND_DEPLOYMENT_NAME --timeout=3m

echo "Deployments are ready."


# --- Step 6: Show Results ---
echo "[6/6] Deployment complete! Your services are:"
minikube service list

echo "---"
    echo "Your application is deployed."
    echo "To connect from your Windows browser, run:"
    echo ""
    echo "   ./deploy.sh connect"
    echo ""
}

# Function to start all port-forwards
func_connect() {
    func_disconnect

    echo "--- Starting background port-forwards ---"

    # Start Frontend
    echo "Forwarding Frontend (http://localhost:8080)..."
    kubectl port-forward service/frontend-service 8080:80 &
    echo $! >> $PID_FILE

    # Start Backend
    echo "Forwarding Backend (http://localhost:8081)..."
    kubectl port-forward service/backend-service 8081:8080 &
    echo $! >> $PID_FILE

    # Start Grafana (adjust service name if needed)
    echo "Forwarding Grafana (http://localhost:3000)..."
    kubectl port-forward service/grafana 3000:3000 &
    echo $! >> $PID_FILE

    # Start Prometheus (adjust service name if needed)
    echo "Forwarding Prometheus (http://localhost:9090)..."
    kubectl port-forward service/prometheus 9090:9090 &
    echo $! >> $PID_FILE

    echo "Forwarding PostgreSQL (localhost:5432)..."
    kubectl port-forward service/postgres-db-service 5432:5432 &
    echo $! >> $PID_FILE

    # Start Backend 2
    echo "Forwarding Backend 2(http://localhost:8082)..."
    kubectl port-forward service/second-backend-service 8082:8080 &
    echo $! >> $PID_FILE

    echo "---"
    echo "All forwards are running in the background."
    echo "Access your apps on 'localhost' from your Windows browser."
    echo "To stop them, run: ./deploy.sh disconnect"
}

# Function to stop all port-forwards
func_disconnect() {
    echo "--- Stopping background port-forwards ---"
    if [ -f $PID_FILE ]; then
        while read pid; do
            if kill -0 $pid 2>/dev/null; then
                echo "Stopping process $pid..."
                kill $pid
            else
                echo "Process $pid already stopped."
            fi
        done < $PID_FILE

        rm $PID_FILE
        echo "All forwards stopped."
    else
        echo "No PID file found. Nothing to stop."
    fi
}

# --- Main Script Logic ---

# Exit immediately if a command exits with a non-zero status.
set -e


# --- Minikube Status Check ---
echo "Checking Minikube status..."
# Get the status of the 'host' component for the 'minikube' profile
# If this command fails (minikube not installed, profile not found), 'set -e' will exit the script.
MINIKUBE_STATUS=$(minikube -p minikube status -f '{{.Host}}' || echo "Stopped")

if [ "$MINIKUBE_STATUS" != "Running" ]; then
    echo "Minikube is not running (Current status: $MINIKUBE_STATUS). Starting Minikube..."
    minikube -p minikube start
    echo "Minikube started."
else
    echo "Minikube is already running."
fi
echo "---"


# --- Command Handling ---

COMMAND=$1
if [ -z "$COMMAND" ]; then
    COMMAND="deploy"
fi

case $COMMAND in
    deploy)
        func_deploy
        ;;
    connect)
        func_connect
        ;;
    disconnect)
        func_disconnect
        ;;
    *)
        echo "Error: Unknown command '$COMMAND'"
        echo "Usage: $0 [deploy|connect|disconnect]"
        exit 1
        ;;
esac