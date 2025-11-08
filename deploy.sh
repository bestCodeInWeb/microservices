#!/bin/bash
# ----------------------------------------------------
# Скрипт для локальної збірки та розгортання в Minikube
# ----------------------------------------------------

# 1. Налаштовуємо Docker-середовище на Minikube
echo "🚀 Налаштування Docker-середовища Minikube..."
eval $(minikube docker-env)

# 2. Збираємо образ Keycloak (ВИПРАВЛЕНО)
echo "📦 Збірка образу sn-keycloak:latest..."
docker build -t sn-keycloak:latest -f ./sn-keycloak-rabbitmq-provider/Dockerfile ./sn-keycloak-rabbitmq-provider

# 3. Збираємо образ sn-user
echo "📦 Збірка образу sn-user:latest..."
docker build -t sn-user:latest -f Dockerfile-sn-user .

# 4. Збираємо образ sn-media (НОВИЙ КРОК)
echo "📦 Збірка образу sn-media:latest..."
docker build -t sn-media:latest ./sn-media

echo "✅ Збірка образів завершена!"

# 5. Застосовуємо всі .yml конфігурації з папки k8s/
echo "🚀 Розгортання сервісів у Kubernetes..."
kubectl apply -f k8s/

echo "🎉 Готово! Усі сервіси запущені."